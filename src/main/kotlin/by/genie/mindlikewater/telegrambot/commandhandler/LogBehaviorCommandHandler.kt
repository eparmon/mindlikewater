package by.genie.mindlikewater.telegrambot.commandhandler

import by.genie.mindlikewater.helper.ChatHelper
import by.genie.mindlikewater.helper.MessageHelper
import by.genie.mindlikewater.persistence.domain.Chat
import by.genie.mindlikewater.persistence.domain.TrackedBehavior
import by.genie.mindlikewater.persistence.domain.TrackedBehaviorEntry
import by.genie.mindlikewater.persistence.domain.enums.TrackingType
import by.genie.mindlikewater.persistence.repository.TrackedBehaviorEntryRepository
import by.genie.mindlikewater.persistence.repository.TrackedBehaviorRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import java.time.OffsetDateTime
import kotlin.time.Duration

@Component
class LogBehaviorCommandHandler(
    private val chatHelper: ChatHelper,
    private val trackedBehaviorRepository: TrackedBehaviorRepository,
    private val trackedBehaviorEntryRepository: TrackedBehaviorEntryRepository,
    private val messageHelper: MessageHelper,
    private val objectMapper: ObjectMapper,
) : CommandHandler {

    override fun command(): String {
        return "/log"
    }

    override fun handle(text: String, chat: Chat): BotApiMethodMessage {
        if (chat.context == null) {
            return askWhichBehavior(chat)
        }
        val context = objectMapper.readValue(chat.context, Context::class.java)
        return when (context.status) {
            Status.ASKED_FOR_BEHAVIOR -> {
                val trackedBehavior = trackedBehaviorRepository.findByChatIdAndNameAndDeletedAtNull(chat.id!!, text)
                if (trackedBehavior == null) {
                    chatHelper.resetActiveCommandAndContext(chat)
                    return messageHelper.buildSendMessage(chat, "error.behavior-not-found")
                }
                return when (trackedBehavior.type) {
                    TrackingType.SIMPLE -> logSimpleBehavior(chat, trackedBehavior)
                    TrackingType.TIME -> askForDuration(chat, trackedBehavior)
                    null -> error("")
                }
            }
            Status.ASKED_FOR_DURATION -> logTimeBehavior(text, chat)
        }
    }

    private fun askWhichBehavior(chat: Chat): SendMessage {
        chatHelper.updateActiveCommandAndContext(chat, command(), Context(Status.ASKED_FOR_BEHAVIOR, null))

        val trackedBehaviors = trackedBehaviorEntryRepository
            .findAllByTrackedBehaviorChatIdAndTimestampAfterAndTrackedBehaviorDeletedAtNull(
                chat.id!!,
                OffsetDateTime.now().minusDays(30)
            )
            .sortedByDescending(TrackedBehaviorEntry::timestamp)
            .map { trackedBehaviorEntry -> trackedBehaviorEntry.trackedBehavior!!.name }
            .distinct()
            .toMutableList()

        trackedBehaviorRepository.findAllByChatIdAndDeletedAtNull(chat.id!!)
            .filterNot { trackedBehaviors.contains(it.name) }
            .forEach { trackedBehaviors.add(it.name) }

        val keyboardRows = trackedBehaviors
            .map { trackedBehavior -> KeyboardRow(listOf(KeyboardButton(trackedBehavior!!))) }
        return messageHelper.buildSendMessage(chat, "log.which-behavior", keyboardRows)
    }

    private fun logSimpleBehavior(chat: Chat, trackedBehavior: TrackedBehavior): SendMessage {
        chatHelper.resetActiveCommandAndContext(chat)
        trackedBehaviorEntryRepository.save(TrackedBehaviorEntry(trackedBehavior))
        return messageHelper.buildSendMessage(chat, "log.success")
    }

    private fun askForDuration(chat: Chat, trackedBehavior: TrackedBehavior): SendMessage {
        chatHelper.updateContext(chat, Context(Status.ASKED_FOR_DURATION, trackedBehavior))
        return messageHelper.buildSendMessage(chat, "log.how-long")
    }

    private fun logTimeBehavior(durationString: String, chat: Chat): SendMessage {
        val behavior = objectMapper.readValue(chat.context, Context::class.java).behavior
        chatHelper.resetActiveCommandAndContext(chat)
        val duration = try {
            Duration.parse(durationString)
        } catch (e: IllegalArgumentException) {
            return messageHelper.buildSendMessage(chat, "error.instructions-unclear")
        }
        trackedBehaviorEntryRepository.save(TrackedBehaviorEntry(behavior!!, duration.inWholeSeconds.toInt()))
        return messageHelper.buildSendMessage(chat, "log.success")
    }

    private enum class Status {
        ASKED_FOR_BEHAVIOR, ASKED_FOR_DURATION
    }

    private data class Context(
        val status: Status,
        val behavior: TrackedBehavior?
    )

}
