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
import java.util.*;

public class TopCommand extends Command {

    private static final String Prefix = ">g ";

    public TopCommand() {
        super(Prefix + "top");
    }

    @Override
    public void onExecute(Message message, String[] args) {
        try {
            NumberFormat top = NumberFormat.getInstance(new Locale("da", "DK"));
            APIManager apiManager = new APIManager();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            EmbedBuilder embedTopPlayer = new EmbedBuilder();

            if (!Utils.isSurfy(message.getAuthor().getId()) & !Utils.isGuildMember(Objects.requireNonNull(message.getMember()))) {
                embedBuilder.setTitle("Error! You must be a Guild Member in order to use this bot!")
                        .setColor(Color.red)
						.setFooter("TragedyBOT v1.1 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
                        .setTimestamp(new Date().toInstant())
                        .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(), message.getAuthor().getEffectiveAvatarUrl(), message.getAuthor().getEffectiveAvatarUrl());
                message.getChannel().sendMessage(embedBuilder.build()).queue();
                return;
            }

            embedTopPlayer.setColor(Color.orange)
                    .setTitle("Calculating daily experience...")
                    .setFooter("TragedyBOT v2.1 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
                    .setTimestamp(new Date().toInstant())
                    .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(), message.getAuthor().getEffectiveAvatarUrl(), message.getAuthor().getEffectiveAvatarUrl());
            message.getChannel().sendMessage(embedTopPlayer.build()).queue(response -> {
                HashMap<String,Integer> topPlayers = apiManager.getCurrentEXP();
                embedTopPlayer.setTitle("Top 10 GXP");
                embedTopPlayer.setColor(Color.red);
                topPlayers.values()
                        .stream()
                        .filter(Objects::nonNull)
                        .sorted(Comparator.reverseOrder())
                        .limit(10)
                        .map(IndexedMap.indexed())
                        .forEach(map ->{
                            String username = Utils.getKeyByValue(topPlayers,map.value());
                            topPlayers.remove(username);
                            embedTopPlayer.addField(Utils.indexToEmoji(map.index()) + MarkdownSanitizer.escape(username),top.format(map.value()),false);
                        });
                response.editMessage(embedTopPlayer.build()).queue();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
