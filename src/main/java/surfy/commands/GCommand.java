package surfy.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import surfy.managers.Command;
import surfy.utils.Emotes;
import surfy.utils.Utils;

import java.awt.*;
import java.util.*;

public class GCommand extends Command {

    private static final String Prefix = ">g";

    public GCommand() {
        super(Prefix);
    }

    @Override
    public void onExecute(Message message, String[] args) {
        try {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            EmbedBuilder embedAdmin = new EmbedBuilder();

            if (!Utils.isSurfy(message.getAuthor().getId()) & !Utils.isGuildMember(Objects.requireNonNull(message.getMember()))) {
                embedBuilder.setTitle("Error! You must be a Guild Member in order to use this bot!")
                        .setColor(Color.red).setFooter("TragedyBOT v2.1 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
                        .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(), message.getAuthor().getEffectiveAvatarUrl(), message.getAuthor().getEffectiveAvatarUrl());
                message.getChannel().sendMessage(embedBuilder.build()).queue();
                return;
            }

            if(args.length == 1 || args[1].equalsIgnoreCase("help")) {
                embedBuilder.setTitle("Commands")
                        .addField(MarkdownUtil.bold(">g"), MarkdownUtil.italics("Sends this message"), false)
                        .addField(MarkdownUtil.bold(">g top"), MarkdownUtil.italics("Sends the daily gxp leaderboard"), false)
                        .addField(MarkdownUtil.bold(">g info"), MarkdownUtil.italics("Sends all the guild's infos"), false)
                        .addField(MarkdownUtil.bold(">g admin"), MarkdownUtil.italics("Send the admin’s commands list"), false)
                        .addField(MarkdownUtil.bold(">g weeklytop"), MarkdownUtil.italics("Sends the weekly gxp leaderboard"), false)
                        .addField(MarkdownUtil.bold(">g weeklybottom"), MarkdownUtil.italics("Sends the weekly bottom gxp leaderboard"), false)
                        .addField(MarkdownUtil.bold(">g member [username]"), MarkdownUtil.italics("Shows a specific guild member's infos"), false)
                        .setFooter("TragedyBOT v2.1 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
						.setTimestamp(new Date().toInstant())
                        .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(), message.getAuthor().getEffectiveAvatarUrl(), message.getAuthor().getEffectiveAvatarUrl())
                        .setColor(Color.green);
                message.getChannel().sendMessage(embedBuilder.build()).queue();
                return;
            }

            if(args[1].equalsIgnoreCase("admin")) {
                if (!Utils.isSurfy(message.getAuthor().getId()) & !Utils.isOfficer(message.getMember())) {
                    embedAdmin.setTitle("You do not have this permission.")
                            .setColor(Color.red)
                            .setFooter("TragedyBOT v2.1 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
							.setTimestamp(new Date().toInstant())
                            .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(), message.getAuthor().getEffectiveAvatarUrl(), message.getAuthor().getEffectiveAvatarUrl());
                    message.getChannel().sendMessage(embedAdmin.build()).queue();
                    return;
                }
                embedAdmin.setTitle("Admins Commands")
                        .addField(MarkdownUtil.bold(">g winslb"), MarkdownUtil.italics("Sends the automatic wins lb in the " + Emotes.lbChannel + " channel."), false)
                        .addField(MarkdownUtil.bold(">g reload"), MarkdownUtil.italics("Reloads the users' config list."), false)
                        .addField(MarkdownUtil.bold(">g check [mode] [username]"), MarkdownUtil.italics("Look if a player has the requirements for the guild."), false)
                        .addField(MarkdownUtil.bold(">blacklist [IGN]"), MarkdownUtil.italics("Adds IGN in the " + Emotes.blChannel + " channel."), false)
                        .addField(MarkdownUtil.bold(">blacklist [IGN] @Member"), MarkdownUtil.italics("Removes guild's roles and adds IGN in the " + Emotes.blChannel + " channel."), false)
                        .setColor(Color.green)
                        .setFooter("TragedyBOT v2.1 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
						.setTimestamp(new Date().toInstant())
                        .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(), message.getAuthor().getEffectiveAvatarUrl(), message.getAuthor().getEffectiveAvatarUrl());
                message.getChannel().sendMessage(embedAdmin.build()).queue();
            }
        } catch(Exception exc) {
            exc.printStackTrace();
        }
    }
}
