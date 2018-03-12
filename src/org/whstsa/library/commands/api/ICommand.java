package org.whstsa.library.commands.api;

import org.json.JSONObject;

import java.util.List;

/**
 * Interface for commands, for use in the command-line
 * @see org.whstsa.library.commands.functional.HelpCommand
 * @author Eric
 */
public interface ICommand {

    String HELP_FLAG = "show_help";

    static JSONObject showSyntax() {
        JSONObject object = new JSONObject();
        object.put(HELP_FLAG, true);
        return object;
    }

    JSONObject handle(String[] args, ICommandSender commandSender);

    List<String> getArgs();

    String getName();

}
