package by.genie.mindlikewater.helper

import by.genie.mindlikewater.persistence.domain.Chat
import by.genie.mindlikewater.persistence.repository.ChatRepository
import org.springframework.stereotype.Component

@Component
class ChatHelper(private val chatRepository: ChatRepository) {

    fun resetActiveCommand(chat: Chat) {
        updateActiveCommand(chat, null)
    }

    fun updateActiveCommand(chat: Chat, newActiveCommand: String?) {
        chat.activeCommand = newActiveCommand
        chatRepository.save(chat)
    }

}
