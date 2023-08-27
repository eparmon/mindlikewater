package by.genie.mindlikewater.persistence.repository

import by.genie.mindlikewater.persistence.domain.Meal
import org.springframework.data.repository.CrudRepository

interface MealRepository : CrudRepository<Meal, Int> {
}