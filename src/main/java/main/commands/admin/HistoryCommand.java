package main.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.actions.HistoryAction;
import main.actions.ModAction;
import main.lib.CommandArguments;
import main.lib.Utils;

public class HistoryCommand extends Command {
	public HistoryCommand() {
		name = "history";
		help = "history <userQuery>";
		requiredRole = "staff";
	}
	
	@Override
	protected void execute(CommandEvent event) {
		CommandArguments args = Utils.getArgs(event);
		
		ModAction action = new HistoryAction(args.getTargetUserID(), args.getTargetUser().getEffectiveName(), args.getAdminID(), args.getAdmin().getEffectiveName(), args.getReason());
		action.execute(event.getGuild(), event.getTextChannel());
	}
}
