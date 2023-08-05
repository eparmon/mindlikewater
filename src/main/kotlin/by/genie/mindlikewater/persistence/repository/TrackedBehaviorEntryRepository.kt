package by.genie.mindlikewater.persistence.repository

import by.genie.mindlikewater.persistence.domain.TrackedBehaviorEntry
import org.springframework.data.repository.CrudRepository

interface TrackedBehaviorEntryRepository : CrudRepository<TrackedBehaviorEntry, Int>
