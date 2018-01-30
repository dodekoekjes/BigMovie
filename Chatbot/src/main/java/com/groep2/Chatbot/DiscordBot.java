//created by Tristan Kruijshaar
package com.groep2.Chatbot;

import com.rivescript.*;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.File;

public class DiscordBot extends ListenerAdapter{
    private RiveScript rive = new RiveScript(Config.utf8());

    public DiscordBot() {
        rive.setSubroutine("sql", new subroutine.SqlSubroutine("server.kevinswebsite.nl", 3306, "moviedatabase", "root", "fietsbel"));

        rive.setSubroutine("r", new subroutine.RSubroutine());
        rive.loadDirectory("Chatbot/src/main/resources/rivescript");
        rive.sortReplies();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){

        Message objMsg = event.getMessage();
        MessageChannel objChannel = event.getChannel();
        User objUser = event.getAuthor();
        rive.setSubroutine("image", new subroutine.SendDiscordImageSubroutine(objChannel));

        if(!objUser.isBot()) {
            String reply = rive.reply(objChannel.getId(), objMsg.getContentRaw());
            System.out.println(reply);
            if (!reply.isEmpty()) {
                objChannel.sendMessage(reply).queue();
            }
        }
    }
}
