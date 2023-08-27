package by.genie.mindlikewater.telegrambot.commandhandler

import by.genie.mindlikewater.persistence.domain.Chat
import by.genie.mindlikewater.persistence.domain.TrackedBehavior
import by.genie.mindlikewater.persistence.repository.ChatRepository
import by.genie.mindlikewater.persistence.repository.TrackedBehaviorRepository
import org.springframework.context.support.AbstractMessageSource
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import java.util.*

@Component
class StartTrackingBehaviorCommandHandler(
    private val chatRepository: ChatRepository,
    private val trackedBehaviorRepository: TrackedBehaviorRepository,
    private val messageSource: AbstractMessageSource
) : CommandHandler {

    override fun command(): String {
        return "/track"
    }

    override fun handle(text: String, chat: Chat): BotApiMethodMessage {
        if (chat.activeCommand == null) {
            chat.activeCommand = command()
            chatRepository.save(chat)
            return SendMessage(
                "${chat.externalId}",
                messageSource.getMessage("track.enter-name", null, Locale.getDefault())
            )
        }
        trackedBehaviorRepository.save(TrackedBehavior(chat.id!!, text))
        chat.activeCommand = null
        chatRepository.save(chat)
        return SendMessage(
            "${chat.externalId}",
            messageSource.getMessage("track.success", null, Locale.getDefault())
        )
    }

}
