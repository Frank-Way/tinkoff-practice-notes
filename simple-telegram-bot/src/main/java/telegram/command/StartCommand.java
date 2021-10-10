package telegram.command;

import telegram.service.SendBotMessageService;

import static telegram.command.Messages.*;

import static telegram.command.Patterns.START;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Start {@link Command}.
 */
public class StartCommand implements Command {

    private final SendBotMessageService sendBotMessageService;

    public StartCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        if (START.getPattern().matcher(update.getMessage().getText()).matches())
            sendBotMessageService.sendMessage(update.getMessage(), START_MESSAGE.getMessageText());
        else
            sendBotMessageService.sendMessage(update.getMessage(), WRONG_FORMAT_MESSAGE.getMessageText());
    }
}
