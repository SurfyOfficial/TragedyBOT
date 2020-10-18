package surfy.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import surfy.bot.Main;
import surfy.managers.Command;
import surfy.utils.Emotes;
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

        if(!Utils.isActuallyMe(message.getAuthor().getId())) {
            return;
        }
        if(args[1].equalsIgnoreCase("send1")) {
            embedUpdates.setTitle("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬" +
                    "\n                                  **Version 1.1.7**" +
                    "\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬")
                    .setAuthor(botTag + " | 2020-10-12",botAvatarUrl,botAvatarUrl)
                    .setColor(Color.green)
                    .addField("**[Added]**",
                                    "\n**>blacklist** - New blacklist system. -*Admins*" +
                                    "\n**>g winslb** - New Guild's Bedwars wins LB. -*Admins*\n\n",false)
                    .addField("**[Removed]**",
                            "**Bugs** - Fixed lots of bots inner problems." +
                                    "\n**eGirls** - be gone.",false)
                    .setFooter("TragedyBOT v2.1 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
                    .setTimestamp(new Date().toInstant());
            message.getChannel().sendMessage(embedUpdates.build()).queue();
        }
        if(args[1].equalsIgnoreCase("send2")) {
            embedUpdates.setTitle("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬" +
                    "\n                                  **Version 2.0.0**" +
                    "\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬")
                    .setAuthor(botTag + " | 2020-10-13",botAvatarUrl,botAvatarUrl)
                    .setColor(Color.green)
                    .addField("**[Added]**",
                            "**!verify** - New verification system. -*New members*" +
                                    "\n**ChangeLogs!** - Made these new changelogs!",false)
                    .addField("**[Removed]**",
                            "**Recoded** - Recoded 85% of the BOT (everything but the applications.)" +
                                    "\n**Lags/delays** - Recoding made the BOT 65% **FASTER**.",false)
                    .setFooter("TragedyBOT v2.1 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
                    .setTimestamp(new Date().toInstant());
            message.getChannel().sendMessage(embedUpdates.build()).queue();
        }
        if(args[1].equalsIgnoreCase("send3")) {
            embedUpdates.setTitle("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬" +
                    "\n                                  **Version 2.0.1**" +
                    "\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬")
                    .setAuthor(botTag + " | 2020-10-13",botAvatarUrl,botAvatarUrl)
                    .setColor(Color.green)
                    .addField("**[Added]**",
                            "**!verify** - New verification system. -*New members*" +
                                    "\n**ChangeLogs!** - Made these new changelogs!",false)
                    .addField("**[Removed]**",
                            "**Recoded** - Recoded 85% of the BOT (everything but the applications.)" +
                                    "\n**Lags/delays** - Recoding made the BOT 65% **FASTER**." +
                                    "\n**BugFixing** - Fixed few bugs.",false)
                    .setFooter("TragedyBOT v2.1 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
                    .setTimestamp(new Date().toInstant());
            message.getChannel().sendMessage(embedUpdates.build()).queue();
        }
        if(args[1].equalsIgnoreCase("send4")) {
            embedUpdates.setTitle("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬" +
                    "\n                                  **Version 2.1**" +
                    "\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬")
                    .setAuthor(botTag + " | 2020-10-18",botAvatarUrl,botAvatarUrl)
                    .setColor(Color.green)
                    .addField("**[Added]**",
                            "**Reaction roles!** - New ReactionRoles. Check"+ Emotes.reactionChannel +" -*Everyone*",false)
                    .addField("**[Removed]**",
                                    "\n**BugFixing** - Fixed few bugs.",false)
                    .setFooter("TragedyBOT v2.1 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
                    .setTimestamp(new Date().toInstant());
            message.getChannel().sendMessage(embedUpdates.build()).queue();
        }
    }


}
