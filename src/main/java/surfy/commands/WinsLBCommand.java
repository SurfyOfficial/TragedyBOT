package surfy.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import surfy.API.APIManager;
import surfy.bot.Main;
import surfy.managers.Command;
import surfy.utils.Emotes;
import surfy.utils.IndexedMap;
import surfy.utils.Utils;

import java.awt.*;
import java.util.*;

public class WinsLBCommand extends Command {

    private static final String Prefix = ">g ";

    public WinsLBCommand() {
        super(Prefix + "winslb");
    }

    @Override
    public void onExecute(Message message, String[] args) {
        try {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            EmbedBuilder embedTop = new EmbedBuilder();

            if (!Utils.isSurfy(message.getAuthor().getId()) & !Utils.isGuildMember(Objects.requireNonNull(message.getMember()))) {
                embedBuilder.setTitle("Error! You must be a Guild Member in order to use this bot!")
                        .setColor(Color.red).setFooter("TragedyBOT v2.1 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
						.setTimestamp(new Date().toInstant())
                        .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(), message.getAuthor().getEffectiveAvatarUrl(), message.getAuthor().getEffectiveAvatarUrl());
                message.getChannel().sendMessage(embedBuilder.build()).queue();
                return;
            }

            if(args[1].equalsIgnoreCase("winslb")) {
                if(!Utils.isSurfy(message.getAuthor().getId()) & !Utils.isOfficer(message.getMember())) {
                    embedTop.setTitle("You do not have this permission.");
                    embedTop.setColor(Color.red);
                    embedTop.setFooter("TragedyBOT v2.1 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
							.setTimestamp(new Date().toInstant())
                            .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl());
                    message.getChannel().sendMessage(embedTop.build()).queue();
                    return;
                }
                EmbedBuilder embedConfirm = new EmbedBuilder();
                embedConfirm.setTitle("Successfully created a new Leaderboard!")
                        .addField("Note: This is an automatic leaderboard. **Don't create copies!!**","Check it in the " + Emotes.lbChannel + " channel.",false)
                        .setColor(Color.green)
                        .setFooter("TragedyBOT v2.1 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
						.setTimestamp(new Date().toInstant())
                        .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl());
                message.getChannel().sendMessage(embedConfirm.build()).queue(msg -> msg.addReaction(Emotes.YES).queue());

                embedTop.setTitle("Calculating wins leaderboard...")
						.setFooter("TragedyBOT v2.1 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
						.setTimestamp(new Date().toInstant())
						.setColor(Color.orange);
                Timer timer = new Timer();

                //Objects.requireNonNull(Main.getJDiscordAPI().getTextChannelById("765214168085168158")).sendMessage(embedTop.build()).queue(response -> { //test bot
                Objects.requireNonNull(Main.getJDiscordAPI().getTextChannelById("765207590060818452")).sendMessage(embedTop.build()).queue(response -> { //normal bot
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            try {
                                embedTop.clear();
                                embedTop.setTitle("Guild's BedWars Wins - Leaderboard");
                                embedTop.setColor(Color.green);
                                (new APIManager()).getWinsLeaderboard()
                                        .stream()
                                        .map(IndexedMap.indexed())
                                        .forEach(data -> {
                                            String bwWins = data.value().getBwWins();
                                            String totalWins = data.value().getTotalWins();
                                            //String[] levels = data.value().getLevel().split("\\.");
                                            //String lvlPercentage = levels[1].length() == 1 ? String.valueOf(Integer.parseInt(levels[1]) * 10) : levels[1];
                                            String exp = data.value().getExp();
                                            embedTop.addField(Utils.indexToEmoji(data.index()) + MarkdownUtil.bold(data.value().getName()),
                                                    MarkdownUtil.bold("Bedwars Wins » ") + bwWins +
                                                            MarkdownUtil.bold("\nTotal Wins » ") + totalWins +
                                                            //MarkdownUtil.bold("\nLevel » ") + levels[0] + " ("+lvlPercentage+"%)" +
                                                            MarkdownUtil.bold("\nExp » ") + exp, false);
                                            embedTop.setFooter("TragedyBOT v2.1 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
													.setTimestamp(new Date().toInstant());
                                        });
                                //embedTop.addField("Last update:",dateFormat.format(date) + " - " + hourFormat.format(date)+" (EST).",false);
                                response.editMessage(embedTop.build()).queue();
                                System.out.println("TragedyBOT: WLeaderboard updated. +" + new Date().toInstant());
                            } catch(Exception exc) {
                                exc.printStackTrace();
                            }
                        }
                    },0, 60 * 30000);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}