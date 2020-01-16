package main.handlers;

import com.jagrosh.jdautilities.command.CommandClientBuilder;

import main.lib.Constants;

public class CommandHandler extends CommandClientBuilder {
	public CommandHandler() {
		setPrefix("-");
		useHelpBuilder(true);
		setShutdownAutomatically(false);
		setOwnerId(Constants.pandaID);
		setCoOwnerIds(Constants.megaID);
		setEmojis("✅", "⚠", "❌");
		addCommands();
	}
}
