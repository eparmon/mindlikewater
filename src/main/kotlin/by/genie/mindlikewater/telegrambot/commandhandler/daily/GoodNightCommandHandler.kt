package by.genie.mindlikewater.telegrambot.commandhandler.daily

import by.genie.mindlikewater.persistence.domain.Chat
import by.genie.mindlikewater.persistence.domain.enums.TrackingType
import by.genie.mindlikewater.persistence.repository.TaskRepository
import by.genie.mindlikewater.persistence.repository.TrackedBehaviorEntryRepository
import by.genie.mindlikewater.telegrambot.commandhandler.CommandHandler
import by.genie.mindlikewater.util.formatTime
import org.springframework.context.support.AbstractMessageSource
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@Component
class GoodNightCommandHandler(
    private val messageSource: AbstractMessageSource,
    private val trackedBehaviorEntryRepository: TrackedBehaviorEntryRepository,
    private val taskRepository: TaskRepository,
) : CommandHandler {

    override fun command(): String {
        return "/good_night"
    }

    override fun handle(text: String, chat: Chat): BotApiMethodMessage {
        val responseBuilder = StringBuilder()
            .appendLine(messageSource.getMessage("evening.prefix", null, Locale.getDefault()))
            .appendLine()

        val startOfDay = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).plusHours(4)

        val trackedBehaviorEntriesToday =
            trackedBehaviorEntryRepository.findAllByTrackedBehaviorChatIdAndTimestampAfterAndDeletedAtNull(
                chat.id!!,
                startOfDay
            )

        val entriesByBehavior = trackedBehaviorEntriesToday
            .groupBy { trackedBehaviorEntry -> trackedBehaviorEntry.trackedBehavior!! }

        entriesByBehavior.filter { it.key.type == TrackingType.SIMPLE }
            .forEach { (behavior, _) -> responseBuilder.appendLine(behavior.name) }

        entriesByBehavior.filter { it.key.type == TrackingType.TIME }
            .forEach { (behavior, entries) ->
                responseBuilder.append(behavior.name)
                    .append(": ")
                    .appendLine(formatTime(entries.sumOf { it.durationSeconds!! }))
            }

        taskRepository.findByChatIdAndDoneAfter(chat.id!!, startOfDay)
            .forEach { task -> responseBuilder.appendLine(task.name) }

        responseBuilder.appendLine()
            .appendLine(messageSource.getMessage("evening.postfix", null, Locale.getDefault()))

        return SendMessage("${chat.externalId}", responseBuilder.toString())
    }

}
