package telegram.command;

import exception.ResourceNotFoundException;
import model.Note;
import model.ServiceUser;
import org.telegram.telegrambots.meta.api.objects.Message;
import service.ApiService;
import telegram.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static telegram.command.Messages.*;
import static telegram.command.Patterns.UPDATE;

import java.util.HashMap;

/**
 * Update {@link Command}.
 */
public class UpdateCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final HashMap<Long, ServiceUser> authorizedUsers;

    public UpdateCommand(SendBotMessageService sendBotMessageService,
                         HashMap<Long, ServiceUser> authorizedUsers) {
        this.sendBotMessageService = sendBotMessageService;
        this.authorizedUsers = authorizedUsers;
    }

    @Override
    public void execute(Update update) {
        Message message = update.getMessage();
        String text = message.getText();
        if (UPDATE.getPattern().matcher(text).matches()) {
            if (authorizedUsers.containsKey(message.getChatId())) {
                String[] words = text.split(" ");
                String[] idHeaderAndBody = text.split("#");
                try {
                    Long id = Long.decode(words[1]);
                    String header = idHeaderAndBody[1].strip();
                    String body = idHeaderAndBody[2].strip();
                    if (header.equals(""))
                        header = null;
                    Note note = new Note();
                    note.setId(id);
                    note.setName(header);
                    note.setBody(body);
                    note.setStatus(true);
                    ApiService.updateNote(authorizedUsers.get(message.getChatId()).getId(), note);
                    sendBotMessageService.sendMessage(message, UPDATED_MESSAGE.getMessageText());
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