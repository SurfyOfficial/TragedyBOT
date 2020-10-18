package surfy.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.dv8tion.jda.api.entities.User;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class APIManager {
    private final String API_KEY = "CENSORED";
    /* Hypixel API-KEY */

    private String guildData;
    /* Guild_DATA */

    private Document skierData,skierWins;
    /* Documents used from sk1er.club */

    public APIManager() throws Exception {
        /* Connections */
        String guildID = "5979b6330cf2d93de3bc6779";
        Connection.Response response = Jsoup.connect("https://api.hypixel.net/guild?key=" + API_KEY + "&id=" + guildID).userAgent("Mozilla").ignoreContentType(true).execute();
        while(response.statusCode() != 200)
            response = Jsoup.connect("https://api.hypixel.net/guild?key=" + API_KEY + "&id=" + guildID).userAgent("Mozilla").ignoreContentType(true).execute();
        this.guildData = response.body();
        this.skierData = Jsoup.connect("https://sk1er.club/guild/player/Surfy").userAgent("Mozilla").ignoreContentType(true).execute().parse();
        this.skierWins = Jsoup.connect("https://sk1er.club/leaderboards/newdata/guild_wins_bedwars").userAgent("Mozilla").ignoreContentType(true).execute().parse();
    }

    public String[] getLevel() {
        /* Getting Guild Level */
        String data = skierData.getElementsByTag("strong")
                .stream()
                .filter(e->e.text().contains("Guild Level"))
                .findFirst()
                .get()
                .nextSibling()
                .toString();
        return data.split("\\.");
    }

    public ArrayList<GuildLB> getWinsLeaderboard() {
        /* Getting all the infos to build the WINSLB */
        ArrayList<GuildLB> leaderboard = new ArrayList<GuildLB>();
        Element table = skierWins.select("table").get(0);
        Elements list = table.select("tbody").first().select("tr");
        for(int i = 0; i < 10; i++) {
            Element team = list.get(i);
            String name = Jsoup.clean(team.select("td").get(2).text(), Whitelist.simpleText());
            String bwWins = team.select("td").get(3).text();
            String totalWins = team.select("td").get(4).text();
            String level = team.select("td").get(6).text();
            String exp = team.select("td").get(7).text();
            leaderboard.add(new GuildLB(name,bwWins,totalWins,level,exp));
        }
        return leaderboard;
    }

    public String getLbRank() {
        /* Getting the Guild #position in Level */
        return skierData.getElementsByTag("strong")
                .stream()
                .filter(e->e.text().contains("Guild Level Position"))
                .findFirst()
                .get()
                .nextSibling()
                .toString();
    }

    public GuildTragedy getGuild() {
        /* Getting the guild from the source */
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        return gson.fromJson(this.guildData,GuildTragedy.class).getGuild();
    }

    public Player getPlayer(String username) throws Exception {
        /* Getting the player by username */
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        String playerData = Jsoup.connect("https://api.hypixel.net/player?key=" + API_KEY + "&name=" + username).userAgent("Mozilla").ignoreContentType(true).execute().body();
        return gson.fromJson(playerData,Player.class).getPlayer();
    }

    public HashMap<String,Integer> getCurrentEXP() {
        /* Getting GXP */
        HashMap<String,Integer> topList = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
        String date = simpleDateFormat.format(new Date());
        List<GuildMember> guildMemberList = Arrays.asList(this.getGuild().getMembers());
        guildMemberList
                .forEach(user-> topList.put(MojangAPI.getUsername(user.getUUID(),true),user.getExpHistory().get(date)));
        return topList;
    }

    public HashMap<String,Integer> getCurrentEXPSum() {
        /* Getting weekly GXP */
        HashMap<String,Integer> topList = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
        List<GuildMember> guildMemberList = Arrays.asList(this.getGuild().getMembers());
        guildMemberList
                .forEach(user-> {
                    AtomicLong atomicLong = new AtomicLong(0);
                    user.getExpHistory().values().forEach(val->atomicLong.set(atomicLong.get() + val));
                    topList.put(MojangAPI.getUsername(user.getUUID(),true),atomicLong.intValue());
                });
        return topList;
    }

    public GuildMember getGuildMember(String uuid) {
        /* Getting Guild Member */
        GuildTragedy guild = this.getGuild();
        for(GuildMember guildMember : guild.getMembers())
            if(guildMember.getUUID().equals(uuid))
                return guildMember;
        return null;
    }

    public void sendPrivateMessage(User user, String content) {
        /* Method to send a private DM in discord. */
        user.openPrivateChannel().queue((channel) -> channel.sendMessage(content).queue());
    }
}