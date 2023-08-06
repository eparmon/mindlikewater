package by.genie.mindlikewater.telegrambot.commandhandler

import by.genie.mindlikewater.persistence.domain.Chat
import by.genie.mindlikewater.persistence.domain.TrackedBehaviorEntry
import by.genie.mindlikewater.persistence.repository.ChatRepository
import by.genie.mindlikewater.persistence.repository.TrackedBehaviorEntryRepository
import by.genie.mindlikewater.persistence.repository.TrackedBehaviorRepository
import org.springframework.context.support.AbstractMessageSource
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import java.util.*

@Component
class LogBehaviorCommandHandler(
    private val chatRepository: ChatRepository,
    private val trackedBehaviorRepository: TrackedBehaviorRepository,
    private val trackedBehaviorEntryRepository: TrackedBehaviorEntryRepository,
    private val messageSource: AbstractMessageSource
) : CommandHandler {

    override fun command(): String {
        return "/log"
    }

    override fun handle(text: String, chat: Chat): BotApiMethodMessage {
        if (chat.activeCommand == null) {
            chat.activeCommand = command()
            chatRepository.save(chat)
            val sendMessage = SendMessage(
                "${chat.externalId}",
                messageSource.getMessage("log.which-behavior", null, Locale.getDefault())
            )
            val keyboard = ReplyKeyboardMarkup(
                trackedBehaviorRepository.findAllByChatId(chat.id!!)
                    .map { trackedBehavior -> KeyboardRow(listOf(KeyboardButton(trackedBehavior.name!!))) }
            )
            keyboard.oneTimeKeyboard = true
            sendMessage.replyMarkup = keyboard
            return sendMessage
        }
        chat.activeCommand = null
        chatRepository.save(chat)
        val trackedBehavior = trackedBehaviorRepository.findByChatIdAndName(chat.id!!, text)
        if (trackedBehavior.isEmpty) {
            return SendMessage(
                "${chat.externalId}",
                messageSource.getMessage("log.behavior-not-found", null, Locale.getDefault())
            )
        }
        trackedBehaviorEntryRepository.save(TrackedBehaviorEntry(trackedBehavior.get().id!!))
        return SendMessage(
            "${chat.externalId}",
            messageSource.getMessage("log.success", null, Locale.getDefault())
        )
    }

}
