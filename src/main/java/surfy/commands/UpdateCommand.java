package surfy.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import surfy.bot.Main;
import surfy.managers.Command;
import surfy.utils.Utils;

import java.awt.*;
import java.util.Date;

public class UpdateCommand extends Command {

    public UpdateCommand() {
        super(">update");
    }

    @Override
    public void onExecute(Message message, String[] args) {

        EmbedBuilder embedUpdates = new EmbedBuilder();
        String botTag = Main.getJDiscordAPI().getSelfUser().getAsTag();
        String botAvatarUrl = Main.getJDiscordAPI().getSelfUser().getEffectiveAvatarUrl();
        String gitHubUrl = "https://github.com/SurfyOfficial/TragedyBOT";

        if(!Utils.isActuallyMe(message.getAuthor().getId())) {
            return;
        }
        if(args[1].equalsIgnoreCase("send1")) {
            embedUpdates.setTitle("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬" +
                    "\n                                     **Version 2.4.1**" +
                    "\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬")
                    .setAuthor(botTag + " | 2020-11-06",gitHubUrl,botAvatarUrl)
                    .setColor(Color.green)
                    .addField("**[Added]**",
                                    "\n**Ticket System**" +
                                            "\n- New TicketSystem for support! <#774208888081874984> -*everyone*.",false)
                    .addField("**[Removed]**",
                            "**Bugs** " +
                                    "\n- Bugs Fix",false)
                    .setFooter(Main.version, Main.head)
                    .setTimestamp(new Date().toInstant());
            message.getChannel().sendMessage(embedUpdates.build()).queue();
        }
    }


}
