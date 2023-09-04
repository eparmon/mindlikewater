package by.genie.mindlikewater.telegrambot.commandhandler.tasks

import by.genie.mindlikewater.helper.ChatHelper
import by.genie.mindlikewater.helper.MessageHelper
import by.genie.mindlikewater.persistence.domain.Chat
import by.genie.mindlikewater.persistence.repository.TaskRepository
import by.genie.mindlikewater.telegrambot.commandhandler.CommandHandler
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import java.time.OffsetDateTime

@Component
class MarkTaskAsDoneCommandHandler(
    private val taskRepository: TaskRepository,
    private val chatHelper: ChatHelper,
    private val messageHelper: MessageHelper,
) : CommandHandler {

    override fun command(): String {
        return "/done"
    }

    override fun handle(text: String, chat: Chat): BotApiMethodMessage {
        if (chat.activeCommand == null) {
            val keyboardRows = taskRepository.findAllByChatIdAndDoneAtNull(chat.id!!)
                .map { task -> KeyboardRow(listOf(KeyboardButton(task.name!!))) }
            if (keyboardRows.isEmpty()) {
                return messageHelper.buildSendMessage(chat, "done.nothing-to-do")
            }
            chatHelper.updateActiveCommand(chat, command())
            return messageHelper.buildSendMessage(chat, "done.select-task", keyboardRows)
        }
        chatHelper.resetActiveCommandAndContext(chat)
        val task = taskRepository.findByChatIdAndNameAndDoneAtNull(chat.id!!, text)
            ?: return messageHelper.buildSendMessage(chat, "error.task-not-found")
        task.doneAt = OffsetDateTime.now()
        taskRepository.save(task)
        return messageHelper.buildSendMessage(chat, "done.success")
    }

}
