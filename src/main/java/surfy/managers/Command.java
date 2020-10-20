package surfy.managers;


import net.dv8tion.jda.api.entities.Message;

import java.io.IOException;

public class Command {
    
    private String command;

    public Command(String command) {
        /* Casting the command to a string */
        this.command = command;
    }

    public String getCommand() {
        /* Command Getter */
        return command;
    }

    public void onExecute(Message message,String[] syntax) throws IOException {
        /* Command Base */
    }
}
