package surfy.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import surfy.API.MojangAPI;
import surfy.bot.Main;
import surfy.managers.Command;
import surfy.utils.Emotes;
import surfy.utils.FileUtils;
import surfy.utils.Utils;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class newBlacklistCommand extends Command {

    public newBlacklistCommand() {
        super(">blacklist");
    }

    public void onExecute(Message message, String[] args) throws Exception {
        if(message.getMember() != null && !Utils.isSurfy(message.getAuthor().getId()) & !Utils.isOfficer(message.getMember())) {
            EmbedBuilder embedPerms = new EmbedBuilder();
            /* Checks if messageAuthor is a Guild Staff. */
            embedPerms.setTitle("You don't have this permission.")
                    .setColor(Color.black)
                    .setFooter(Main.version, Main.head)
                    .setTimestamp(new Date().toInstant())
                    .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl());
            message.getChannel().sendMessage(embedPerms.build()).queue();
            return;
        }
        EmbedBuilder embedBlacklist = new EmbedBuilder();
        EmbedBuilder embedUsg = new EmbedBuilder();
        embedUsg.setColor(Color.red)
                .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),
                message.getAuthor().getEffectiveAvatarUrl()).setTitle("**Usage** » >blacklist add/remove/check/list [IGN]");

        embedBlacklist.setColor(Color.red)
                .setFooter(Main.version,Main.head)
                .setTimestamp(new Date().toInstant())
                .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl());

        if(args.length == 1) {
            message.getChannel().sendMessage(embedUsg.build()).queue();
            return;
        }
        if(args[1].equalsIgnoreCase("list")) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("blacklist.txt")), StandardCharsets.UTF_8));
                StringBuilder builder = new StringBuilder();
                List<String> lines = reader.lines().collect(Collectors.toList());
                int totalUsers = lines.size();

                lines.forEach(line -> builder.append(line).append(", "));
                embedBlacklist.addField("**Blacklisted people who can no longer join Tragedy:**",builder.toString().substring(0,builder.toString().length() - 1),false)
                        .addField("**Total**", totalUsers + " users.",false);

                message.getChannel().sendMessage(embedBlacklist.build()).queue();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return;
        }

        if(args.length < 3 || args.length > 4) {
            message.getChannel().sendMessage(embedUsg.build()).queue();
            return;
        }

        if(args[1].equalsIgnoreCase("add")) {
            try {
                String UUID = MojangAPI.getUUID(args[2]);
                String playerName = MojangAPI.getUsername(UUID);
                List<String> lines = FileUtils.readLinesFromFile(new File("blacklist.txt"));
                assert lines != null;
                int totalUsers = lines.size();
                if (lines.contains(playerName)) {
                    embedBlacklist.addField("**Error**", "User: **" + playerName + "** is already **Blacklisted**.", false);
                    message.getChannel().sendMessage(embedBlacklist.build()).queue();
                    return;
                }
                lines.add(playerName);
                FileUtils.writeLinesToFile(new File("blacklist.txt"), lines);
                embedBlacklist.addField("**Success**", "User: **" + playerName + "** is now **Blacklisted**.", false);

                message.getChannel().sendMessage(embedBlacklist.build()).queue();
                //Objects.requireNonNull(message.getGuild().getTextChannelById("765221397760835595")).sendMessage((totalUsers + 1) + ") " + playerName).queue(); //test bot
                Objects.requireNonNull(message.getGuild().getTextChannelById("693555959653597255")).sendMessage((totalUsers + 1) + ") " + playerName).queue(); //normal bot
                return;
            } catch (NullPointerException e) {
                embedBlacklist.addField("**Error**","User: **" + args[2] + "** does not exist!",false);
                message.getChannel().sendMessage(embedBlacklist.build()).queue();
                return;
            }
        }

        if(args[1].equalsIgnoreCase("remove")) {
            try {
                String UUID = MojangAPI.getUUID(args[2]);
                String playerName = MojangAPI.getUsername(UUID);
                List<String> lines = FileUtils.readLinesFromFile(new File("blacklist.txt"));
                assert lines != null;
                if(!lines.contains(playerName)) {
                    embedBlacklist.addField("**Error**","User: **" + playerName + "** is not **Blacklisted**.",false);
                    message.getChannel().sendMessage(embedBlacklist.build()).queue();
                    return;
                }
                lines.remove(playerName);
                FileUtils.writeLinesToFile(new File("blacklist.txt"),lines);
                embedBlacklist.addField("**Success**","User **" + playerName + "** removed from **Blacklist**.",false);

                message.getChannel().sendMessage(embedBlacklist.build()).queue();
                //Objects.requireNonNull(message.getGuild().getTextChannelById("765221397760835595")).sendMessage(playerName+"'s **Blacklist** got removed.").queue(); //test bot
                Objects.requireNonNull(message.getGuild().getTextChannelById("693555959653597255")).sendMessage(playerName+"'s **Blacklist** got removed.").queue(); //normal bot
                return;
            } catch (NullPointerException e) {
                embedBlacklist.addField("**Error**","User: **" + args[2] + "** does not exist!",false);
                message.getChannel().sendMessage(embedBlacklist.build()).queue();
                return;
            }
        }

        if(args[1].equalsIgnoreCase("check")) {
            try {
                String UUID = MojangAPI.getUUID(args[2]);
                String playerName = MojangAPI.getUsername(UUID);
                List<String> lines = FileUtils.readLinesFromFile(new File("blacklist.txt"));
                assert lines != null;
                if(lines.contains(playerName)) {
                    embedBlacklist.addField("**Result**:", Emotes.msgYES + " " + playerName + " is **Blacklisted**. ",false);
                } else {
                    embedBlacklist.addField("**Result**:",Emotes.msgNO + " " + playerName + " is not **Blacklisted**. ",false);
                }
                message.getChannel().sendMessage(embedBlacklist.build()).queue();
            } catch (NullPointerException e) {
                embedBlacklist.addField("**Error**","User: **" + args[2] + "** does not exist!",false);
                message.getChannel().sendMessage(embedBlacklist.build()).queue();
            }
        }
    }
}
