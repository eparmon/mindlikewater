package by.genie.mindlikewater.leagueoflegends

import by.genie.mindlikewater.leagueoflegends.dto.SummonerBasicRequestDto
import by.genie.mindlikewater.leagueoflegends.dto.SummonerBasicResponseDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

@Component
class LeagueStatsClient {

    @Value("league-stats.base-url")
    private val leagueStatsBaseUrl = null

    fun getDailyStats(args: Array<String>): DailyStats {
        val summonerBasicResponseDto = RestTemplate().postForObject(
            "https://api.leaguestats.gg/summoner/basic",
            SummonerBasicRequestDto("ru", "BronzeBrain"),
            SummonerBasicResponseDto::class.java
        )!!

        val activityToday =
            summonerBasicResponseDto.recentActivity.firstOrNull { dto -> dto.day == getFormattedDateOfToday() }

        return DailyStats(activityToday?.wins ?: 0, activityToday?.losses ?: 0)
    }

    private fun getFormattedDateOfToday(): String {
        return LocalDate.now().atStartOfDay().atZone(ZoneOffset.UTC).format(DATE_TIME_FORMATTER)
    }

}
