package telegram.command;

public enum Messages {

    START_MESSAGE(
            "Вас приветствует бот, позволяющий получить доступ к прототипу сервиса для " +
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
                    "4) выход (опционально) командой /signout."),
    HELP_MESSAGE(
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
                    "Примечание 1: при вводе аргументов к командам не нужно указывать \"<\" и \">\"\n" +
                    "Примечание 2: разделитель \"#\" необходимо указывать обязательно\n" +
                    "Примечание 3: заголовок заметки является не обязательным"
    ),
    SETTINGS_MESSAGE("Установлен размер страницы - "),
    FAIL_CONVERSION_MESSAGE("Не удалось преобразовать число"),
    WRONG_FORMAT_MESSAGE(
            "Не верный формат сообщения. Список доступных команд " +
                    "и их формат доступен по команде /help"),
    NO_AUTH_MESSAGE(
            "Вы не авторизированы. Зайдите в учётную запись командой /signin или " +
                    "зарегистрируйтесь командой /signup. Подробнее - /help"),
    ERROR_MESSAGE("Произошла ошибка..."),
    CREATED_MESSAGE("Заметка была успешно добавлена"),
    SIGNED_IN_MESSAGE("Вы успешно авторизировались"),
    USER_NOT_FOUND_MESSAGE("Учётная запись с указанным логином не была найдена"),
    NOTE_NOT_FOUND_MESSAGE("Заметка с указанным id не была найдена"),
    WRONG_PASSWORD_MESSAGE("Не верный пароль"),
    SIGNED_OUT_MESSAGE("Вы успешно деавторизировались"),
    SIGNED_UP_MESSAGE("Учётная запись успешно зарегистрирована"),
    PASSWORDS_MISMATCHING_MESSAGE("Пароли не совпадают"),
    USER_EXISTS_MESSAGE("Указанный логин уже занят"),
    UPDATED_MESSAGE("Заметка была успешно обновлена"),
    DELETED_MESSAGE("Заметка была успешно удалена"),
    UNKNOWN_MESSAGE("Кажется, я Вас не понимаю..."),
    NO_MESSAGE("Я поддерживаю команды, начинающиеся со слеша(/).\n"
            + "Чтобы посмотреть список команд введите /help");

    private final String messageText;

    Messages(String text) {
        this.messageText = text;
    }

    public String getMessageText() {
        return messageText;
    }

}