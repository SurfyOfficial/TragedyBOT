package surfy.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.utils.MarkdownSanitizer;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import surfy.API.APIManager;
import surfy.API.Player;
import surfy.managers.Command;
import surfy.modes.Bedwars;
import surfy.utils.Emotes;
import surfy.utils.Utils;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CheckCommand extends Command {

    private static final String Prefix = ">g ";

    public CheckCommand() {
        super(Prefix + "check");
    }

    @Override
    public void onExecute(Message message, String[] args) {
        try {
            APIManager apiManager = new APIManager();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            if (!Utils.isSurfy(message.getAuthor().getId()) & !Utils.isGuildMember(Objects.requireNonNull(message.getMember()))) {
                embedBuilder.setTitle("Error! You must be a Guild Member in order to use this bot!")
                        .setColor(Color.red).setFooter("TragedyBOT v2.2 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
						.setTimestamp(new Date().toInstant())
                        .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(), message.getAuthor().getEffectiveAvatarUrl(), message.getAuthor().getEffectiveAvatarUrl());
                message.getChannel().sendMessage(embedBuilder.build()).queue();
                return;
            }

            if(args.length < 4) {
                EmbedBuilder embedError = new EmbedBuilder();
                embedError.setTitle(MarkdownUtil.italics("Type >g check bw <username>"))
                        .setColor(Color.cyan)
                        .setFooter("TragedyBOT v2.2 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
						.setTimestamp(new Date().toInstant())
                        .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl());
                message.getChannel().sendMessage(embedError.build()).queue();
                return;
            }
            String gameType = args[2];
            String username = args[3];
            Player p = apiManager.getPlayer(username);
            Player tipo = apiManager.getPlayer(username);
            //Status status = apiManager.getStatus(p.getUUID());

            if(tipo == null) {
                EmbedBuilder embedMemberInfo = new EmbedBuilder();
                if(!Utils.isSurfy(message.getAuthor().getId()) & !Utils.isOfficer(message.getMember())) {
                    embedMemberInfo.setTitle("You do not have this permission.")
                            .setColor(Color.red)
                            .setFooter("TragedyBOT v2.2 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
							.setTimestamp(new Date().toInstant())
                            .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl());
                    message.getChannel().sendMessage(embedMemberInfo.build()).queue();
                    return;
                }
                embedMemberInfo.setTitle("Error! This user does not exist")
                        .setColor(Color.orange)
                        .setFooter("TragedyBOT v2.2 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
						.setTimestamp(new Date().toInstant())
                        .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl());
                message.getChannel().sendMessage(embedMemberInfo.build()).queue();
                return;
            }
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            NumberFormat req = NumberFormat.getInstance(new Locale("da", "DK"));
            switch(gameType.toLowerCase()) {
                case "bw":
                case "bedwars":
                    EmbedBuilder embedBedwars = new EmbedBuilder();
                    Bedwars bedwars = tipo.getBedwarsInfo();
                    double kdr_bw = bedwars.getKills() / bedwars.getDeaths();
                    int bwstar = Math.toIntExact(bedwars.getReqLevel());
                    double bwwins = bedwars.getWins();
                    if(!Utils.isSurfy(message.getAuthor().getId()) & !Utils.isOfficer(message.getMember())) {
                        embedBedwars.setTitle("You do not have this permission.")
                                .setColor(Color.red)
                                .setFooter("TragedyBOT v2.2 by ↬Surfy#0069", "http://i.imgur.com/W6wwYId.png")
								.setTimestamp(new Date().toInstant())
                                .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl());
                        message.getChannel().sendMessage(embedBedwars.build()).queue();
                        return;
                    }
                    apiManager.sendPrivateMessage(Objects.requireNonNull(message.getJDA().getUserById("247152228182392833")),
                            MarkdownUtil.bold(message.getAuthor().getName())
                                    + " just used:" + MarkdownUtil.italics(" [>g "
                                    + args[1]+" " + args[2]+" " + args[3]+"]")
                                    + " in the channel: " + MarkdownUtil.bold("#"
                                    + message.getChannel().getName()));
                    embedBedwars.setColor(Color.red)
                            .setThumbnail("https://visage.surgeplay.com/full/256/"+tipo.getUUID())
							.setTimestamp(new Date().toInstant())
                            .setTitle("Bedwars Stats - " + MarkdownSanitizer.escape(bedwars.getName()))
                            .addField("Stars",req.format(bedwars.getLevel()) + "✫",true)
                            .addField("Final KDR",String.valueOf(decimalFormat.format(kdr_bw)),true)
                            .addField("Wins",req.format(bwwins),true)
                            .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl());
                    if ((bwstar >= 300 & kdr_bw >= 6) || bwwins >= 3000) {
                        embedBedwars.addField("Approval", Emotes.msgYES,false);
                    } else {
                        embedBedwars.addField("Approval",Emotes.msgNO,false);
                    }

                    if (tipo.getLastLogin() > tipo.getLastLogout()) {
                        embedBedwars.setFooter("Online | TragedyBOT v2.2 by ↬Surfy#0069", "https://i.imgur.com/OXO5HSW.png")
								.setTimestamp(new Date().toInstant());
                    } else {
                        embedBedwars.setFooter("Offline | TragedyBOT v2.2 by ↬Surfy#0069", "https://i.imgur.com/NNBWr17.png")
								.setTimestamp(new Date().toInstant());
                    }
                    message.getChannel().sendMessage(embedBedwars.build()).queue();
                    break;
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
