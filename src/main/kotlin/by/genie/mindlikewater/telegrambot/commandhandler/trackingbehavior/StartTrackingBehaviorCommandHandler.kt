package by.genie.mindlikewater.telegrambot.commandhandler.trackingbehavior

import by.genie.mindlikewater.helper.ChatHelper
import by.genie.mindlikewater.helper.MessageHelper
import by.genie.mindlikewater.persistence.domain.Chat
import by.genie.mindlikewater.persistence.domain.TrackedBehavior
import by.genie.mindlikewater.persistence.domain.enums.TrackingType
import by.genie.mindlikewater.persistence.repository.TrackedBehaviorRepository
import by.genie.mindlikewater.telegrambot.commandhandler.CommandHandler
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import java.lang.IllegalArgumentException

@Component
class StartTrackingBehaviorCommandHandler(
    private val chatHelper: ChatHelper,
    private val trackedBehaviorRepository: TrackedBehaviorRepository,
    private val messageHelper: MessageHelper,
    private val objectMapper: ObjectMapper,
) : CommandHandler {

    override fun command(): String {
        return "/track"
    }

    override fun handle(text: String, chat: Chat): BotApiMethodMessage {
        if (chat.context == null) {
            chatHelper.updateActiveCommandAndContext(chat, command(), Context(Status.ASKED_FOR_NAME, null))
            return messageHelper.buildSendMessage(chat, "track.enter-name")
        }
        val context = objectMapper.readValue(chat.context, Context::class.java)
        return when (context.status) {
            Status.ASKED_FOR_NAME -> saveNameAndAskForType(text, chat)
            Status.ASKED_FOR_TYPE -> saveTrackedBehavior(context.name!!, text, chat)
        }
    }

    private fun saveNameAndAskForType(name: String, chat: Chat): SendMessage {
        chatHelper.updateContext(chat, Context(Status.ASKED_FOR_TYPE, name))
        return messageHelper.buildSendMessage(
            chat,
            "track.select-type",
            listOf(KeyboardRow(
                TrackingType.values()
                    .map { type -> (KeyboardButton(type.capitalize())) }
            ))
        )
    }

    private fun saveTrackedBehavior(name: String, capitalizedType: String, chat: Chat): SendMessage {
        chatHelper.resetActiveCommandAndContext(chat)
        val type = try {
            TrackingType.valueOf(capitalizedType.uppercase())
        } catch (e: IllegalArgumentException) {
            return messageHelper.buildSendMessage(chat, "error.instructions-unclear")
        }
        trackedBehaviorRepository.save(TrackedBehavior(chat.id!!, name, type))
        return messageHelper.buildSendMessage(chat, "track.success")
    }

    private enum class Status {
        ASKED_FOR_NAME, ASKED_FOR_TYPE
    }

    private data class Context(
        val status: Status,
        val name: String?,
    )

}
