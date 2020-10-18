package surfy.API;

import com.sun.xml.internal.ws.util.StringUtils;

public class Status {
    private boolean online;
    private Status session;
    private String gameType;

    public boolean isSuccess() {
        return online;
    }

    public Status getStatus() {
        return session;
    }

    public boolean isOnline(){
        return online;
    }

    public String getGameType() {
        return StringUtils.capitalize(gameType.toLowerCase());
    }
}
