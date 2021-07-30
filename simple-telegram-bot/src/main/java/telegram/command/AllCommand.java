package telegram.command;

import model.Note;
import model.ServiceUser;
import org.telegram.telegrambots.meta.api.objects.Message;
import service.ApiService;
import telegram.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static telegram.command.Messages.*;
import static telegram.command.Patterns.ALL;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * All {@link Command}.
 */
public class AllCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final HashMap<Long, ServiceUser> authorizedUsers;
    private final HashMap<Long, Long> usersSettings;

    private final static String HEADER_TEXT = "id | заголовок | текст заметки\n\n";
    private final static String DELIM = " | ";

    public AllCommand(SendBotMessageService sendBotMessageService,
                      HashMap<Long, ServiceUser> authorizedUsers,
                      HashMap<Long, Long> usersSettings) {
        this.sendBotMessageService = sendBotMessageService;
        this.authorizedUsers = authorizedUsers;
        this.usersSettings = usersSettings;
    }

    @Override
    public void execute(Update update) {
        Message message = update.getMessage();
        String text = message.getText();
        if (ALL.getPattern().matcher(text).matches()) {
            String[] words = text.split(" ");
            if (authorizedUsers.containsKey(message.getChatId())) {
                long pageNum = 0L;
                if (words.length == 2) {
                    try {
                        pageNum = Long.decode(words[1]) - 1;
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        sendBotMessageService.sendMessage(message, FAIL_CONVERSION_MESSAGE.getMessageText());
                    }
                }
                List<Note> ln = ApiService.getNotesByUserId(
                        authorizedUsers.get(message.getChatId()).getId(),
                        pageNum,
                        usersSettings.get(message.getChatId()));
                StringBuilder outText = new StringBuilder(HEADER_TEXT);
                for (Note note : ln) {
                    outText.append(note.getId())
                            .append(DELIM)
                            .append(note.getName())
                            .append(DELIM)
                            .append(note.getBody(), 0, Math.min(15, note.getBody().length()))
                            .append("..\n\n");
                }
                outText.append("Страница: ").append(pageNum + 1L);
                sendBotMessageService.sendMessage(message, outText.toString());
            } else
                sendBotMessageService.sendMessage(message, NO_AUTH_MESSAGE.getMessageText());
        } else
            sendBotMessageService.sendMessage(message, WRONG_FORMAT_MESSAGE.getMessageText());
    }
}
