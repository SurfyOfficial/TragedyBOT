package surfy.API;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import surfy.bot.Main;
import surfy.utils.ConfigManager;
import surfy.utils.Utils;
import net.dv8tion.jda.api.utils.MarkdownSanitizer;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import org.jsoup.Jsoup;

import java.util.Date;
import java.util.Properties;

@SuppressWarnings("unused")
public class MojangAPI {

    public static String getUsername(String UUID,boolean config) {
        try{
            if(config){
                Properties properties = Main.getConfigManager().load();
                if(properties.containsKey(UUID)) return properties.getProperty(UUID);
            }
            String content = Jsoup.connect("https://api.mojang.com/user/profiles/"+UUID+"/names").ignoreContentType(true).execute().body();
            Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
            PreviousPlayerNameEntry[] names = gson.fromJson(content, PreviousPlayerNameEntry[].class);
            String playerName = names[names.length-1].getPlayerName();
            if(Main.getConfigManager().exist()) Main.getConfigManager().addUser(UUID,playerName);
            return playerName;
        } catch(Exception exc) {
            return UUID;
        }
    }

    public static String getUUID(String username,boolean config) {
        try{
            if(config){
                Properties properties = Main.getConfigManager().load();
                if(properties.containsValue(username)) return (String) Utils.getKeyByValue(properties,username);
            }
            String content = Jsoup.connect("https://api.mojang.com/users/profiles/minecraft/"+username).ignoreContentType(true).execute().body();
            Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
            Profile profile = gson.fromJson(content,Profile.class);
            String UUID = profile.getId();
            if(Main.getConfigManager().exist()) Main.getConfigManager().addUser(UUID,username);
            return UUID;
        }catch(Exception exc){return null;}
    }
}


class Profile {
    private String  id;
    private String name;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}

class PreviousPlayerNameEntry {
    private String name;
    @SerializedName("changedToAt")
    private long changeTime;

    public String getPlayerName() {
        return name;
    }

    public long getChangeTime() {
        return changeTime;
    }

    public boolean isPlayersInitialName() {
        return getChangeTime() == 0;
    }
    @Override
    public String toString() {
        return "Name: " + name + " Date of change: " + new Date(changeTime).toString();
    }
}