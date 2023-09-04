package by.genie.mindlikewater.telegrambot.commandhandler.tasks

import by.genie.mindlikewater.persistence.domain.Chat
import by.genie.mindlikewater.persistence.repository.TaskRepository
import by.genie.mindlikewater.telegrambot.commandhandler.CommandHandler
import org.springframework.context.support.AbstractMessageSource
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import java.util.*

@Component
class ListTasksCommandHandler(
    private val taskRepository: TaskRepository,
    private val messageSource: AbstractMessageSource,
) : CommandHandler {

    override fun command(): String {
        return "/list_tasks"
    }

    override fun handle(text: String, chat: Chat): BotApiMethodMessage {
        return SendMessage(
            "${chat.externalId}",
            taskRepository.findAllByChatIdAndDoneAtNull(chat.id!!)
                .map { it.name }
                .joinToString(separator = "\n")
                .ifEmpty { messageSource.getMessage("tasks.nothing-to-do", null, Locale.getDefault()) }
        )
    }

}
