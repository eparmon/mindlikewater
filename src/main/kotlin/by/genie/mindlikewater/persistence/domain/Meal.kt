package by.genie.mindlikewater.persistence.domain

import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
@Table(name = "meals")
class Meal() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    @Column(name = "chat_id")
    var chatId: Int? = null

    @Column(name = "food_id")
    var foodId: Int? = null

    @Column
    var amount: Int? = null

    @Column
    var timestamp: OffsetDateTime? = null

    constructor(chatId: Int, foodId: Int, amount: Int): this() {
        this.chatId = chatId
        this.foodId = foodId
        this.amount = amount
        timestamp = OffsetDateTime.now()
    }

}
