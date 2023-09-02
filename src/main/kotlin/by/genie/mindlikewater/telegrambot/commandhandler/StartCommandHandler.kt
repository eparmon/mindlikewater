package by.genie.mindlikewater.telegrambot.commandhandler

import by.genie.mindlikewater.helper.MessageHelper
import by.genie.mindlikewater.persistence.domain.Chat
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

@Component
class StartCommandHandler(private val messageHelper: MessageHelper) : CommandHandler {

    override fun command(): String {
        return "/start"
    }

    override fun handle(text: String, chat: Chat): SendMessage {
        return messageHelper.buildSendMessage(chat, "start.welcome")
    }

}
