package subroutine;

import com.rivescript.macro.Subroutine;
import com.rivescript.util.StringUtils;

import java.io.IOException;

public class ShellSubroutine implements Subroutine{
    @Override
    public String call(com.rivescript.RiveScript rs, String[] args) {
        String cmd = StringUtils.join(args, " ");

        java.util.Scanner s;
        try {
            s = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "";
    }
}
