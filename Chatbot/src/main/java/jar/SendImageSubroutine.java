package jar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.rivescript.RiveScript;
import com.rivescript.util.StringUtils;
import com.rivescript.macro.Subroutine;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class SendImageSubroutine implements Subroutine {
    private AbsSender sender;

    public SendImageSubroutine(AbsSender sender) {
        this.sender = sender;
    }

    @Override
    public String call(RiveScript rs, String[] args) {
        String caption = "";
        SendPhoto reply;
        for (int i = 1; i < args.length; i++) {
            caption += " " + args[i];
        }
        caption = caption.trim();
        reply = new SendPhoto()
                .setNewPhoto(new File(args[0]))
                .setChatId(rs.currentUser());
        if (!caption.isEmpty()) {
            reply.setCaption(caption);
        }
        try {
            sender.sendPhoto(reply);
        }
        catch (TelegramApiException ex) {
            ex.printStackTrace();
        }
        System.out.println("Sending image");
        return "";
    }
}
