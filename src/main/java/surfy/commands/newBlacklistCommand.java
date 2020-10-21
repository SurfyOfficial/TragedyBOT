package surfy.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
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

    public void onExecute(Message message, String[] args) {
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
            List<String> lines = FileUtils.readLinesFromFile(new File("blacklist.txt"));
            assert lines != null;
            int totalUsers = lines.size();
            if(lines.contains(args[2])) {
                embedBlacklist.addField("**Error**","User: **" + args[2] + "** is already **Blacklisted**.",false);
                message.getChannel().sendMessage(embedBlacklist.build()).queue();
                return;
            }
            lines.add(args[2]);
            FileUtils.writeLinesToFile(new File("blacklist.txt"),lines);
            embedBlacklist.addField("**Success**","User: **" + args[2] + "** is now **Blacklisted**.",false);

            message.getChannel().sendMessage(embedBlacklist.build()).queue();
            //Objects.requireNonNull(message.getGuild().getTextChannelById("765221397760835595")).sendMessage((totalUsers + 1) + ") " + args[2]).queue(); //test bot
            Objects.requireNonNull(message.getGuild().getTextChannelById("693555959653597255")).sendMessage((totalUsers + 1) + ") " + args[2]).queue(); //normal bot
            return;
        }

        if(args[1].equalsIgnoreCase("remove")) {
            List<String> lines = FileUtils.readLinesFromFile(new File("blacklist.txt"));
            assert lines != null;
            int totalUsers = lines.size();
            if(!lines.contains(args[2])) {
                embedBlacklist.addField("**Error**","User: **" + args[2] + "** is not **Blacklisted**.",false);
                message.getChannel().sendMessage(embedBlacklist.build()).queue();
                return;
            }
            lines.remove(args[2]);
            FileUtils.writeLinesToFile(new File("blacklist.txt"),lines);
            embedBlacklist.addField("**Success**","User **" + args[2] + "** removed from **Blacklist**.",false);

            message.getChannel().sendMessage(embedBlacklist.build()).queue();
            //Objects.requireNonNull(message.getGuild().getTextChannelById("765221397760835595")).sendMessage(args[2]+"'s **Blacklist** got removed.").queue(); //test bot
            Objects.requireNonNull(message.getGuild().getTextChannelById("693555959653597255")).sendMessage(args[2]+"'s **Blacklist** got removed.").queue(); //normal bot
            return;
        }

        if(args[1].equalsIgnoreCase("check")) {
            List<String> lines = FileUtils.readLinesFromFile(new File("blacklist.txt"));
            assert lines != null;
            String ign = args[2];
            if(lines.contains(ign)) {
                embedBlacklist.addField("**Result**:", Emotes.msgYES + ign + " is **Blacklisted**. ",false);
            } else {
                embedBlacklist.addField("**Result**:",Emotes.msgNO + ign + " is not **Blacklisted**. ",false);
            }
            message.getChannel().sendMessage(embedBlacklist.build()).queue();
        }
    }
}
