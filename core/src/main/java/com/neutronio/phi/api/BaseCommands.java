package com.neutronio.phi.api;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Base class for a group of commands, on a per use case basis.
 */
public class BaseCommands implements ConsoleCommands  {
    protected Map<String, CommandProcessor.Command> commands = new LinkedHashMap<>();

    @Override
    public Map<String, CommandProcessor.Command> getCommands() {
        return this.commands;
    }

    @Override
    public String getHelp() {
        StringBuilder help = new StringBuilder();
        for(CommandProcessor.Command command : this.commands.values()) {
            help.append(command.getUsage()).append("\n");
        }
        return help.toString();
    }
}
