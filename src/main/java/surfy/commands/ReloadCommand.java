package surfy.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import surfy.API.APIManager;
import surfy.API.GuildTragedy;
import surfy.bot.Main;
import surfy.managers.Command;
import surfy.utils.Utils;

import java.awt.*;
import java.util.Date;
import java.util.Objects;

public class ReloadCommand extends Command {

    private static final String Prefix = ">g ";

    public ReloadCommand() {
        super(Prefix + "reload");
    }

    @Override
    public void onExecute(Message message, String[] args) {
        try {
            APIManager apiManager = new APIManager();
            GuildTragedy guildInfo = apiManager.getGuild();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            EmbedBuilder embedReload = new EmbedBuilder();

            if(!Utils.isSurfy(message.getAuthor().getId()) & !Utils.isGuildMember(Objects.requireNonNull(message.getMember()))) {
                embedBuilder.setTitle("Error! You must be a Guild Member in order to use this bot!")
                        .setColor(Color.red).setFooter("TragedyBOT v2.2 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
						.setTimestamp(new Date().toInstant())
                        .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(), message.getAuthor().getEffectiveAvatarUrl(), message.getAuthor().getEffectiveAvatarUrl());
                message.getChannel().sendMessage(embedBuilder.build()).queue();
                return;
            }

            if(!Utils.isSurfy(message.getAuthor().getId()) & !Utils.isOfficer(message.getMember())) {
                embedReload.setTitle("You do not have this permission.")
                        .setColor(Color.red)
                        .setFooter("TragedyBOT v2.2 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
						.setTimestamp(new Date().toInstant())
                        .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl());
                message.getChannel().sendMessage(embedReload.build()).queue();
                return;
            }
            embedReload.setTitle("Reloading...")
                    .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Color.orange);
            message.getChannel().sendMessage(embedReload.build()).queue(response -> {
                try {
                    Main.getConfigManager().reload();
                    embedReload.setTitle("Reload Complete")
                            .addField("Config reloaded", Main.getConfigManager().load().size() + " UUIDs stored",false)
                            .addField("Users list", guildInfo.getMembers().length + " Guild users",false)
                            .setColor(Color.green)
                            .setFooter("TragedyBOT v2.2 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
							.setTimestamp(new Date().toInstant())
                            .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl());
                    response.editMessage(embedReload.build()).queue();
                } catch(Exception exc) {
                    exc.printStackTrace();
                }
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
