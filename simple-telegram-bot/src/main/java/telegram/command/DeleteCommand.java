package telegram.command;

import exception.ResourceNotFoundException;
import model.ServiceUser;
import org.telegram.telegrambots.meta.api.objects.Message;
import service.ApiService;
import telegram.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.HashMap;

import static telegram.command.Messages.*;
import static telegram.command.Patterns.DELETE;

/**
 * Delete {@link Command}.
 */
public class DeleteCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final HashMap<Long, ServiceUser> authorizedUsers;

    public DeleteCommand(SendBotMessageService sendBotMessageService,
                         HashMap<Long, ServiceUser> authorizedUsers) {
        this.sendBotMessageService = sendBotMessageService;
        this.authorizedUsers = authorizedUsers;
    }

    @Override
    public void execute(Update update) {
        Message message = update.getMessage();
        String text = message.getText();
        if (DELETE.getPattern().matcher(text).matches()) {
            String[] words = text.split(" ");
            if (authorizedUsers.containsKey(message.getChatId())) {
                try {
                    Long noteId = Long.decode(words[1]);
                    ApiService.deleteNote(authorizedUsers.get(message.getChatId()).getId(), noteId);
                    sendBotMessageService.sendMessage(message, DELETED_MESSAGE.getMessageText());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    sendBotMessageService.sendMessage(message, FAIL_CONVERSION_MESSAGE.getMessageText());
                } catch (ResourceNotFoundException e) {
                    e.printStackTrace();
                    sendBotMessageService.sendMessage(message, NOTE_NOT_FOUND_MESSAGE.getMessageText());
                }

            } else
                sendBotMessageService.sendMessage(message, NO_AUTH_MESSAGE.getMessageText());
        } else
            sendBotMessageService.sendMessage(message, WRONG_FORMAT_MESSAGE.getMessageText());
    }
}