package telegram.command;

import java.util.regex.Pattern;

public enum Patterns {

    START("^/start$"),
    HELP("^/help$"),
    SETTINGS("^/settings \\d{1,16}$"),
    SIGN_UP("^/signup \\S{1,16} \\S{1,32} \\S{1,32}$"),
    SIGN_IN("^/signin \\S{1,16} \\S{1,32}$"),
    SIGN_OUT("^/signout$"),
    ALL("^/all( \\d{1,16})?$"),
    CREATE("^/create .{0,64} ?# .{1,1024}$"),
    READ("^/read \\d{1,16}$"),
    UPDATE("^/update \\d{1,16} # .{0,64} ?# .{1,1024}$"),
    DELETE("^/delete \\d{1,16}$");

    private final Pattern pattern;

    Patterns(String pattern) {
        this.pattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
    }

    public Pattern getPattern() {
        return pattern;
    }

}