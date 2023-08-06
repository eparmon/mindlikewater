package by.genie.mindlikewater.persistence.domain

import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
@Table(name = "tracked_behavior_entries")
class TrackedBehaviorEntry() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    @ManyToOne
    @JoinColumn(name = "tracked_behavior_id")
    var trackedBehavior: TrackedBehavior? = null

    @Column
    var timestamp: OffsetDateTime? = null

    constructor(trackedBehavior: TrackedBehavior) : this() {
        this.trackedBehavior = trackedBehavior
        this.timestamp = OffsetDateTime.now()
    }

}
