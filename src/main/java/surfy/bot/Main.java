package surfy.bot;

import java.util.EnumSet;

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

public class Main {
    private static JDA JDiscordAPI;
    private static CommandsManager commandsManager;
    private static ConfigManager configManager;

    public static void main(String[] args) {
        try{
            configManager = new ConfigManager(new APIManager().getGuild().getMembers());
            configManager.load();
            commandsManager = new CommandsManager();

            //JDABuilder builder = new JDABuilder("NzU0MDg3Mzk2NTEzMTUzMDI2.X1vonQ.Nbn9mBFrGrf-KoGCuhUNyb70gNM"); //test bot
            JDABuilder builder = new JDABuilder("NzU0MDc2ODM4MTA3ODA3ODE1.X1veyA.z4kg0wB7iyzCxlfqyegm2lyV55k"); //actual bot

            builder.setDisabledCacheFlags(EnumSet.of(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE));
            builder.setBulkDeleteSplittingEnabled(false);
            builder.setCompression(Compression.NONE);
            builder.setActivity(Activity.listening("type >g help | ❤"));
            builder.setStatus(OnlineStatus.ONLINE);
            JDiscordAPI = builder.addEventListeners(new EventListener()).build();
        } catch (Exception exc){System.out.println(exc.getMessage());}
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
