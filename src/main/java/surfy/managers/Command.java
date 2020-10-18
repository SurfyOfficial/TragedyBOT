package surfy.managers;


import net.dv8tion.jda.api.entities.Message;

public class Command {
    private String command;

    public Command(String command){
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void onExecute(Message message,String[] syntax){
    }
}
