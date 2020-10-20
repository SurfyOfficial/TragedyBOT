package surfy.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import surfy.API.APIManager;
import surfy.managers.CommandsManager;
import surfy.managers.EventListener;
import surfy.utils.ConfigManager;

import java.util.EnumSet;

public class Main {
    private static JDA JDiscordAPI;
    private static CommandsManager commandsManager;
    private static ConfigManager configManager;
    public static final String version = "TragedyBOT v2.3 by ↬Surfy#0069";
    public static final String head = "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec";

    public static void main(String[] args) {
        try{
            configManager = new ConfigManager(new APIManager().getGuild().getMembers());
            configManager.load();
            commandsManager = new CommandsManager();

            JDABuilder builder = new JDABuilder("CENSORED"); //test bot
            //JDABuilder builder = new JDABuilder("CENSORED"); //actual bot

            builder.setDisabledCacheFlags(EnumSet.of(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE));
            builder.setBulkDeleteSplittingEnabled(false);
            builder.setCompression(Compression.NONE);
            builder.setActivity(Activity.listening("type >g help | ❤"));
            builder.setStatus(OnlineStatus.ONLINE);
            JDiscordAPI = builder.addEventListeners(new EventListener()).build();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public static JDA getJDiscordAPI() {
        return JDiscordAPI;
    }

    public static CommandsManager getCommandsManager() {
        return commandsManager;
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }
}
