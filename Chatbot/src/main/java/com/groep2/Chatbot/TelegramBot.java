//--------------------------------------------------------------------------------------//
// Deze pagina is gemaakt door Benjamin                                                 //
//--------------------------------------------------------------------------------------//
//Mainpackage jar;
package com.groep2.Chatbot;

import com.rivescript.*;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import subroutine.RSubroutine;
import subroutine.SendImageSubroutine;
import subroutine.SqlSubroutine;

/* RiveScript calls:
    SQL query doen: sql [query]
    Foto versturen: image [foto pad] [foto beschrijving - mag leeg zijn]
    R script runnen (nog niet getest): r [naam van r bestand in resources/R]
 */

public class TelegramBot extends TelegramLongPollingBot {
    private RiveScript bot = new RiveScript(Config.utf8());

    public TelegramBot() {
        bot.setSubroutine("sql", new subroutine.SqlSubroutine("server.kevinswebsite.nl", 3306, "moviedatabase", "root", "fietsbel"));
        bot.setSubroutine("image", new subroutine.SendImageSubroutine(this));
        bot.setSubroutine("r", new subroutine.RSubroutine());
        bot.loadDirectory("Chatbot/src/main/resources/rivescript");
        bot.sortReplies();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            String reply = bot.reply(String.valueOf(chat_id), message_text);
            String replies[] = reply.split("\n\n");

            for (int i = 0; i < replies.length; i++){
                SendMessage message = new SendMessage()
                        .setChatId(chat_id)
                        .setText(replies[i]);
                try {
                    if (!replies[i].isEmpty()) {
                        execute(message);
                    }
                } catch (TelegramApiException e) {
                    SendMessage error = new SendMessage()
                            .setChatId(chat_id)
                            .setText(e.getMessage());
                    try {
                        execute(error);
                    }
                    catch (TelegramApiException ex) {
                        ex.printStackTrace();
                    }
                }
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
