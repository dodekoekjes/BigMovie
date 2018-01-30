//created by Tristan Kruijshaar

package com.groep2.Chatbot;

//discord
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

//telegram
import org.telegram.telegrambots.*;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import javax.security.auth.login.LoginException;

public class Main {
    public static void main(String[] args) throws LoginException, InterruptedException {

        //discord bot token
        String token ="MzkyOTkxMzgwMjQ0Mzk4MDgw.DVHzJg.Qe81VAUmnhioXXM7ep124HFhAMw";

        //initialize the discord bot
        JDA bot = new JDABuilder(AccountType.BOT).setToken(token).buildBlocking();
        bot.addEventListener(new DiscordBot());

        //initialize telegram bot
//        ApiContextInitializer.init();
//
//        TelegramBotsApi botsApi = new TelegramBotsApi();
//
//        try {
//            botsApi.registerBot(new TelegramBot());
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
    }
}
