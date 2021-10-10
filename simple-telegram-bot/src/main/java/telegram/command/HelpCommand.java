package telegram.command;

import telegram.service.SendBotMessageService;

import static telegram.command.Messages.*;
import static telegram.command.Patterns.HELP;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Help {@link Command}.
 */
public class HelpCommand implements Command {

    private final SendBotMessageService sendBotMessageService;

    public HelpCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        if (HELP.getPattern().matcher(update.getMessage().getText()).matches())
            sendBotMessageService.sendMessage(update.getMessage(), HELP_MESSAGE.getMessageText());
        else
            sendBotMessageService.sendMessage(update.getMessage(), WRONG_FORMAT_MESSAGE.getMessageText());
    }
}
