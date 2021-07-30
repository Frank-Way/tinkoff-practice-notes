package telegram.command;

import exception.ResourceNotFoundException;
import model.ServiceUser;
import org.telegram.telegrambots.meta.api.objects.Message;
import service.ApiService;
import telegram.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static telegram.command.Messages.*;
import static telegram.command.Patterns.SIGN_UP;

import java.io.IOException;

/**
 * Sign up {@link Command}.
 */
public class SignUpCommand implements Command {

    private final SendBotMessageService sendBotMessageService;

    public SignUpCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        Message message = update.getMessage();
        String text = message.getText();
        if (SIGN_UP.getPattern().matcher(text).matches()) {
            String[] words = text.split(" ");
            if (words[2].equals(words[3])) {
                try {
                    ServiceUser user = ApiService.getUserByLogin(words[1]);
                    if (user.getLogin().equals(words[1]))
                        sendBotMessageService.sendMessage(message, USER_EXISTS_MESSAGE.getMessageText());
                } catch (ResourceNotFoundException e) {
                    ApiService.createUser(new ServiceUser(words[1], words[2]));
                    sendBotMessageService.sendMessage(message, SIGNED_UP_MESSAGE.getMessageText());
                }
            } else
                sendBotMessageService.sendMessage(message, PASSWORDS_MISMATCHING_MESSAGE.getMessageText());
        } else
            sendBotMessageService.sendMessage(message, WRONG_FORMAT_MESSAGE.getMessageText());
    }
}