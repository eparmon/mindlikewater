package by.genie.mindlikewater.telegrambot.commandhandler

import by.genie.mindlikewater.persistence.domain.Chat
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import java.util.*

@Component
class StartCommandHandler(private val messageSource: MessageSource) : CommandHandler {

    override fun command(): String {
        return "/start"
    }

    override fun handle(text: String, chat: Chat): SendMessage {
        return SendMessage("${chat.externalId}",
            messageSource.getMessage("start.welcome", null, Locale.getDefault()))
    }

}
