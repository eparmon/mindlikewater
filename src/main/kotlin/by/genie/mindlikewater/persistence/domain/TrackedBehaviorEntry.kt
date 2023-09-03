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

    @Column(name = "duration_seconds")
    var durationSeconds: Int? = null

    constructor(trackedBehavior: TrackedBehavior, durationSeconds: Int) : this(trackedBehavior) {
        this.durationSeconds = durationSeconds
    }

    constructor(trackedBehavior: TrackedBehavior) : this() {
        this.trackedBehavior = trackedBehavior
        this.timestamp = OffsetDateTime.now()
    }

}
