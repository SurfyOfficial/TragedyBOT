package surfy.managers;

import surfy.commands.*;

import java.util.LinkedHashSet;

public class CommandsManager {

    public static LinkedHashSet<Command> commands = new LinkedHashSet<>();

    public CommandsManager() {
        getCommands().add(new ApplyCommand());
        getCommands().add(new ApplyMessageCommand());
        getCommands().add(new CheckCommand());
        getCommands().add(new GCommand());
        getCommands().add(new InfoCommand());
        getCommands().add(new MemberCommand());
        getCommands().add(new ReloadCommand());
        getCommands().add(new TopCommand());
        getCommands().add(new UpdateCommand());
        getCommands().add(new VerifyCommand());
        getCommands().add(new WeeklyBottomCommand());
        getCommands().add(new WeeklyTopCommand());
        getCommands().add(new WinsLBCommand());
        getCommands().add(new ReactionRoleCommand());
        //getCommands().add(new oldBlacklistCommand());
        getCommands().add(new newBlacklistCommand());
    }

    public static LinkedHashSet<Command> getCommands() {
        return commands;
    }

    @SuppressWarnings("unused")
    public Command getCommandByClass(Class<Command> commandClass){
        return getCommands()
                .stream()
                .filter(command -> command.getClass().equals(commandClass))
                .findFirst()
                .get();
    }
}
