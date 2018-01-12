package com.groep2.Chatbot;


import org.telegram.telegrambots.*;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Main {
    public static void main(String[] args){
        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(new Chatbot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
