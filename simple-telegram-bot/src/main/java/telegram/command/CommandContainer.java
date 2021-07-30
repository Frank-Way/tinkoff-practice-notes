package telegram.command;

import com.google.common.collect.ImmutableMap;

import model.ServiceUser;
import telegram.service.SendBotMessageService;

import java.util.HashMap;

import static telegram.command.CommandName.*;

/**
 * Container of the {@link Command}s, which are using for handling telegram commands.
 */
public class CommandContainer {

    private final ImmutableMap<String, Command> commandMap;
    private final Command unknownCommand;

    public CommandContainer(SendBotMessageService sendBotMessageService,
                            HashMap<Long, ServiceUser> authorizedUsers,
                            HashMap<Long, Long> usersSettings) {

        commandMap = ImmutableMap.<String, Command>builder()
                .put(START.getCommandName(), new StartCommand(sendBotMessageService))
                .put(SETTINGS.getCommandName(), new SettingsCommand(sendBotMessageService, usersSettings))
                .put(HELP.getCommandName(), new HelpCommand(sendBotMessageService))
                .put(SIGN_UP.getCommandName(), new SignUpCommand(sendBotMessageService))
                .put(SIGN_IN.getCommandName(), new SignInCommand(sendBotMessageService, authorizedUsers, usersSettings))
                .put(SIGN_OUT.getCommandName(), new SignOutCommand(sendBotMessageService, authorizedUsers, usersSettings))
                .put(ALL.getCommandName(), new AllCommand(sendBotMessageService, authorizedUsers, usersSettings))
                .put(CREATE.getCommandName(), new CreateCommand(sendBotMessageService, authorizedUsers))
                .put(READ.getCommandName(), new ReadCommand(sendBotMessageService, authorizedUsers))
                .put(UPDATE.getCommandName(), new UpdateCommand(sendBotMessageService, authorizedUsers))
                .put(DELETE.getCommandName(), new DeleteCommand(sendBotMessageService, authorizedUsers))
                .put(NO.getCommandName(), new NoCommand(sendBotMessageService))
                .build();

        unknownCommand = new UnknownCommand(sendBotMessageService);
    }

    public Command retrieveCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }

}
