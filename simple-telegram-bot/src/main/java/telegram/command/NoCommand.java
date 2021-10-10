package telegram.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import telegram.service.SendBotMessageService;

import static telegram.command.Messages.NO_MESSAGE;

/**
 * No {@link Command}.
 */
public class NoCommand implements Command {
    private final SendBotMessageService sendBotMessageService;

    public NoCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage(), NO_MESSAGE.getMessageText());
    }
}
