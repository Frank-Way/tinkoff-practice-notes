package telegram.command;

/**
 * Enumeration for {@link Command}'s.
 */
public enum CommandName {

    START("/start"),
    HELP("/help"),
    SETTINGS("/settings"),
    SIGN_UP("/signup"),
    SIGN_IN("/signin"),
    SIGN_OUT("/signout"),
    ALL("/all"),
    CREATE("/create"),
    READ("/read"),
    UPDATE("/update"),
    DELETE("/delete"),
    NO("");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }

}