package ru.bez_createha.queue_bot.controller;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.view.CallbackCommand;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CallbackInvoker {

    private final List<CallbackCommand> commands;

    public CallbackInvoker() {
        this.commands = new ArrayList<>();
    }
    public void register(CallbackCommand command){
        commands.add(command);
    }

    public void process(CallbackQuery callbackQuery, User user, Bot bot) throws TelegramApiException {
        for (CallbackCommand command : commands) {
            if(command.statePredicate().test(user.getBotState()) && command.callbackPredicate().test(callbackQuery)){
                command.process(callbackQuery, user, bot);
                break;
            }
        }
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
        bot.execute(answerCallbackQuery);
    }
}
