package by.genie.mindlikewater.telegrambot.commandhandler

import by.genie.mindlikewater.helper.ChatHelper
import by.genie.mindlikewater.helper.MessageHelper
import by.genie.mindlikewater.persistence.domain.Chat
import by.genie.mindlikewater.persistence.domain.TrackedBehavior
import by.genie.mindlikewater.persistence.repository.TrackedBehaviorRepository
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage

@Component
class StartTrackingBehaviorCommandHandler(
    private val chatHelper: ChatHelper,
    private val trackedBehaviorRepository: TrackedBehaviorRepository,
    private val messageHelper: MessageHelper,
) : CommandHandler {

    override fun command(): String {
        return "/track"
    }

    override fun handle(text: String, chat: Chat): BotApiMethodMessage {
        if (chat.activeCommand == null) {
            chatHelper.updateActiveCommand(chat, command())
            return messageHelper.buildSendMessage(chat, "track.enter-name")
        }
        trackedBehaviorRepository.save(TrackedBehavior(chat.id!!, text))
        chatHelper.resetActiveCommand(chat)
        return messageHelper.buildSendMessage(chat, "track.success")
    }

}
