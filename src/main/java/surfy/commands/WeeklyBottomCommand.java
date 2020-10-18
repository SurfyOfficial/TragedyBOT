package surfy.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.utils.MarkdownSanitizer;
import surfy.API.APIManager;
import surfy.managers.Command;
import surfy.utils.IndexedMap;
import surfy.utils.Utils;

import java.awt.*;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Date;

public class WeeklyBottomCommand extends Command {

    private static final String Prefix = ">g ";

    public WeeklyBottomCommand() {
        super(Prefix + "weeklybottom");
    }

    @Override
    public void onExecute(Message message, String[] args) {
        try {
            NumberFormat weeklybottom = NumberFormat.getInstance(new Locale("da", "DK"));
            APIManager apiManager = new APIManager();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            EmbedBuilder embedweeklybottom = new EmbedBuilder();

            if (!Utils.isSurfy(message.getAuthor().getId()) & !Utils.isGuildMember(Objects.requireNonNull(message.getMember()))) {
                embedBuilder.setTitle("Error! You must be a Guild Member in order to use this bot!")
                        .setColor(Color.red).setFooter("TragedyBOT v2.1 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
						.setTimestamp(new Date().toInstant())
                        .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(), message.getAuthor().getEffectiveAvatarUrl(), message.getAuthor().getEffectiveAvatarUrl());
                message.getChannel().sendMessage(embedBuilder.build()).queue();
                return;
            }

            embedweeklybottom.setColor(Color.orange)
                    .setTitle("Calculating weekly experience...")
                    .setFooter("TragedyBOT v2.1 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
						.setTimestamp(new Date().toInstant())
                    .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl());
            message.getChannel().sendMessage(embedweeklybottom.build()).queue(response -> {
                HashMap<String,Integer> topPlayers = apiManager.getCurrentEXPSum();
                embedweeklybottom.setTitle("Weekly Bottom 10 GXP")
                        .setColor(Color.red)
                        .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl());
                topPlayers.values()
                        .stream()
                        .filter(Objects::nonNull)
                        .sorted(Comparator.naturalOrder())
                        .limit(10)
                        .map(IndexedMap.indexed())
                        .forEach(map ->{
                            String username = Utils.getKeyByValue(topPlayers,map.value());
                            topPlayers.remove(username);
                            embedweeklybottom.addField(Utils.indexToEmoji(map.index()) + MarkdownSanitizer.escape(username),weeklybottom.format(map.value()),false);
                        });
                response.editMessage(embedweeklybottom.build()).queue();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
