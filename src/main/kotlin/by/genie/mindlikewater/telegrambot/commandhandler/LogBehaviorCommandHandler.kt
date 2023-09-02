package by.genie.mindlikewater.telegrambot.commandhandler

import by.genie.mindlikewater.helper.ChatHelper
import by.genie.mindlikewater.helper.MessageHelper
import by.genie.mindlikewater.persistence.domain.Chat
import by.genie.mindlikewater.persistence.domain.TrackedBehaviorEntry
import by.genie.mindlikewater.persistence.repository.TrackedBehaviorEntryRepository
import by.genie.mindlikewater.persistence.repository.TrackedBehaviorRepository
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow

@Component
class LogBehaviorCommandHandler(
    private val chatHelper: ChatHelper,
    private val trackedBehaviorRepository: TrackedBehaviorRepository,
    private val trackedBehaviorEntryRepository: TrackedBehaviorEntryRepository,
    private val messageHelper: MessageHelper,
) : CommandHandler {

    override fun command(): String {
        return "/log"
    }

    override fun handle(text: String, chat: Chat): BotApiMethodMessage {
        if (chat.activeCommand == null) {
            chatHelper.updateActiveCommand(chat, command())
            val keyboardRows = trackedBehaviorRepository.findAllByChatIdAndDeletedAtNull(chat.id!!)
                .map { trackedBehavior -> KeyboardRow(listOf(KeyboardButton(trackedBehavior.name!!))) }
            return messageHelper.buildSendMessage(chat, "log.which-behavior", keyboardRows)
        }
        chatHelper.resetActiveCommandAndContext(chat)
        val trackedBehavior = trackedBehaviorRepository.findByChatIdAndNameAndDeletedAtNull(chat.id!!, text)
            ?: return messageHelper.buildSendMessage(chat, "error.behavior-not-found")
        trackedBehaviorEntryRepository.save(TrackedBehaviorEntry(trackedBehavior))
        return messageHelper.buildSendMessage(chat, "log.success")
    }

}
