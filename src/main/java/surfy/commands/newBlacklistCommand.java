package surfy.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.utils.MarkdownSanitizer;
import net.dv8tion.jda.api.utils.MarkdownUtil;
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
        EmbedBuilder embedConfirm = new EmbedBuilder();
        EmbedBuilder embedUsg = new EmbedBuilder();
        embedUsg.setColor(Color.red)
                .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),
                message.getAuthor().getEffectiveAvatarUrl()).setTitle("**Usage** » >blacklist add/remove/check/list [IGN]")
                .setFooter(Main.version, Main.head);

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
                embedBlacklist.addField("**Blacklisted people who can no longer join Tragedy:**",builder.toString().substring(0,builder.toString().length() - 2),false)
                        .addField("**Total**", totalUsers + " users.",false)
                        .setFooter(Main.version, Main.head);

                message.getChannel().sendMessage(embedBlacklist.build()).queue();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return;
        }

        if(args.length < 3) {
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
                    embedBlacklist.addField("**Error**", "User: **" + MarkdownSanitizer.escape(playerName) + "** is already **Blacklisted**.", false)
                            .setFooter(Main.version, Main.head);
                    message.getChannel().sendMessage(embedBlacklist.build()).queue();
                    return;
                }
                lines.add(playerName);
                FileUtils.writeLinesToFile(new File("blacklist.txt"), lines);
                embedBlacklist.addField("**Success**", "User: **" + MarkdownSanitizer.escape(playerName) + "** is now **Blacklisted**.", false)
                        .setFooter(Main.version, Main.head);

                embedConfirm.setColor(Color.red)
                        .setTitle(MarkdownSanitizer.escape((totalUsers + 1) + ") " + playerName))
                        .setFooter(Main.version, Main.head)
                        .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl());
                if(args.length > 3) {
                    StringBuilder reason = new StringBuilder();
                    for(int i = 3;i<args.length;++i) {
                        reason.append(args[i]).append(" ");
                    }
                    embedBlacklist.addField("Reason:", MarkdownUtil.italics(reason.toString()),false);
                    embedConfirm.addField("Reason:", MarkdownUtil.italics(reason.toString()),false);
                } else {
                    embedBlacklist.addField("Reason:", "*Not specified.*",false);
                    embedConfirm.addField("Reason:","*Not specified.*",false);
                }

                message.getChannel().sendMessage(embedBlacklist.build()).queue();

                Objects.requireNonNull(message.getGuild().getTextChannelById("765221397760835595")).sendMessage(embedConfirm.build()).queue(); //test bot
                //Objects.requireNonNull(message.getGuild().getTextChannelById("693555959653597255")).sendMessage(embedConfirm.build()).queue(); //normal bot
                return;
            } catch (NullPointerException e) {
                embedBlacklist.addField("**Error**","User: **" + MarkdownSanitizer.escape(args[2]) + "** does not exist!",false)
                        .setFooter(Main.version, Main.head);
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
                    embedBlacklist.addField("**Error**","User: **" + MarkdownSanitizer.escape(playerName) + "** is not **Blacklisted**.",false)
                            .setFooter(Main.version, Main.head);
                    message.getChannel().sendMessage(embedBlacklist.build()).queue();
                    return;
                }
                lines.remove(playerName);
                FileUtils.writeLinesToFile(new File("blacklist.txt"),lines);
                embedBlacklist.setColor(Color.green)
                        .addField("**Success**","User **" + playerName + "** removed from **Blacklist**.",false)
                        .setFooter(Main.version, Main.head);

                embedConfirm.setColor(Color.green)
                        .setTitle(MarkdownSanitizer.escape(playerName) + "'s Blacklist got removed.")
                        .setFooter(Main.version, Main.head)
                        .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl());
                if(args.length > 3) {
                    StringBuilder reason = new StringBuilder();
                    for(int i = 3;i<args.length;++i) {
                        reason.append(args[i]).append(" ");
                    }
                    embedBlacklist.addField("Reason:", MarkdownUtil.italics(reason.toString()),false);
                    embedConfirm.addField("Reason:", MarkdownUtil.italics(reason.toString()),false);
                } else {
                    embedBlacklist.addField("Reason:", "*Not specified.*",false);
                    embedConfirm.addField("Reason:","*Not specified.*",false);
                }

                message.getChannel().sendMessage(embedBlacklist.build()).queue();

                Objects.requireNonNull(message.getGuild().getTextChannelById("765221397760835595")).sendMessage(embedConfirm.build()).queue(); //test bot
                //Objects.requireNonNull(message.getGuild().getTextChannelById("693555959653597255")).sendMessage(embedConfirm.build()).queue(); //normal bot
                return;
            } catch (NullPointerException e) {
                embedBlacklist.addField("**Error**","User: **" + MarkdownSanitizer.escape(args[2]) + "** does not exist!",false)
                        .setFooter(Main.version, Main.head);
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
                    embedBlacklist.addField("**Result**:", Emotes.msgYES + " " + MarkdownSanitizer.escape(playerName) + " is **Blacklisted**. ",false)
                            .setFooter(Main.version, Main.head);
                } else {
                    embedBlacklist.addField("**Result**:",Emotes.msgNO + " " + MarkdownSanitizer.escape(playerName) + " is not **Blacklisted**. ",false)
                            .setFooter(Main.version, Main.head);
                }
                message.getChannel().sendMessage(embedBlacklist.build()).queue();
            } catch (NullPointerException e) {
                embedBlacklist.addField("**Error**","User: **" + MarkdownSanitizer.escape(args[2]) + "** does not exist!",false);
                message.getChannel().sendMessage(embedBlacklist.build()).queue();
            }
        }
    }
}
