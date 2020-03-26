package main.handlers;

import com.jagrosh.jdautilities.command.CommandClientBuilder;

import main.commands.HelpCommand;
import main.commands.InfoCommand;
import main.commands.InviteCommand;
import main.commands.NoCodeCommand;
import main.commands.RequestCommand;
import main.commands.admin.BanCommand;
import main.commands.admin.FilterCommand;
import main.commands.admin.HistoryCommand;
import main.commands.admin.KickCommand;
import main.commands.admin.MuteCommand;
import main.commands.admin.OfftopicCommand;
import main.commands.admin.RemoveMuteCommand;
import main.commands.admin.RulesCommand;
import main.commands.admin.ShutdownCommand;
import main.commands.admin.UnmuteCommand;
import main.commands.admin.ViewImageCommand;
import main.commands.admin.WarnCommand;
import main.lib.Constants;

public class CommandHandler extends CommandClientBuilder {
	public CommandHandler() {
		setPrefix(";");
		useHelpBuilder(false);
		setShutdownAutomatically(false);
		setOwnerId(Constants.pandaID);
		setCoOwnerIds(Constants.megaID);
		setEmojis("✅", "⚠", "❌");
		addCommands(new HelpCommand(),
					new OfftopicCommand(),
					new RulesCommand(),
					new InviteCommand(),
					new InfoCommand(),
					new NoCodeCommand(),
					new MuteCommand(),
					new HistoryCommand(),
					new WarnCommand(),
					new UnmuteCommand(),
					new KickCommand(),
					new RemoveMuteCommand(),
					new BanCommand(),
					new RequestCommand(),	
					new FilterCommand(),
					new ViewImageCommand(),
					new ShutdownCommand());
	}
}
