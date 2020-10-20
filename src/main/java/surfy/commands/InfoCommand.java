package surfy.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import surfy.API.APIManager;
import surfy.API.GuildMember;
import surfy.API.GuildTragedy;
import surfy.bot.Main;
import surfy.managers.Command;
import surfy.utils.Utils;

import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.*;

public class InfoCommand extends Command {

    private static final String Prefix = ">g ";

    public InfoCommand() {
        super(Prefix + "info");
    }

    @Override
    public void onExecute(Message message, String[] args) {
        try {
            APIManager apiManager = new APIManager();
            GuildTragedy guildInfo = apiManager.getGuild();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            EmbedBuilder embedInfo = new EmbedBuilder();

            if (!Utils.isSurfy(message.getAuthor().getId()) & !Utils.isGuildMember(Objects.requireNonNull(message.getMember()))) {
                embedBuilder.setTitle("Error! You must be a Guild Member in order to use this bot!")
                        .setColor(Color.red).setFooter(Main.version, Main.head)
						.setTimestamp(new Date().toInstant())
                        .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(), message.getAuthor().getEffectiveAvatarUrl(), message.getAuthor().getEffectiveAvatarUrl());
                message.getChannel().sendMessage(embedBuilder.build()).queue();
                return;
            }

            String level = apiManager.getLevel()[0];
            String tempPercentage = apiManager.getLevel()[1];
            String levelPercentage = tempPercentage.length() == 1 ? String.valueOf(Integer.parseInt(tempPercentage) * 10) : tempPercentage;
            NumberFormat expFormat = NumberFormat.getInstance(new Locale("da", "DK"));
            List<GuildMember> guildMemberList = Arrays.asList(guildInfo.getMembers());

            embedInfo.setTitle("Info")
                    .setColor(Color.red)
                    .addField("Creation",guildInfo.getCreated(),false)
                    .addField("Guild EXP",expFormat.format(guildInfo.getExp()),true)
                    .addField("Guild Level", level + " ("+levelPercentage+"%)",true)
                    .addField("Guild Rank", "#"+ apiManager.getLbRank(),true)
                    .addField("Members",String.valueOf(guildMemberList.size()),true)
                    .addField("Online","Counting players...",true)
                    .setFooter(Main.version, Main.head)
					.setTimestamp(new Date().toInstant())
                    .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl());
            message.getChannel().sendMessage(embedInfo.build()).queue(response -> {
                try {
                    String onlinePlayers = String.valueOf(guildInfo.getOnlinePlayers());
                    embedInfo.clearFields()
                            .addField("Creation",guildInfo.getCreated(),false)
                            .addField("Guild EXP",expFormat.format(guildInfo.getExp()),true)
                            .addField("Guild Level", level + " ("+levelPercentage+"%)",true)
                            .addField("Guild Rank", "#"+ apiManager.getLbRank(),true)
                            .addField("Members",String.valueOf(guildMemberList.size()),true)
                            .addField("Online",onlinePlayers,true);
                    response.editMessage(embedInfo.build()).queue();
                } catch(Exception exc){
                    exc.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
