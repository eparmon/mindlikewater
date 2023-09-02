package by.genie.mindlikewater.persistence.domain

import by.genie.mindlikewater.persistence.domain.enums.TrackingType
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

    @Column
    @Enumerated(EnumType.STRING)
    var type: TrackingType? = null

    constructor(chatId: Int, name: String, type: TrackingType) : this() {
        this.chatId = chatId
        this.name = name
        this.type = type
    }

}
