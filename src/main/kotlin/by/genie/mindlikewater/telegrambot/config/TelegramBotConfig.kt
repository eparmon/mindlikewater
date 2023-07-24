package by.genie.mindlikewater.telegrambot.config

import by.genie.mindlikewater.persistence.repository.ChatRepository
import by.genie.mindlikewater.telegrambot.Bot
import by.genie.mindlikewater.telegrambot.commandhandler.CommandHandler
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.AbstractMessageSource
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

@Configuration
class TelegramBotConfig(
    private val handlers: List<CommandHandler>,
    private val chatRepository: ChatRepository,
    private val messageSource: AbstractMessageSource
) {

    @Value("\${app.bot.token}")
    private val token = ""

    @Value("\${app.bot.username}")
    private val botUsername = ""

    @Bean
    fun api(): TelegramBotsApi? {
        val telegramBotsApi = TelegramBotsApi(DefaultBotSession::class.java)
        telegramBotsApi.registerBot(Bot(token, botUsername, handlers, chatRepository, messageSource))
        return telegramBotsApi
    }

}
