package by.genie.mindlikewater.telegrambot

import by.genie.mindlikewater.logging.logger
import by.genie.mindlikewater.persistence.domain.Chat
import by.genie.mindlikewater.persistence.repository.ChatRepository
import by.genie.mindlikewater.telegrambot.commandhandler.CommandHandler
import org.springframework.context.support.AbstractMessageSource
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import java.util.*

class Bot(
    token: String,
    private val botUsername: String,
    private val handlers: List<CommandHandler>,
    private val chatRepository: ChatRepository,
    private val messageSource: AbstractMessageSource
) : TelegramLongPollingBot(token) {

    private val log = logger()

    override fun getBotUsername(): String {
        return botUsername
    }

    override fun onUpdateReceived(update: Update) {
        val (externalChatId, text) = if (update.message != null) {
            log.info("Received message from chat ${update.message.chatId}: ${update.message.text}")
            Pair(update.message.chatId, update.message.text)
        } else {
            log.info(
                "Received callbackQuery from chat ${update.callbackQuery.message.chatId} with data " +
                        update.callbackQuery.data
            )
            Pair(update.callbackQuery.message.chatId, update.callbackQuery.data)
        }
        val chat = chatRepository.findByExternalId(externalChatId)
            .orElseGet { chatRepository.save(Chat(externalChatId)) }
        val command = chat.activeCommand ?: text
        val commandHandler = findHandler(command)
        val message = if (commandHandler != null) {
            commandHandler.handle(text, chat)
        } else {
            log.warning("Could not find handler for command $command")
            SendMessage(
                "$externalChatId",
                messageSource.getMessage("error.unknown-command", null, Locale.getDefault())
            )
        }
        execute(message)
    }

    private fun findHandler(command: String): CommandHandler? {
        return handlers.find { handler -> handler.command() == command }
    }

}
