package surfy.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import surfy.bot.Main;
import surfy.managers.Command;
import surfy.utils.Emotes;
import surfy.utils.Utils;

import java.awt.*;
import java.util.Date;

public class VerifyCommand extends Command {

    public VerifyCommand() {
        super("!verify");
    }

    @Override
    public void onExecute(Message message, String[] syntax) {
        EmbedBuilder embedVerify = new EmbedBuilder();
        if(syntax.length > 1) {
            embedVerify.setTitle("Usage » !verify")
                    .setColor(Color.black)
                    .setFooter(Main.version, Main.head);
            message.getChannel().sendMessage(embedVerify.build()).queue();
            return;
        }

        if(message.getMember() != null && !Utils.isVerified(message.getMember())) {
            if(message.getChannel() == Main.getJDiscordAPI().getTextChannelById("714952107333124216")) {
                embedVerify.setTitle("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬" +
                        "\n                        **READ HERE TO VERIFY**" +
                        "\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬")
                        .addField("This is very simple!","In order to verify you have to click the " + Emotes.msgYES + " reaction!",false)
                        .setColor(Color.yellow)
                        .setAuthor(message.getAuthor().getName(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl())
                        .setFooter(Main.version, Main.head)
						.setTimestamp(new Date().toInstant());
                message.getChannel().sendMessage(embedVerify.build()).queue(msg -> msg.addReaction(Emotes.YES).queue());
                message.getGuild().addRoleToMember(message.getAuthor().getId(),Utils.getUnverifiedRole()).queue();
            } else {
                embedVerify.setTitle("Error!").addField("You can't verify here.", "You have to verify in the "+ Emotes.verifyChannel + " channel.",false)
                        .setColor(Color.red)
                        .setFooter(Main.version, Main.head)
						.setTimestamp(new Date().toInstant())
                        .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl());
                message.getChannel().sendMessage(embedVerify.build()).queue();
            }
        } else {
            embedVerify.setTitle("You're already verified.")
                    .setColor(Color.red)
                    .setFooter(Main.version, Main.head)
						.setTimestamp(new Date().toInstant())
                    .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl());
            message.getChannel().sendMessage(embedVerify.build()).queue();
        }
    }
}
