package telegram;

import model.Note;
import model.ServiceUser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import service.ApiService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

public final class Bot extends TelegramLongPollingBot {
    private final String BOT_TOKEN;
    private final String BOT_NAME;

    private HashMap<Long, ServiceUser> authorizedUsers;
    private HashMap<Long, Long> usersSettings;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public Bot(String botName, String botToken) {
        super();
        this.BOT_NAME = botName;
        this.BOT_TOKEN = botToken;

        authorizedUsers = new HashMap<>();
        usersSettings = new HashMap<>();
    }

    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            String text = message.getText();
            String[] words = text.split(" ");
            switch (words[0]) {
                case "/start":
                    sendMsg(message, "Вас приветствует бот, позволяющий получить доступ к прототипу сервиса для " +
                            "ведения заметок, разработанный в рамках летней практики в Тинькофф 2021 студентами " +
                            "РГРТУ.\n" +
                            "Сервис позволяет хранить, просматривать, изменять и удалять заметки, состоящие " +
                            "из заголовка и поля с текстом. Также сервис предусматривает элементарную систему " +
                            "аутентификации (логин и пароль).\n" +
                            "Для получения списка доступных команд отправьте /help.\n" +
                            "Предполагаемая последоватьельность действий:\n" +
                            "1) регистрация командой /signup;\n" +
                            "2) аутентификация командой /signin;\n" +
                            "3) работа с заметками (команды /all, /create, /read, /update, /delete);\n" +
                            "4) выход (опционально) командой /signout.");
                    break;
                case "/help":
                    sendMsg(message,
                            "Сервисные команды:\n" +
                            "/start - начать\n" +
                            "/help - помощь\n" +
                            "/settings <целое_число> - задать количество выводимых заметок на странице\n\n" +
                            "Команды для авторизации:\n" +
                            "/signin <логин> <пароль> - авторизация\n" +
                            "/signup <логин> <пароль> <подтверждение_пароля> - регистрация\n" +
                            "/signout - деавторизация\n\n" +
                            "Команды для работы с заметками:\n" +
                            "/all - просмотр заметок\n" +
                            "/create <заголовок> # <текст> - создание заметки\n" +
                            "/read <id_заметки> - просмотр одной заметки\n" +
                            "/update <id_заметки> # <заголовок> # <текст> - обновление заметки\n" +
                            "/delete <id_заметки> - удаление заметки\n\n" +
                            "Примечание 1: при вводе аргументов к командам не нужно указывать \"<\" и \">\"" +
                            "Примечание 2: разделитель \"#\" необходимо указывать обязательно\n" +
                            "Примечание 3: заголовок заметки является не обязательным");
                    break;
                case "/settings":
                    if (words.length == 2)
                        try {
                            usersSettings.put(message.getChatId(), Long.decode(words[1]));
                            sendMsg(message, "Установлен размер страницы - " + Long.decode(words[1]));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            sendMsg(message, "Не удалось преобразовать число");
                        }
                    else
                        sendMsg(message, "Не верный формат сообщения. Список доступных команд и их формат доступен " +
                                "по команде /help");
                    break;
                case "/signup":
                    if (words.length == 4)
                        if (words[2].equals(words[3]))
                            try {
                                ApiService.createUser(new ServiceUser(words[1], words[2]));
                                sendMsg(message, "Учётная запись успешно создана. Для авторизации воспользуйтесь " +
                                        "командой /signin");
                            } catch (IOException | InterruptedException e) {
                                e.printStackTrace();
                                sendMsg(message, "Произошла ошибка...");
                            }
                        else
                            sendMsg(message, "Пароли не совпадают");
                    else
                        sendMsg(message, "Не верный формат сообщения. Список доступных команд и их формат доступен " +
                                "по команде /help");
                    break;
                case "/signin":
                    if (words.length == 3)
                        try {
                            ServiceUser user = ApiService.getUserByLogin(words[1]);
                            if (user.getPassword().equals(words[2])) {
                                sendMsg(message, "Вы успешно авторизировались");
                                authorizedUsers.put(message.getChatId(), user);
                                usersSettings.put(message.getChatId(), 5L);
                            } else
                                sendMsg(message, "Неверный логин или пароль");
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                            sendMsg(message, "Произошла ошибка...");
                        }
                    else
                        sendMsg(message, "Не верный формат сообщения. Список доступных команд и их формат доступен " +
                                "по команде /help");
                    break;
                case "/signout":
                    authorizedUsers.remove(message.getChatId());
                    sendMsg(message, "Вы успешно деавторизировались");
                    break;
                case "/all":
                    if (authorizedUsers.containsKey(message.getChatId())) {
                        Long pageNum = 0L;
                        if (words.length == 2) {
                            try {
                                pageNum = Long.decode(words[1]) - 1;
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                sendMsg(message, "Не удалось преобразовать число");
                            }
                        }
                        try {
                            List<Note> ln = ApiService.getNotesByUserId(
                                    authorizedUsers.get(message.getChatId()).getId(),
                                    pageNum,
                                    usersSettings.get(message.getChatId()));
                            String outText = "id | заголовок | текст заметки\n\n";
                            for (Note note : ln) {
                                outText = outText + note.getId() + " | " + note.getName() + " | "
                                        + note.getBody().substring(0, Math.min(15, note.getBody().length())) + "..\n\n";
                            }
                            outText = outText + "Страница: " + (pageNum + 1L);
                            sendMsg(message, outText);
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                            sendMsg(message, "Произошла ошибка...");
                        }
                    } else
                        sendMsg(message, "Вы не авторизированы. Зайдите в учётную запись командой /signin или " +
                                "зарегистрируйтесь командой /signup. Подробнее - /help");
                    break;
                case "/read":
                    if (authorizedUsers.containsKey(message.getChatId())) {
                        if (words.length == 2) {
                            try {
                                Long noteId = Long.decode(words[1]);
                                Note note = ApiService.getNoteById(authorizedUsers.get(message.getChatId()).getId(), noteId);
                                sendMsg(message, note.getId() + " - " + note.getName() + "\n\n" + note.getBody() +
                                        "\n\nСоздано:\n" + dateFormat.format(note.getCreatedAt()) +
                                        "\nПоследнее изменение:\n" + dateFormat.format(note.getUpdatedAt()));
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                sendMsg(message, "Не удалось преобразовать число");
                            } catch (IOException | InterruptedException e) {
                                e.printStackTrace();
                                sendMsg(message, "Произошла ошибка...");
                            }
                        } else
                            sendMsg(message, "Не верный формат сообщения. Список доступных команд и их формат доступен " +
                                    "по команде /help");
                    } else
                        sendMsg(message, "Вы не авторизированы. Зайдите в учётную запись командой /signin или " +
                                "зарегистрируйтесь командой /signup. Подробнее - /help");
                    break;
                case "/create":
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
                        try {
                            ApiService.createNote(authorizedUsers.get(message.getChatId()).getId(), note);
                            sendMsg(message, "Заметка была успешно добавлена");
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                            sendMsg(message, "Произошла ошибка...");
                        }
                    } else
                        sendMsg(message, "Вы не авторизированы. Зайдите в учётную запись командой /signin или " +
                                "зарегистрируйтесь командой /signup. Подробнее - /help");
                    break;
                case "/update":
                    if (authorizedUsers.containsKey(message.getChatId())) {
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
                            sendMsg(message, "Заметка была успешно изменена");
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            sendMsg(message, "Не удалось преобразовать id");
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                            sendMsg(message, "Произошла ошибка...");
                        }
                    } else
                        sendMsg(message, "Вы не авторизированы. Зайдите в учётную запись командой /signin или " +
                                "зарегистрируйтесь командой /signup. Подробнее - /help");
                    break;
                case "/delete":
                    if (authorizedUsers.containsKey(message.getChatId())) {
                        if (words.length == 2) {
                            try {
                                Long noteId = Long.decode(words[1]);
                                ApiService.deleteNote(authorizedUsers.get(message.getChatId()).getId(), noteId);
                                sendMsg(message, "Заметка была успешно удалена");
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                sendMsg(message, "Не удалось преобразовать число");
                            } catch (IOException | InterruptedException e) {
                                e.printStackTrace();
                                sendMsg(message, "Произошла ошибка...");
                            }
                        } else
                            sendMsg(message, "Не верный формат сообщения. Список доступных команд и их формат доступен " +
                                    "по команде /help");
                    } else
                        sendMsg(message, "Вы не авторизированы. Зайдите в учётную запись командой /signin или " +
                                "зарегистрируйтесь командой /signup. Подробнее - /help");
                    break;
                default:
                    sendMsg(message, "Кажется, я Вас не понимаю...");
                    break;
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