package by.genie.mindlikewater.persistence.repository

import by.genie.mindlikewater.persistence.domain.Task
import org.springframework.data.repository.CrudRepository

interface TaskRepository : CrudRepository<Task, Int> {

    fun findAllByChatIdAndDoneAtNull(chatId: Int): List<Task>

    fun findByChatIdAndNameAndDoneAtNull(chatId: Int, name: String): Task?

}
