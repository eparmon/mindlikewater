package by.genie.mindlikewater.persistence.domain.enums

enum class TrackingType {

    SIMPLE, TIME;

    fun capitalize(): String {
        return name.lowercase().replaceFirstChar(Char::uppercaseChar)
    }

}
