package telegram;

import model.ServiceUser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import telegram.command.CommandContainer;
import telegram.service.SendBotMessageServiceImpl;

import java.util.HashMap;

import static telegram.command.CommandName.NO;

public final class Bot extends TelegramLongPollingBot {
    private final String BOT_TOKEN;
    private final String BOT_NAME;

    private static String COMMAND_PREFIX = "/";

    private HashMap<Long, ServiceUser> authorizedUsers;
    private HashMap<Long, Long> usersSettings;

    private final CommandContainer commandContainer;

    public Bot(String botName, String botToken) {
        super();
        this.BOT_NAME = botName;
        this.BOT_TOKEN = botToken;

        authorizedUsers = new HashMap<>();
        usersSettings = new HashMap<>();

        this.commandContainer = new CommandContainer(new SendBotMessageServiceImpl(this), authorizedUsers, usersSettings);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();
            if (message.startsWith(COMMAND_PREFIX)) {
                String commandIdentifier = message.split(" ")[0].toLowerCase();
                commandContainer.retrieveCommand(commandIdentifier).execute(update);
            } else {
                commandContainer.retrieveCommand(NO.getCommandName()).execute(update);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}