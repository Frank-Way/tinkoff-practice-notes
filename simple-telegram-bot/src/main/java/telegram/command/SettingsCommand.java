package telegram.command;

import org.telegram.telegrambots.meta.api.objects.Message;
import telegram.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static telegram.command.Messages.*;
import static telegram.command.Patterns.SETTINGS;

import java.util.HashMap;

/**
 * Settings {@link Command}.
 */
public class SettingsCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final HashMap<Long, Long> usersSettings;

    public SettingsCommand(SendBotMessageService sendBotMessageService,
                           HashMap<Long, Long> usersSettings) {
        this.sendBotMessageService = sendBotMessageService;
        this.usersSettings = usersSettings;
    }

    @Override
    public void execute(Update update) {
        Message message = update.getMessage();
        String text = message.getText();
        if (SETTINGS.getPattern().matcher(text).matches()) {
            String[] words = text.split(" ");
            try {
                usersSettings.put(message.getChatId(), Long.decode(words[1]));
                sendBotMessageService.sendMessage(message,
                        SETTINGS_MESSAGE.getMessageText() + Long.decode(words[1]));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                sendBotMessageService.sendMessage(message, FAIL_CONVERSION_MESSAGE.getMessageText());
            }
        } else
            sendBotMessageService.sendMessage(message, WRONG_FORMAT_MESSAGE.getMessageText());

    }
}