import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import telegram.Bot;


public class TinkoffPracticeTGbotApplication {

    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Bot("TinkoffPracticeNotesBot",
                    "1912787380:AAHPMClhwP-pNgC8wlhf2fOQn1ITftoTCpU"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
