package by.genie.mindlikewater.persistence.repository

import by.genie.mindlikewater.persistence.domain.TrackedBehavior
import org.springframework.data.repository.CrudRepository

interface TrackedBehaviorRepository : CrudRepository<TrackedBehavior, Int> {

    fun findByChatIdAndName(chatId: Int, name: String): TrackedBehavior?

    fun findAllByChatId(chatId: Int): List<TrackedBehavior>

}
