package surfy.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.utils.MarkdownSanitizer;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import surfy.API.*;
import surfy.managers.Command;
import surfy.utils.Utils;

import java.awt.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class MemberCommand extends Command {

    private static final String Prefix = ">g ";

    public MemberCommand() {
        super(Prefix + "member");
    }

    @Override
    public void onExecute(Message message, String[] args) {
        try {
            APIManager apiManager = new APIManager();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            EmbedBuilder embedMember = new EmbedBuilder();
            EmbedBuilder embedMemberInfo = new EmbedBuilder();
            StringBuilder stringBuilder = new StringBuilder();

            NumberFormat member = NumberFormat.getInstance(new Locale("da", "DK"));

            if (!Utils.isSurfy(message.getAuthor().getId()) & !Utils.isGuildMember(Objects.requireNonNull(message.getMember()))) {
                embedBuilder.setTitle("Error! You must be a Guild Member in order to use this bot!")
                        .setColor(Color.red).setFooter("TragedyBOT v2.1 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
						.setTimestamp(new Date().toInstant())
                        .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(), message.getAuthor().getEffectiveAvatarUrl(), message.getAuthor().getEffectiveAvatarUrl());
                message.getChannel().sendMessage(embedBuilder.build()).queue();
                return;
            }

            if(args.length < 2) {
                embedMemberInfo.setTitle(MarkdownUtil.italics("Type >g member <username>"))
                        .setColor(Color.cyan)
                        .setFooter("TragedyBOT v2.1 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
						.setTimestamp(new Date().toInstant())
                        .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl());
                message.getChannel().sendMessage(embedMemberInfo.build()).queue();
                return;
            }

            if(args[1].equalsIgnoreCase("member")) {
                String UUID = MojangAPI.getUUID(args[2],true);
                GuildMember guildMember = apiManager.getGuildMember(UUID);
                Player player = apiManager.getPlayer(args[2]);
                String pp = MojangAPI.getUsername(player.getUUID(),true);
                //Status sttt = apiManager.getStatus(player.getUUID());
                float val = Utils.expToLevel(player.getNetworkExp());
                int livello = (int) val;
                int percentuale = (int) ((val - livello) * 100);
                String percentualeFix = String.valueOf(percentuale).length() == 1 ? String.valueOf(percentuale * 10) : String.valueOf(percentuale);

                if(guildMember == null) {
                    embedMemberInfo.setTitle("Error! This user is not in the guild!")
                            .setFooter("TragedyBOT v2.1 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
							.setTimestamp(new Date().toInstant())
                            .setColor(Color.orange)
                            .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl());
                    message.getChannel().sendMessage(embedMemberInfo.build()).queue();
                    return;
                }

                embedMember.setTitle("Member - " + MarkdownSanitizer.escape(pp))
                        .setThumbnail("https://visage.surgeplay.com/full/256/" + player.getUUID())
                        .addField("Network Level", MarkdownUtil.monospace(String.valueOf(livello)) + " " + MarkdownUtil.monospace("(" + percentualeFix + "%)"), false)
                        .addField("Guild Rank", MarkdownUtil.monospace(guildMember.getRank()), true)
                        .addField("Join Date", MarkdownUtil.monospace(Utils.formatTimestamp(guildMember.getJoined())), true);
                AtomicLong weeklyExp = new AtomicLong(0);
                guildMember.getExpHistory().keySet()
                        .stream()
                        .sorted(Comparator.reverseOrder())
                        .forEach(key -> {
                            try {
                                weeklyExp.set(weeklyExp.get() + guildMember.getExpHistory().get(key));
                                stringBuilder.append(Utils.isToday(key) ? MarkdownUtil.bold("Today") : MarkdownUtil.bold(Objects.requireNonNull(Utils.formatDay(key))))
                                        .append(" - ")
                                        .append(MarkdownUtil
                                                .monospace(member
                                                        .format(guildMember.getExpHistory()
                                                                .get(key))))
                                        .append("\n");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        });
                embedMember.addField("Last GXP Contributions:", stringBuilder.toString(), false)
                        .addField("Weekly Contribution:", MarkdownUtil.monospace(member.format(weeklyExp.get())),false)
                        .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl());
                if (player.getLastLogin() > player.getLastLogout()) {
                    embedMember.setFooter("Online on | TragedyBOT v2.1 by ↬Surfy#0069", "https://i.imgur.com/OXO5HSW.png")
							.setTimestamp(new Date().toInstant());
                } else {
                    embedMember.addField("Last Login", MarkdownUtil.monospace(Utils.formatTimestamp(player.getLastLogin())), false)
                            .setFooter("Offline | TragedyBOT v2.1 by ↬Surfy#0069", "https://i.imgur.com/NNBWr17.png")
							.setTimestamp(new Date().toInstant());
                }
                message.getChannel().sendMessage(embedMember.build()).queue();


                Color c34 = new Color(223,89,89);
                Color c35 = new Color(235,184,65);
                Color c45 = new Color(51,204,51);
                Color c55 = new Color(255,255,51);
                Color c65 = new Color(255,102,255);
                Color c75 = new Color(255,255,255);
                Color c85 = new Color(0,102,204);
                Color c95 = new Color(51,102,0);
                Color c150 = new Color(102,0,0);
                Color c200 = new Color(64,64,64);
                Color c250 = new Color(0,0,0);
                if(livello <= 34) {
                    embedMember.setColor(c34);
                } else if(livello < 44) {
                    embedMember.setColor(c35);
                } else if((livello >= 45) & (livello < 54)) {
                    embedMember.setColor(c45);
                } else if((livello >= 55) & (livello < 64)) {
                    embedMember.setColor(c55);
                } else if((livello >= 65) & (livello < 74)) {
                    embedMember.setColor(c65);
                } else if((livello >= 75) & (livello < 84)) {
                    embedMember.setColor(c75);
                } else if((livello >= 85) & (livello < 94)) {
                    embedMember.setColor(c85);
                } else if((livello >= 95) & (livello < 149)) {
                    embedMember.setColor(c95);
                } else if((livello >= 150) & (livello < 199)) {
                    embedMember.setColor(c150);
                } else if((livello >= 200) & (livello < 249)) {
                    embedMember.setColor(c200);
                } else if(livello > 250) {
                    embedMember.setColor(c250);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
