package by.genie.mindlikewater.persistence.domain

import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
@Table(name = "tracked_behavior_entries")
class TrackedBehaviorEntry() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    @Column(name = "tracked_behavior_id")
    var trackedBehaviorId: Int? = null

    @Column
    var timestamp: OffsetDateTime? = null

    constructor(trackedBehaviorId: Int) : this() {
        this.trackedBehaviorId = trackedBehaviorId
        this.timestamp = OffsetDateTime.now()
    }

}
