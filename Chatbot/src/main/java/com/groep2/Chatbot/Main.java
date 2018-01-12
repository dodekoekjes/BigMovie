package com.groep2.Chatbot;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.telegram.telegrambots.*;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws REXPMismatchException, REngineException {

        RHandler rh = new RHandler("c(1,2,3,4,5)");

//        ApiContextInitializer.init();
//
//        TelegramBotsApi botsApi = new TelegramBotsApi();
//
//        try {
//            botsApi.registerBot(new Chatbot());
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
    }
}
