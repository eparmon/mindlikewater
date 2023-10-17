package by.genie.mindlikewater.persistence.repository

import by.genie.mindlikewater.persistence.domain.TrackedBehaviorEntry
import org.springframework.data.repository.CrudRepository
import java.time.ZonedDateTime

interface TrackedBehaviorEntryRepository : CrudRepository<TrackedBehaviorEntry, Int> {

    fun findAllByTrackedBehaviorChatIdAndTrackedBehaviorDeletedAtNull(chatId: Int): List<TrackedBehaviorEntry>

    fun findAllByTrackedBehaviorChatIdAndTimestampAfterAndDeletedAtNull(chatId: Int,
                                                                        from: ZonedDateTime): List<TrackedBehaviorEntry>

}
