package subroutine;

import com.rivescript.RiveScript;
import com.rivescript.macro.Subroutine;
import org.telegram.telegrambots.api.methods.groupadministration.KickChatMember;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class KickSubroutine implements Subroutine {
    private AbsSender sender;

    public KickSubroutine(AbsSender sender) { this.sender = sender; }

    @Override
    public String call(RiveScript rs, String[] args) {
        KickChatMember kickm = new KickChatMember();
        kickm.setChatId(rs.currentUser()).setUserId(Integer.parseInt(args[0]));

        try {
             sender.execute(kickm);
        }
        catch (TelegramApiException ex) {
            ex.printStackTrace();
        }
        return "";
    }
}