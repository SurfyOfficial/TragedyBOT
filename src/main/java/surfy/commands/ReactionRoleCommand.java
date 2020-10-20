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
    private boolean found;

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
                    .setFooter(Main.version, Main.head)
					.setTimestamp(new Date().toInstant())
                    .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl());
            message.getChannel().sendMessage(embedRoles.build()).queue();
            return;
        }
        embedRoles.setFooter("TragedyRoles v2.3 by ↬Surfy#0069", Main.head)
				.setTimestamp(new Date().toInstant())
                .addField("**Roles:** ",":orange_heart:" + Emotes.orangeRoleMsg +
                                                "\n :yellow_heart:" + Emotes.yellowRoleMsg +
                                                "\n :green_heart:" + Emotes.greenRoleMsg +
                                                "\n :blue_heart:" + Emotes.blueRoleMsg +
                                                "\n :purple_heart:" + Emotes.purpleRoleMsg +
                                                "\n\n *React to get a color.* ", false);
        embedConfirm.setTitle("Done!")
				.setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl())
                .setFooter(Main.version, Main.head)
				.setTimestamp(new Date().toInstant())
                .setColor(Color.green);

        if(args.length == 3) {
            found = args[2].equalsIgnoreCase("white") ||
                    args[2].equalsIgnoreCase("black") ||
                    args[2].equalsIgnoreCase("pink") ||
                    args[2].equalsIgnoreCase("green") ||
                    args[2].equalsIgnoreCase("magenta") ||
                    args[2].equalsIgnoreCase("darkgray") ||
                    args[2].equalsIgnoreCase("lightGray") ||
                    args[2].equalsIgnoreCase("cyan") ||
                    args[2].equalsIgnoreCase("blue") ||
                    args[2].equalsIgnoreCase("orange") ||
                    args[2].equalsIgnoreCase("red");
            if(found) {
                if(args[2].equalsIgnoreCase("white")) embedRoles.setColor(Color.white);
                if(args[2].equalsIgnoreCase("red")) embedRoles.setColor(Color.red);
                if(args[2].equalsIgnoreCase("black")) embedRoles.setColor(Color.black);
                if(args[2].equalsIgnoreCase("pink")) embedRoles.setColor(Color.pink);
                if(args[2].equalsIgnoreCase("green")) embedRoles.setColor(Color.green);
                if(args[2].equalsIgnoreCase("magenta")) embedRoles.setColor(Color.magenta);
                if(args[2].equalsIgnoreCase("darkgray")) embedRoles.setColor(Color.darkGray);
                if(args[2].equalsIgnoreCase("lightGray")) embedRoles.setColor(Color.lightGray);
                if(args[2].equalsIgnoreCase("cyan")) embedRoles.setColor(Color.cyan);
                if(args[2].equalsIgnoreCase("blue")) embedRoles.setColor(Color.blue);
                if(args[2].equalsIgnoreCase("orange")) embedRoles.setColor(Color.orange);
            }
        }
        if(!found) {
            embedRoles.setColor(Color.black);
            embedConfirm.addField("","*Invalid Color, I selected Black for you.*",false);
        } else {
            embedConfirm.addField("Color:",args[2],false);
        }
        message.getChannel().sendMessage(embedConfirm.build()).queue();
        //message.getChannel().sendMessage(embedConfirm.build()).queue(msg -> msg.addReaction(Emotes.YES).queue());
        Objects.requireNonNull(Main.getJDiscordAPI().getTextChannelById("692198755360833616")).sendMessage(embedRoles.build()).queue(msg -> {
            msg.addReaction(Emotes.orangeHeart).queue();
            msg.addReaction(Emotes.yellowHeart).queue();
            msg.addReaction(Emotes.greenHeart).queue();
            msg.addReaction(Emotes.blueHeart).queue();
            msg.addReaction(Emotes.purpleHeart).queue();
        });
    }
}