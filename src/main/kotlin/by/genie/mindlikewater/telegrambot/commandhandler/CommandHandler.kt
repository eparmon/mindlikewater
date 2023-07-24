package by.genie.mindlikewater.telegrambot.commandhandler

import by.genie.mindlikewater.persistence.domain.Chat
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage

interface CommandHandler {

    fun command(): String

    fun handle(text: String, chat: Chat): BotApiMethodMessage

}
