package telegram.command;

import model.ServiceUser;
import org.telegram.telegrambots.meta.api.objects.Message;
import telegram.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static telegram.command.Messages.*;
import static telegram.command.Patterns.SIGN_OUT;

import java.util.HashMap;

/**
 * Sign out {@link Command}.
 */
public class SignOutCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final HashMap<Long, ServiceUser> authorizedUsers;
    private final HashMap<Long, Long> usersSettings;

    public SignOutCommand(SendBotMessageService sendBotMessageService,
                          HashMap<Long, ServiceUser> authorizedUsers,
                          HashMap<Long, Long> usersSettings) {
        this.sendBotMessageService = sendBotMessageService;
        this.authorizedUsers = authorizedUsers;
        this.usersSettings = usersSettings;
    }

    @Override
    public void execute(Update update) {
        Message message = update.getMessage();
        if (SIGN_OUT.getPattern().matcher(message.getText()).matches()) {
            authorizedUsers.remove(message.getChatId());
            usersSettings.remove(message.getChatId());
            sendBotMessageService.sendMessage(message, SIGNED_OUT_MESSAGE.getMessageText());
        } else
            sendBotMessageService.sendMessage(message, WRONG_FORMAT_MESSAGE.getMessageText());
    }
}