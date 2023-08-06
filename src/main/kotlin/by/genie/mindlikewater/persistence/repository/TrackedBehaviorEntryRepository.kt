package by.genie.mindlikewater.persistence.repository

import by.genie.mindlikewater.persistence.domain.TrackedBehaviorEntry
import org.springframework.data.repository.CrudRepository
import java.time.OffsetDateTime

interface TrackedBehaviorEntryRepository : CrudRepository<TrackedBehaviorEntry, Int> {

    fun findAllByTrackedBehaviorChatIdAndTimestampAfter(chatId: Int, from: OffsetDateTime): List<TrackedBehaviorEntry>

}
