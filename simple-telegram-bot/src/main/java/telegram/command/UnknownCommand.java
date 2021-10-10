package telegram.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import telegram.service.SendBotMessageService;

import static telegram.command.Messages.UNKNOWN_MESSAGE;

/**
 * Unknown {@link Command}.
 */
public class UnknownCommand implements Command{
    private final SendBotMessageService sendBotMessageService;

    public UnknownCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage(), UNKNOWN_MESSAGE.getMessageText());
    }

}
