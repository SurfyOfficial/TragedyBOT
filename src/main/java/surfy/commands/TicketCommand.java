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
import java.util.concurrent.TimeUnit;

public class TicketCommand extends Command {

    public TicketCommand() {
        super ("!ticket");
    }

    public void onExecute(Message message, String[] syntax) {
        if(!Utils.isOwner(Objects.requireNonNull(message.getMember()))) {
            EmbedBuilder embedTicket = new EmbedBuilder()
                    .setColor(Color.RED)
                    .setTitle("Need help?")
                    .addField("We're here to help!", "You can open a **ticket** in the channel <#774208888081874984>!",false)
                    .setFooter(message.getAuthor().getAsTag(),message.getAuthor().getEffectiveAvatarUrl())
                    .setTimestamp(new Date().toInstant());
            message.getChannel().sendMessage(embedTicket.build()).queue();
            return;
        }
        if(syntax.length > 1) {
            if(syntax[1].equalsIgnoreCase("createmsg")) {
                EmbedBuilder embedTicketMsg = new EmbedBuilder()
                        .setColor(Color.RED)
                        .setTitle("Need help?")
                        .addField("We're here to help!", "You can open a **ticket** by clicking the " + Emotes.ticketEmoji + " Reaction!\nWe will answer you as soon as possible!",false)
                        .setFooter(Main.version, Main.head)
                        .setTimestamp(new Date().toInstant());
                Objects.requireNonNull(Main.getJDiscordAPI().getTextChannelById("774208888081874984")).sendMessage(embedTicketMsg.build()).queue(msg -> msg.addReaction(Emotes.ticketReaction).queue());
                message.delete().queueAfter(50, TimeUnit.MILLISECONDS);
            }
        }
    }
}