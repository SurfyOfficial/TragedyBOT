package surfy.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import surfy.managers.Command;
import surfy.utils.ApplicationForm;
import surfy.utils.ConfigManager;
import surfy.utils.Utils;

public class ApplyCommand extends Command {

    public ApplyCommand() {
        super(">apply");
    }

    @Override
    public void onExecute(Message message, String[] syntax) {
        if(message.getMember() != null && !message.getMember().getRoles().contains(Utils.getApplicantRole())) {
            /* Checks if the user is an Applicant */
            message.getTextChannel().sendMessage(MarkdownUtil.bold(message.getAuthor().getName())
                    + " I'm sorry, you're not an applicant.\nIf you wish to apply check the <#688417186062401590> channel!").queue();
            return;
        }
        if(ConfigManager.getQueueApplications().containsKey(message.getAuthor().getIdLong())) {
            /* Checks if the user already started an application */
            message.getTextChannel().sendMessage(MarkdownUtil.bold(message.getAuthor().getName())
                    + " You already started an application, check your DMs!").queue();
        } else {
            /* Starts the application */
            message.getTextChannel().sendMessage("Okay " + MarkdownUtil.bold(message.getAuthor().getName()) + ", let's start the Application, check your DMs!").queue();
            ConfigManager.getQueueApplications().put(message.getAuthor().getIdLong(), new ApplicationForm());
            Utils.sendPrivateMessage(message.getAuthor(),"Hello, "+ MarkdownUtil.bold(message.getAuthor().getName())
                    +" Let's begin with the Application form!\nPlease answer our questions fully honestly.");
            ApplicationForm applicationForm = ConfigManager.getQueueApplications().get(message.getAuthor().getIdLong());
            Utils.sendPrivateMessage(message.getAuthor(),applicationForm.getPendingQuestion());
        }
    }
}
