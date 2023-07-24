package by.genie.mindlikewater.persistence.repository

import by.genie.mindlikewater.persistence.domain.Chat
import org.springframework.data.repository.CrudRepository
import java.util.*

interface ChatRepository : CrudRepository<Chat, Int> {

    fun findByExternalId(externalId: Long): Optional<Chat>

}
