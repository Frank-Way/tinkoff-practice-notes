package telegram.command;

import model.Note;
import model.ServiceUser;
import org.telegram.telegrambots.meta.api.objects.Message;
import service.ApiService;
import telegram.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static telegram.command.Messages.*;
import static telegram.command.Patterns.CREATE;

import java.io.IOException;
import java.util.HashMap;

/**
 * Create {@link Command}.
 */
public class CreateCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final HashMap<Long, ServiceUser> authorizedUsers;

    public CreateCommand(SendBotMessageService sendBotMessageService,
                         HashMap<Long, ServiceUser> authorizedUsers) {
        this.sendBotMessageService = sendBotMessageService;
        this.authorizedUsers = authorizedUsers;
    }

    @Override
    public void execute(Update update) {
        Message message = update.getMessage();
        String text = message.getText();
        if (CREATE.getPattern().matcher(text).matches()) {
            if (authorizedUsers.containsKey(message.getChatId())) {
                String[] headerAndBody = text.split("#");
                String header = headerAndBody[0].substring(7).strip();
                String body = headerAndBody[1].strip();
                if (header.equals(""))
                    header = null;
                Note note = new Note();
                note.setName(header);
                note.setBody(body);
                note.setStatus(true);
                ApiService.createNote(authorizedUsers.get(message.getChatId()).getId(), note);
                sendBotMessageService.sendMessage(message, CREATED_MESSAGE.getMessageText());
            } else
                sendBotMessageService.sendMessage(message, NO_AUTH_MESSAGE.getMessageText());
        } else
            sendBotMessageService.sendMessage(message, WRONG_FORMAT_MESSAGE.getMessageText());
    }
}