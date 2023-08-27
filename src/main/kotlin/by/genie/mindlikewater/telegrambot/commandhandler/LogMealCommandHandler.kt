package by.genie.mindlikewater.telegrambot.commandhandler

import by.genie.mindlikewater.persistence.domain.Chat
import by.genie.mindlikewater.persistence.domain.Meal
import by.genie.mindlikewater.persistence.repository.ChatRepository
import by.genie.mindlikewater.persistence.repository.FoodRepository
import by.genie.mindlikewater.persistence.repository.MealRepository
import by.genie.mindlikewater.telegrambot.sendMessageWithReplyKeyboard
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.support.AbstractMessageSource
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import java.lang.Integer.parseInt
import java.util.*

@Component
class LogMealCommandHandler(
    private val chatRepository: ChatRepository,
    private val foodRepository: FoodRepository,
    private val mealRepository: MealRepository,
    private val messageSource: AbstractMessageSource,
) : CommandHandler {

    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    override fun command(): String {
        return "/meal"
    }

    override fun handle(text: String, chat: Chat): BotApiMethodMessage {
        if (chat.context == null) {
            return askWhichFood(chat)
        }
        val context = objectMapper.readValue(chat.context, Context::class.java)
        return when (context.status) {
            Status.ASKED_WHICH_FOOD -> {
                saveFoodAndAskForQuantity(text, chat)
            }
            Status.ASKED_FOR_QUANTITY -> {
                saveMeal(text, chat)
            }
        }
    }

    private fun askWhichFood(chat: Chat): SendMessage {
        chat.context = objectMapper.writeValueAsString(Context(Status.ASKED_WHICH_FOOD, null))
        chat.activeCommand = command()
        chatRepository.save(chat)
        return sendMessageWithReplyKeyboard(chat.externalId!!,
            messageSource.getMessage("meal.which-food", null, Locale.getDefault()),
            foodRepository.findAllByOrderByName().chunked(3)
                .map { foodsChunk -> KeyboardRow(foodsChunk.map { food -> KeyboardButton(food.name!!) }) })
    }

    private fun saveFoodAndAskForQuantity(foodName: String, chat: Chat): SendMessage {
        val food = foodRepository.findByName(foodName)
        if (food == null) {
            chat.context = null
            chat.activeCommand = null
            chatRepository.save(chat)
            return SendMessage(
                "${chat.externalId}",
                messageSource.getMessage("meal.food-not-found", null, Locale.getDefault())
            )
        }
        chat.context = objectMapper.writeValueAsString(Context(Status.ASKED_FOR_QUANTITY, food.id!!))
        chatRepository.save(chat)
        return SendMessage(
            "${chat.externalId}",
            messageSource.getMessage("meal.how-much", null, Locale.getDefault())
        )
    }

    private fun saveMeal(foodAmount: String, chat: Chat): SendMessage {
        val amount = try {
            parseInt(foodAmount)
        } catch (e: NumberFormatException) {
            return SendMessage(
                "${chat.externalId}",
                messageSource.getMessage("error.not-a-decimal-number", null, Locale.getDefault())
            )
        }
        val context = objectMapper.readValue(chat.context, Context::class.java)
        mealRepository.save(Meal(chat.id!!, context.foodId!!, amount))
        chat.context = null
        chat.activeCommand = null
        chatRepository.save(chat)
        return SendMessage(
            "${chat.externalId}",
            messageSource.getMessage("meal.success", null, Locale.getDefault())
        )
    }

    private enum class Status {
        ASKED_WHICH_FOOD, ASKED_FOR_QUANTITY
    }

    private data class Context(
        val status: Status,
        val foodId: Int?
    )

}
