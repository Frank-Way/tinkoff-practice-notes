package telegram.command;

import exception.ResourceNotFoundException;
import model.ServiceUser;
import org.telegram.telegrambots.meta.api.objects.Message;
import service.ApiService;
import telegram.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static telegram.command.Messages.*;
import static telegram.command.Patterns.SIGN_IN;

import java.util.HashMap;

/**
 * Sign in {@link Command}.
 */
public class SignInCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final HashMap<Long, ServiceUser> authorizedUsers;
    private final HashMap<Long, Long> usersSettings;

    public SignInCommand(SendBotMessageService sendBotMessageService,
                         HashMap<Long, ServiceUser> authorizedUsers,
                         HashMap<Long, Long> usersSettings) {
        this.sendBotMessageService = sendBotMessageService;
        this.authorizedUsers = authorizedUsers;
        this.usersSettings = usersSettings;
    }

    @Override
    public void execute(Update update) {
        Message message = update.getMessage();
        String text = message.getText();
        if (SIGN_IN.getPattern().matcher(text).matches()) {
            String[] words = text.split(" ");
            try {
                ServiceUser user = ApiService.getUserByLogin(words[1]);
                if (user.getPassword().equals(words[2])) {
                    sendBotMessageService.sendMessage(message, SIGNED_IN_MESSAGE.getMessageText());
                    authorizedUsers.put(message.getChatId(), user);
                    usersSettings.put(message.getChatId(), 5L);
                } else
                    sendBotMessageService.sendMessage(message, WRONG_PASSWORD_MESSAGE.getMessageText());
            } catch (ResourceNotFoundException e) {
                e.printStackTrace();
                sendBotMessageService.sendMessage(message, USER_NOT_FOUND_MESSAGE.getMessageText());
            }
        } else
            sendBotMessageService.sendMessage(message, WRONG_FORMAT_MESSAGE.getMessageText());

    }
}