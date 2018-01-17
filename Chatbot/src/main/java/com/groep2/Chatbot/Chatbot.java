//Mainpackage jar;
package com.groep2.Chatbot;

import com.rivescript.*;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/* RiveScript calls:
    SQL query doen: sql [query]
    Shell gebruiken: shell [commando]
    Foto versturen: image [foto pad] [foto beschrijving - mag leeg zijn]
    R script runnen (nog niet getest): r [naam van r bestand in resources/R]
 */

public class Chatbot extends TelegramLongPollingBot {
    private RiveScript bot = new RiveScript(Config.utf8());

    public Chatbot() {
        bot.setSubroutine("sql", new jar.SqlSubroutine("server.kevinswebsite.nl", 3306, "moviedatabase", "root", "fietsbel"));
        bot.setSubroutine("shell", new jar.ShellSubroutine());
        bot.setSubroutine("image", new jar.SendImageSubroutine(this));
        bot.setSubroutine("r", new jar.RSubroutine());
        bot.loadDirectory("Chatbot/resources/rivescript");
        bot.sortReplies();
    }

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            // Get reply
            String reply = bot.reply(String.valueOf(chat_id), message_text);

            SendMessage message = new SendMessage() // Create a message object object
                    .setChatId(chat_id)
                    .setText(reply);
            try {
                if (!reply.isEmpty()) {
                    execute(message); // Sending our message object to user
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "Movie Facts";
    }

    @Override
    public String getBotToken() {
        return "535298953:AAFKoLil3IAE__rjscmxbG1EYeefDJin3BM";
    }
}
