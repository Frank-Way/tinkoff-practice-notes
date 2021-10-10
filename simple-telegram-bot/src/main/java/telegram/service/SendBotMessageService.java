package telegram.service;

import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Service for sending messages via telegram-bot.
 */
public interface SendBotMessageService {

    /**
     * Send message via telegram bot.
     *
     * @param message provided recieved message.
     * @param text provided message to be sent.
     */
    void sendMessage(Message message, String text);
}