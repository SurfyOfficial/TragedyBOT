package surfy.modes;

import net.dv8tion.jda.api.utils.MarkdownSanitizer;
import surfy.API.Player;

@SuppressWarnings("unused")
public class Bedwars {
    private Player player;

    public Bedwars(Player player){
        this.player = player;
    }

    public String getName() {
        return player.getDisplayname();
    }

    public int getLevel() {
        return player.getAchievements().get("bedwars_level");
    }

    public double getWins() {
        return (double)player.getStats().get("Bedwars").get("wins_bedwars");
    }

    public double getLosses() {
        return (double)player.getStats().get("Bedwars").get("losses_bedwars");
    }

    public double getKills() {
        return (double)player.getStats().get("Bedwars").get("final_kills_bedwars");
    }

    public double getDeaths() {
        return (double)player.getStats().get("Bedwars").get("final_deaths_bedwars");
    }


    public long getReqLevel() {
        return (long)(player.getAchievements().get("bedwars_level"));
    }

    public long getReqWins() {
        return (long)player.getStats().get("Bedwars").get("wins_bedwars");
    }
}