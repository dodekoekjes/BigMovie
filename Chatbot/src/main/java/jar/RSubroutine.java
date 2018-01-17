package jar;

import com.rivescript.RiveScript;
import com.rivescript.util.StringUtils;
import com.rivescript.macro.Subroutine;
import org.rosuda.JRI.Rengine;

public class RSubroutine implements Subroutine {
    private Rengine rg;
    private String[] rargs;

    public RSubroutine() {
        rargs = new String[0];
        rg = new Rengine(rargs, false, null);
        if (!Rengine.versionCheck()) throw new RuntimeException("Rengine.versionCheck() failed");
    }


    @Override
    public String call(RiveScript rs, String[] args) {
        rargs = new String[args.length-1];
        for (int i = 1; i < args.length-1; i++) rargs[i-1] = args[i];
        rg.eval("source(\"Chatbot/resources/R/" + args[0] + "\")");
        return "";
    }
}
