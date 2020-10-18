package surfy.API;

import java.util.Map;

import surfy.modes.*;

public class Player {
    private Player player;
    private String displayname;
    private long lastLogin;
    private long lastLogout;
    private long networkExp;
    private String uuid;

    private Map<String, Integer> achievements;
    private Map<String, Map<String,Object>> stats;


    public Player getPlayer() {
        return player;
    }

    public String getDisplayname() {
        return displayname;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public long getLastLogout() {
        return lastLogout;
    }

    public String getUUID() {
        return uuid;
    }

    public float getNetworkExp() {
        return networkExp;
    }

    public Map<String, Integer> getAchievements() {
        return achievements;
    }

    public Map<String, Map<String, Object>> getStats() {
        return stats;
    }

    public Bedwars getBedwarsInfo() {
        return new Bedwars(this);
    }

   /* public Duels getDuelsInfo() {
        return new Duels(this);
    }

    public Skywars getSkywarsInfo() {
        return new Skywars(this);
    }

    public UHC getUHCInfo() {
        return new UHC(this);
    }

    public BSG getBSGInfo() {
        return new BSG(this);
    }
    */
}
