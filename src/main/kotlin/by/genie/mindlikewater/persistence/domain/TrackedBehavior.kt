package by.genie.mindlikewater.persistence.domain

import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
@Table(name = "tracked_behaviors")
class TrackedBehavior() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    @Column(name = "chat_id")
    var chatId: Int? = null

    @Column
    var name: String? = null

    @Column(name = "deleted_at")
    var deletedAt: OffsetDateTime? = null

    constructor(chatId: Int, name: String) : this() {
        this.chatId = chatId
        this.name = name
    }

}
