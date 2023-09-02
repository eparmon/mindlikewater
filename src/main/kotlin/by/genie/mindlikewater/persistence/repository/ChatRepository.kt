package by.genie.mindlikewater.persistence.repository

import by.genie.mindlikewater.persistence.domain.Chat
import org.springframework.data.repository.CrudRepository

interface ChatRepository : CrudRepository<Chat, Int> {

    fun findByExternalId(externalId: Long): Chat?

}
