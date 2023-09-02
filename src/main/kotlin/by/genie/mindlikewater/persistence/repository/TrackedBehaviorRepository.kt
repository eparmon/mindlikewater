package by.genie.mindlikewater.persistence.repository

import by.genie.mindlikewater.persistence.domain.TrackedBehavior
import org.springframework.data.repository.CrudRepository

interface TrackedBehaviorRepository : CrudRepository<TrackedBehavior, Int> {

    fun findByChatIdAndNameAndDeletedAtNull(chatId: Int, name: String): TrackedBehavior?

    fun findAllByChatIdAndDeletedAtNull(chatId: Int): List<TrackedBehavior>

}
