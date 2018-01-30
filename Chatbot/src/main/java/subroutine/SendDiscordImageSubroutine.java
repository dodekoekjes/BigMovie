//created by Tristan Kruijshaar
package subroutine;

import com.groep2.Chatbot.DiscordBot;
import com.rivescript.RiveScript;
import com.rivescript.macro.Subroutine;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.io.File;

public class SendDiscordImageSubroutine implements Subroutine {
    private MessageChannel sender;

    public SendDiscordImageSubroutine(MessageChannel sender) {
        this.sender = sender;
    }

    @Override
    public String call(RiveScript rs, String[] args) {
        sender.sendFile(new File(args[0])).queue();
        System.out.println("Sending image");
        return "";
    }
}
