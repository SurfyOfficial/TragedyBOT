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
                    "\n                                     **Version 2.2**" +
                    "\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬")
                    .setAuthor(botTag + " | 2020-10-19",gitHubUrl,botAvatarUrl)
                    .setColor(Color.green)
                    .addField("**[Added]**",
                                    "\n**GitHub Repository!**" +
                                            "\n- The **BOT SourceCode** can now be found in **GitHub**!" +
                                            "\n*https://github.com/SurfyOfficial/TragedyBOT*\n",false)
                    .addField("**[Removed]**",
                            "**Bugs** " +
                                    "- Bugs Fix",false)
                    .setFooter(Main.version, Main.head)
                    .setTimestamp(new Date().toInstant());
            message.getChannel().sendMessage(embedUpdates.build()).queue();
        }
    }


}
