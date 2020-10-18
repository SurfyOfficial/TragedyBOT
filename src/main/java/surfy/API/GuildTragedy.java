package surfy.API;

import java.util.Arrays;
import java.util.Map;

public class GuildTragedy {
    private GuildTragedy guild;
    private long exp;
    private GuildMember[] members;
    private Map<String, Integer> achievements;

    public GuildTragedy getGuild() {
        return guild;
    }

    public long getExp() {
        return exp;
    }

    public String getCreated() {
        return "18-08-2019";
    }

    public long getOnlinePlayers() throws Exception {
        APIManager api = new APIManager();
        return Arrays.asList(members)
                .stream()
                .filter(guildMember -> {
                    try {
                        String username = MojangAPI.getUsername(guildMember.getUUID(), true);
                        Player player = api.getPlayer(username);
                        return player.getLastLogin() > player.getLastLogout();
                    }
                    catch(Exception exc) {
                        return false;
                    }
                })
                .count();
    }

    public GuildMember[] getMembers() {
        return members;
    }

    public Map<String, Integer> getAchievements() {
        return achievements;
    }
}