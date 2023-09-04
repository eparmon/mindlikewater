package by.genie.mindlikewater.telegrambot.commandhandler.trackingbehavior

import by.genie.mindlikewater.helper.ChatHelper
import by.genie.mindlikewater.helper.MessageHelper
import by.genie.mindlikewater.persistence.domain.Chat
import by.genie.mindlikewater.persistence.repository.TrackedBehaviorRepository
import by.genie.mindlikewater.telegrambot.commandhandler.CommandHandler
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import java.time.OffsetDateTime

@Component
class StopTrackingBehaviorCommandHandler(
    private val chatHelper: ChatHelper,
    private val trackedBehaviorRepository: TrackedBehaviorRepository,
    private val messageHelper: MessageHelper,
) : CommandHandler {

    override fun command(): String {
        return "/stop_tracking"
    }

    override fun handle(text: String, chat: Chat): BotApiMethodMessage {
        if (chat.activeCommand == null) {
            chatHelper.updateActiveCommand(chat, command())
            val keyboardRows = trackedBehaviorRepository.findAllByChatIdAndDeletedAtNull(chat.id!!)
                .map { trackedBehavior -> KeyboardRow(listOf(KeyboardButton(trackedBehavior.name!!))) }
            return messageHelper.buildSendMessage(chat, "stop-tracking.which-behavior", keyboardRows)
        }
        chatHelper.resetActiveCommandAndContext(chat)
        val trackedBehavior = trackedBehaviorRepository.findByChatIdAndNameAndDeletedAtNull(chat.id!!, text)
            ?: return messageHelper.buildSendMessage(chat, "error.behavior-not-found")
        trackedBehavior.deletedAt = OffsetDateTime.now()
        trackedBehaviorRepository.save(trackedBehavior)
        return messageHelper.buildSendMessage(chat, "stop-tracking.success", arrayOf(text))
    }

}
