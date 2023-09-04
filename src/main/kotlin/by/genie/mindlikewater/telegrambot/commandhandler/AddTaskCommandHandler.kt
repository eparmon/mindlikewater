package by.genie.mindlikewater.telegrambot.commandhandler

import by.genie.mindlikewater.helper.ChatHelper
import by.genie.mindlikewater.helper.MessageHelper
import by.genie.mindlikewater.persistence.domain.Chat
import by.genie.mindlikewater.persistence.domain.Task
import by.genie.mindlikewater.persistence.repository.TaskRepository
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage

@Component
class AddTaskCommandHandler(
    private val taskRepository: TaskRepository,
    private val chatHelper: ChatHelper,
    private val messageHelper: MessageHelper,
) : CommandHandler {

    override fun command(): String {
        return "/add_task"
    }

    override fun handle(text: String, chat: Chat): BotApiMethodMessage {
        if (chat.activeCommand == null) {
            chatHelper.updateActiveCommand(chat, command())
            return messageHelper.buildSendMessage(chat, "add-task.what-is-to-be-done")
        }
        chatHelper.resetActiveCommandAndContext(chat)
        taskRepository.save(Task(chat.id!!, text))
        return messageHelper.buildSendMessage(chat, "add-task.success")
    }

}
