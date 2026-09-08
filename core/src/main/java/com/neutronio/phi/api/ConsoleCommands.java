package com.neutronio.phi.api;

import java.util.Map;

/**
 * A commands object typically covers a single use case
 */
public interface ConsoleCommands {

    /**
     *
     * @return
     */
    Map<String, CommandProcessor.Command> getCommands();

    /**
     * Gets a help text of available commands in this object
     * @return
     */
    String getHelp();
}
