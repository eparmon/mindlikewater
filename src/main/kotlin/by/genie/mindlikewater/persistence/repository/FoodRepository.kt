package by.genie.mindlikewater.persistence.repository

import by.genie.mindlikewater.persistence.domain.Food
import org.springframework.data.repository.Repository

interface FoodRepository : Repository<Food, Int> {

    fun findByName(name: String): Food?

    fun findAllByOrderByName(): List<Food>

}
