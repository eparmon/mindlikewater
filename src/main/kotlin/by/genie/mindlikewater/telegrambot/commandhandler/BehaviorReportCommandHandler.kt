package by.genie.mindlikewater.telegrambot.commandhandler

import by.genie.mindlikewater.persistence.domain.Chat
import by.genie.mindlikewater.persistence.domain.TrackedBehavior
import by.genie.mindlikewater.persistence.domain.TrackedBehaviorEntry
import by.genie.mindlikewater.persistence.domain.enums.TrackingType
import by.genie.mindlikewater.persistence.repository.TrackedBehaviorEntryRepository
import by.genie.mindlikewater.util.formatTime
import by.genie.mindlikewater.util.isWithinLastMonth
import org.springframework.context.support.AbstractMessageSource
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import java.util.*

@Component
class BehaviorReportCommandHandler(
    private val trackedBehaviorEntryRepository: TrackedBehaviorEntryRepository,
    private val messageSource: AbstractMessageSource,
) : CommandHandler {

    override fun command(): String {
        return "/report"
    }

    override fun handle(text: String, chat: Chat): BotApiMethodMessage {
        val reportBuilder = StringBuilder()
            .appendLine(messageSource.getMessage("report.simple-behaviors-prefix", null, Locale.getDefault()))

        val entriesByBehavior: Map<TrackedBehavior, List<TrackedBehaviorEntry>> =
            trackedBehaviorEntryRepository.findAllByTrackedBehaviorChatIdAndTrackedBehaviorDeletedAtNull(chat.id!!)
                .groupBy { trackedBehaviorEntry -> trackedBehaviorEntry.trackedBehavior!! }

        entriesByBehavior.filter { it.key.type == TrackingType.SIMPLE }
            .forEach { (behavior, entries) ->
                reportBuilder.appendLine(
                    messageSource.getMessage(
                        "report.simple-behavior-line",
                        arrayOf(behavior.name, entries.size, entries.count { isWithinLastMonth(it.timestamp!!) }),
                        Locale.getDefault()
                    )
                )
            }

        reportBuilder.appendLine()
            .appendLine(messageSource.getMessage("report.time-behaviors-prefix", null, Locale.getDefault()))

        entriesByBehavior.filter { it.key.type == TrackingType.TIME }
            .forEach { (behavior, entries) ->
                reportBuilder.appendLine(
                    messageSource.getMessage(
                        "report.time-behavior-line",
                        arrayOf(
                            behavior.name,
                            formatTime(calculateTotalSeconds(entries)),
                            formatTime(calculateTotalSeconds(entries.filter { isWithinLastMonth(it.timestamp!!) }))
                        ),
                        Locale.getDefault()
                    )
                )
            }

        return SendMessage("${chat.externalId}", reportBuilder.toString())
    }

    private fun calculateTotalSeconds(entries: Collection<TrackedBehaviorEntry>): Int {
        return entries.sumOf { it.durationSeconds!! }
    }

}
