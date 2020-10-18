package surfy.API;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicLong;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.dv8tion.jda.api.entities.User;

public class APIManager {
    private final String API_KEY = "a37e4660-353c-4ee4-a64b-882e56fedcd0";

    //private final String RebelID = "53bd67d7ed503e868873eceb";
    //private final String RawrID = "5320667aed50f531e5de4f5b";
    //private final String MatrixID = "53c81676ed50878f4ccc75be";
    //private final String blueBloodsID = "5a16eb970cf2c642769b0b68";
    //private final String EnigmaID = "5988f8340cf2851f860c9a7b";
    //private final String bloodLustID = "53c81676ed50878f4ccc75be";
    //private final String frogletsID = "5a595e490cf29432ef9de234";
    //private final String honraID = "5ab14d920cf201afed33a653";
    //private final String lucidID = "5bb8828e0cf2d46f5a5c5bbc";
    //private final String AbyssID = "5ac8a3780cf2d841f8a664ad";

    private String guildData;
    private Document skierData,skierWins;

    public APIManager() throws Exception {
        String guildID = "5979b6330cf2d93de3bc6779";
        Connection.Response response = Jsoup.connect("https://api.hypixel.net/guild?key=" + API_KEY + "&id=" + guildID).userAgent("Mozilla").ignoreContentType(true).execute();
        while(response.statusCode() != 200)
            response = Jsoup.connect("https://api.hypixel.net/guild?key=" + API_KEY + "&id=" + guildID).userAgent("Mozilla").ignoreContentType(true).execute();
        this.guildData = response.body();

        //this.nextGuild = Jsoup.connect("https://api.hypixel.net/guild?key=" + API_KEY + "&id=" + AbyssID).userAgent("Mozilla").ignoreContentType(true).execute().body();
        this.skierData = Jsoup.connect("https://sk1er.club/guild/player/Surfy").userAgent("Mozilla").ignoreContentType(true).execute().parse();
        //this.skierLB = Jsoup.connect("https://sk1er.club/leaderboards/newdata/GUILD_LEVEL").userAgent("Mozilla").ignoreContentType(true).execute().parse();
        this.skierWins = Jsoup.connect("https://sk1er.club/leaderboards/newdata/guild_wins_bedwars").userAgent("Mozilla").ignoreContentType(true).execute().parse();
    }

    public String[] getLevel() {
        String data = skierData.getElementsByTag("strong")
                .stream()
                .filter(e->e.text().contains("Guild Level"))
                .findFirst()
                .get()
                .nextSibling()
                .toString();
        return data.split("\\.");
    }

    public ArrayList<GuildLB> getWinsLeaderboard(){
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
        return skierData.getElementsByTag("strong")
                .stream()
                .filter(e->e.text().contains("Guild Level Position"))
                .findFirst()
                .get()
                .nextSibling()
                .toString();
    }

    public GuildTragedy getGuild() {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        return gson.fromJson(this.guildData,GuildTragedy.class).getGuild();
    }

    /*public NextGuild getNext() {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        return gson.fromJson(this.nextGuild,NextGuild.class).getGuild();
    }*/

    public Player getPlayer(String username) throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        String playerData = Jsoup.connect("https://api.hypixel.net/player?key=" + API_KEY + "&name=" + username).userAgent("Mozilla").ignoreContentType(true).execute().body();
        return gson.fromJson(playerData,Player.class).getPlayer();
    }

    public HashMap<String,Integer> getCurrentEXP() {
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

    /*spublic ArrayList<GuildLB> getLeaderboard(){
        ArrayList<GuildLB> leaderboard = new ArrayList<GuildLB>();
        Element table = skierLB.select("table").get(0);
        Elements list = table.select("tbody").first().select("tr");
        for(int i = 0; i < 10; i++) {
            Element team = list.get(i);
            String name = Jsoup.clean(team.select("td").get(2).text(), Whitelist.simpleText());
            String level = team.select("td").get(3).text();
            String exp = team.select("td").get(5).text();
            leaderboard.add(new GuildLB(name,level,exp));
        }
        return leaderboard;
    }

    public Status getStatus(String uuid) throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

        String statusData = Jsoup.connect("https://api.hypixel.net/status?key=" + API_KEY + "&uuid=" + uuid).userAgent("Mozilla").ignoreContentType(true).execute().body();
        Status session = gson.fromJson(statusData,Status.class);
        return session.getStatus();
    }*/

    public GuildMember getGuildMember(String uuid) {
        GuildTragedy guild = this.getGuild();
        for(GuildMember guildMember : guild.getMembers())
            if(guildMember.getUUID().equals(uuid))
                return guildMember;
        return null;
    }

    public void sendPrivateMessage(User user, String content) {
        user.openPrivateChannel().queue((channel) -> channel.sendMessage(content).queue());
    }
}