package by.genie.mindlikewater.helper

import by.genie.mindlikewater.persistence.domain.Chat
import by.genie.mindlikewater.persistence.repository.ChatRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Component
class ChatHelper(
    private val chatRepository: ChatRepository,
    private val objectMapper: ObjectMapper,
) {

    fun resetActiveCommandAndContext(chat: Chat) {
        updateActiveCommandAndContext(chat, null, null)
    }

    fun updateActiveCommand(chat: Chat, newActiveCommand: String?) {
        chat.activeCommand = newActiveCommand
        chatRepository.save(chat)
    }

    fun updateActiveCommandAndContext(chat: Chat, newActiveCommand: String?, newContext: Any?) {
        chat.activeCommand = newActiveCommand
        updateContext(chat, newContext)
    }

    fun updateContext(chat: Chat, newContext: Any?) {
        chat.context = when (newContext) {
            null -> null
            else -> objectMapper.writeValueAsString(newContext)
        }
        chatRepository.save(chat)
    }

}
