package surfy.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import surfy.bot.Main;
import surfy.managers.Command;
import surfy.utils.Emotes;
import surfy.utils.Utils;

import java.awt.*;
import java.util.Date;
import java.util.Objects;

public class ReactionRoleCommand extends Command {

    private static final String Prefix = ">g ";

    public ReactionRoleCommand() {
        super(Prefix + "reactionroles");
    }

    @Override
    public void onExecute(Message message, String[] args) {
        EmbedBuilder embedRoles = new EmbedBuilder();
        EmbedBuilder embedConfirm = new EmbedBuilder();
        if(message.getMember() != null && !Utils.isSurfy(message.getAuthor().getId()) & !Utils.isOfficer(message.getMember())) {
            embedRoles.setTitle("You don't have this permission.")
                    .setColor(Color.black)
                    .setFooter("TragedyBOT v2.1 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
					.setTimestamp(new Date().toInstant())
                    .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl());
            message.getChannel().sendMessage(embedRoles.build()).queue();
            return;
        }
        embedRoles.setColor(Color.red)
                .setFooter("TragedyRoles v2.1 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
				.setTimestamp(new Date().toInstant())
                .addField("**Roles:** ",":orange_heart:" + Emotes.orangeRoleMsg +
                                                "\n :yellow_heart:" + Emotes.yellowRoleMsg +
                                                "\n :green_heart:" + Emotes.greenRoleMsg +
                                                "\n :blue_heart:" + Emotes.blueRoleMsg +
                                                "\n :purple_heart:" + Emotes.purpleRoleMsg +
                                                "\n\n *React to get a color.* ", false);
        embedConfirm.setTitle("Done!")
				.setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl())
                .setFooter("TragedyBOT v2.1 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
				.setTimestamp(new Date().toInstant())
                .setColor(Color.green);

        message.getChannel().sendMessage(embedConfirm.build()).queue(msg -> msg.addReaction(Emotes.YES).queue());
        Objects.requireNonNull(Main.getJDiscordAPI().getTextChannelById("767057803419582476")).sendMessage(embedRoles.build()).queue(msg -> {
            msg.addReaction(Emotes.orangeHeart).queue();
            msg.addReaction(Emotes.yellowHeart).queue();
            msg.addReaction(Emotes.greenHeart).queue();
            msg.addReaction(Emotes.blueHeart).queue();
            msg.addReaction(Emotes.purpleHeart).queue();
        });
    }
}