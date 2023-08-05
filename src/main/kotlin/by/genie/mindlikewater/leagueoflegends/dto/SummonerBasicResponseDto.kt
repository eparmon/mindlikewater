package by.genie.mindlikewater.leagueoflegends.dto

data class SummonerBasicResponseDto(
    val recentActivity: List<RecentActivityDto>
)

data class RecentActivityDto(
    val day: String,
    val time: Int,
    val wins: Int,
    val losses: Int
)