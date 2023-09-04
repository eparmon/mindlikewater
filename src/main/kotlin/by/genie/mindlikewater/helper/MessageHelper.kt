package by.genie.mindlikewater.helper

import by.genie.mindlikewater.persistence.domain.Chat
import org.springframework.context.support.AbstractMessageSource
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import java.util.*

private val replyKeyboardRemove = ReplyKeyboardRemove(true)

@Component
class MessageHelper(private val messageSource: AbstractMessageSource) {

    fun buildSendMessage(chat: Chat,
                         messageCode: String,
                         keyboardRows: List<KeyboardRow>): SendMessage {
        return buildSendMessage(chat, messageCode, null, keyboardRows)
    }

    fun buildSendMessage(chat: Chat,
                         messageCode: String,
                         messageArgs: Array<String>? = null,
                         keyboardRows: List<KeyboardRow>? = null): SendMessage {
        val sendMessage = SendMessage(
            "${chat.externalId}",
            messageSource.getMessage(messageCode, messageArgs, Locale.getDefault())
        )
        sendMessage.replyMarkup = when (keyboardRows) {
            null -> replyKeyboardRemove
            else -> buildOneTimeKeyboard(keyboardRows)
        }
        return sendMessage
    }

    private fun buildOneTimeKeyboard(keyboardRows: List<KeyboardRow>): ReplyKeyboard {
        val keyboard = ReplyKeyboardMarkup(keyboardRows)
        keyboard.oneTimeKeyboard = true
        keyboard.resizeKeyboard = true
        return keyboard
    }

}
