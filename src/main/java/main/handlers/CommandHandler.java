package main.handlers;

import com.jagrosh.jdautilities.command.CommandClientBuilder;

import main.commands.InfoCommand;
import main.commands.InviteCommand;
import main.commands.NoCodeCommand;
import main.commands.OfftopicCommand;
import main.commands.RulesCommand;
import main.commands.admin.HistoryCommand;
import main.commands.admin.MuteCommand;
import main.commands.admin.WarnCommand;
import main.lib.Constants;

public class CommandHandler extends CommandClientBuilder {
	public CommandHandler() {
		setPrefix("-");
		useHelpBuilder(true);
		setShutdownAutomatically(false);
		setOwnerId(Constants.pandaID);
		setCoOwnerIds(Constants.megaID);
		setEmojis("✅", "⚠", "❌");
		addCommands(new OfftopicCommand(),
					new RulesCommand(),
					new InviteCommand(),
					new InfoCommand(),
					new NoCodeCommand(),
					new MuteCommand(),
					new HistoryCommand(),
					new WarnCommand());
	}
}
