package by.genie.mindlikewater.telegrambot.commandhandler

import by.genie.mindlikewater.persistence.domain.Chat
import by.genie.mindlikewater.persistence.repository.TrackedBehaviorEntryRepository
import org.springframework.context.support.AbstractMessageSource
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import java.time.OffsetDateTime
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
        val report = trackedBehaviorEntryRepository
            .findAllByTrackedBehaviorChatIdAndTimestampAfterAndTrackedBehaviorDeletedAtNull(
                chat.id!!,
                OffsetDateTime.now().minusDays(30)
            )
            .groupBy { trackedBehaviorEntry -> trackedBehaviorEntry.trackedBehavior!!.name }
            .map { entry -> "${entry.key}: ${entry.value.size} times" }
            .joinToString(
                prefix = messageSource.getMessage("report.prefix", null, Locale.getDefault()) + "\n\n",
                separator = "\n"
            )
        return SendMessage("${chat.externalId}", report)
    }

}
