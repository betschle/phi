package com.neutronio.phi.api;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Allows the user to invoke String Commands that can be hooked to game logic.
 * Can also be used to script events or test APIs.
 */
public class CommandProcessor {
    private boolean verbose = true;
    private Map<String, Command> commands = new LinkedHashMap<>();
    /** Contains command output, valid for the execution of the last command*/
    private Output output = new Output();
    private StringBuilder helperText = new StringBuilder();

    public static class Output {
        /** The status of execution:
         *         <p>0 for command not executed (this is reserved by the command processor and shall be avoided in commands)
         *         <p>1 for command executed
         *         <p>-1 for parse error
         *         <p>-2 for invalid amount of parameters
         *         <p><-3 for other errors, defined by the respective command
         **/
        public int status;
        /** the status message */
        public String message;
        /** Returned output payload (optional)*/
        public Object payload;

        public void setAsInvalidArguments(int expected) {
            this.status = -2;
            this.message = "Invalid amount of parameters!";
            this.payload = expected;
        }

        public void setAsParseError(Exception exception) {
            this.status = -1;
            this.message = "Parse Error";
            this.payload = exception;
        }

        /**
         * Default error with error code -3
         * @param message
         * @param exception
         */
        public void setAsError(String message, Exception exception) {
            this.status = -3;
            this.message = message;
            this.payload = exception;
        }

        /**
         * Error with custom error status
         * @param message
         * @param status
         * @param exception
         */
        public void setAsError(String message, int status, Exception exception) {
            this.status = status;
            this.message = message;
            this.payload = exception;
        }

        public void setAsSuccess() {
            this.setAsSuccess(null);
        }

        public void setAsSuccess(Object payload) {
            this.status = 1;
            this.message = "Command executed successfully";
            this.payload = payload;
        }
    }

    public interface Command {
        /**
         * Executes this command with given String parameters.
         * @param params a list of input parameters. These will have to be parsed by your command implementation.
         *
         */
        void execute(String... params);

        /**
         * Obtains the output object. Always returns the same output object instance. The content of this object
         * changes on a per-execution basis.
         * @return
         */
        Output output();

        /**
         * Gets a usage hint for this command, a short description and information about its parameters.
         * @return
         */
        String getUsage();
    }

    /**
     * Always returns the same output object instance. The content of this object
     * changes on a per-execution basis.
     * @return
     */
    public Output getOutput() {
        return output;
    }

    public Command removeCommand(String commandCall) {
        return this.commands.remove(commandCall);
    }

    public void removeCommands(Map<String, Command> commands) {
        if(commands == null) return;
        for(String command : commands.keySet()) {
            this.commands.remove(command);
        }
    }

    public void addCommand(String commandCall, Command command) {
        this.commands.put(commandCall, command);
        this.helperText.delete(0, this.helperText.length());
    }

    public void addCommands(Map<String, Command> commands) {
        if(commands == null) return;
        this.commands.putAll(commands);
        this.helperText.delete(0, this.helperText.length());
    }

    /**
     * Parses the given input and calls a command with it.
     * @param input
     * @return
     */
    public String parse(String input) {
        String[] split = input.split(Pattern.quote(" "));
        return "> "+ input +"\n" + execute(split[0], Arrays.copyOfRange(split, 1, split.length));
    }

    private void createHelpText() {
        this.helperText.append("Available commands:\n");
        getHelp(this.helperText);
    }
    /**
     * Executes command with string messages. Useful for a user console etc.
     * @param commandString
     * @param params
     * @return
     */
    public String execute(String commandString, String... params) {
        // integrated helper text
        if(commandString.equals("help") || commandString.equals("?")) {
            if(this.helperText.length() == 0) createHelpText();
            this.output.status = 1;
            this.output.message = this.helperText.toString();
            this.output.payload = null;
            return this.output.message;
        }
        Command command = this.commands.get(commandString);
        this.executeNonVerbose(commandString, params);
        if(!this.verbose) return null; // TODO if non-verbose, amount of errors shall be collected in a list
        switch(this.output.status) {
            case -2: return "Could not execute command (status "+this.output.status+"): "+ this.output.message +" - Expected arguments: " + this.output.payload;
            case -1: return "Could not execute command (status "+this.output.status+"): "+ this.output.message + "\n\tUsage:\n\t" + command.getUsage();
            case 0: return "Command not found";
            case 1: return this.output.message + " (status "+this.output.status+")" + (this.output.payload != null ? ":\n" + this.output.payload : "");
            default: return "Command aborted. " + this.output.message + " (status "+this.output.status+")";
        }
    }

    /**
     * Executes command non-verbose without string messages. Useful for batch
     * processing.
     * @param commandString
     * @param params
     * @return
     */
    public int executeNonVerbose(String commandString, String... params) {
        Command command = commands.get(commandString);
        if( command == null) {
            this.output.status = 0;
            this.output.message = null;
            this.output.payload = null;
            return 0;
        }
        command.execute(params);
        this.output.status = command.output().status;
        this.output.message = command.output().message;
        this.output.payload = command.output().payload;
        return this.output.status;
    }

    /**
     * Obtains all commands available in this processor
     * @return
     */
    public Collection<Command> getCommands() {
        return this.commands.values();
    }

    /**
     * Appends a help text of all available commands to the provided StringBuilder
     * @return
     */
    public void getHelp(StringBuilder builder) {
        for(Command command : this.commands.values()) {
            builder.append(command.getUsage()).append("\n");
        }
    }
}
