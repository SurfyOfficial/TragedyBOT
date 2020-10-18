package surfy.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import surfy.managers.Command;
import surfy.utils.Emotes;
import surfy.utils.Utils;

import java.awt.*;
import java.util.Date;
import java.util.Objects;

public class ApplyMessageCommand extends Command {

    private static final String Prefix = ">g ";

    public ApplyMessageCommand() {
        super(Prefix + "991714");
    }

    @Override
    public void onExecute(Message message, String[] args) {
        try {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            if (!Utils.isSurfy(message.getAuthor().getId()) & !Utils.isGuildMember(Objects.requireNonNull(message.getMember()))) {
                /* Checks if messageAuthor is a Guild Member. */
                embedBuilder.setTitle("Error! You must be a Guild Member in order to use this bot!")
                        .setColor(Color.red).setFooter("TragedyBOT v2.2 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
						.setTimestamp(new Date().toInstant())
                        .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(), message.getAuthor().getEffectiveAvatarUrl(), message.getAuthor().getEffectiveAvatarUrl());
                message.getChannel().sendMessage(embedBuilder.build()).queue();
                return;
            }

            EmbedBuilder embedPussy = new EmbedBuilder();
            if(!Utils.isSurfy(message.getAuthor().getId()) & !Utils.isOfficer(message.getMember())) {
                /* Checks if messageAuthor is a Guild Staff. */
                embedPussy.setTitle("You do not have this permission.");
                embedPussy.setColor(Color.red).setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl());
                embedPussy.setFooter("TragedyBOT v2.2 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
						.setTimestamp(new Date().toInstant())
                        .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl());
                message.getChannel().sendMessage(embedPussy.build()).queue();
                return;
            }
            message.getTextChannel().sendMessage("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬" +
                    "\n                        **APPLY FOR TRAGEDY**" +
                    "\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬" +
                    "\n\nReact here if you wish to apply for Tragedy Guild." +
                    "\nThen type **>apply** in the " + Emotes.applyChannel + " channel." +
                    "\n\n**Before applying, make sure that you're allowing DMS from server members!**")
                    .queue(msg -> msg.addReaction(Emotes.APPLY).queue());
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
