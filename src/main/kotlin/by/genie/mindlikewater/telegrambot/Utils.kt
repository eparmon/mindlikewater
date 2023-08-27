package by.genie.mindlikewater.telegrambot

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow

fun sendMessageWithReplyKeyboard(chatExternalId: Long, text: String, keyboardRows: List<KeyboardRow>): SendMessage {
    val sendMessage = SendMessage("$chatExternalId", text)
    val keyboard = ReplyKeyboardMarkup(keyboardRows)
    keyboard.oneTimeKeyboard = true
    keyboard.resizeKeyboard = true
    sendMessage.replyMarkup = keyboard
    return sendMessage
}