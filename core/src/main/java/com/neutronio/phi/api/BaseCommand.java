package com.neutronio.phi.api;

/**
 * A base class for console commands
 */
public class BaseCommand implements CommandProcessor.Command {

    protected CommandProcessor.Output output = new CommandProcessor.Output();

    @Override
    public void execute(String... params) {
    }

    @Override
    public CommandProcessor.Output output() {
        return output;
    }

    @Override
    public String getUsage() {
        return null;
    }
}
