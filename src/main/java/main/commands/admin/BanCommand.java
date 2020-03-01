package main.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.actions.BanAction;
import main.actions.ModAction;
import main.lib.CommandArguments;
import main.lib.Utils;

public class BanCommand extends Command {
	public BanCommand() {
		name = "ban";
		help = "ban <userQuery> <reason>";
		requiredRole = "staff";
	}
	
	@Override
	protected void execute(CommandEvent event) {
		CommandArguments args = Utils.getArgs(event, false, true);
		
		ModAction action = new BanAction((args.getTargetUserID().equalsIgnoreCase("-1")) ? args.getQuery() : args.getTargetUserID(), (args.getTargetUser() != null) ? args.getTargetUser().getEffectiveName() : args.getQuery(), args.getAdminID(), args.getAdmin().getEffectiveName(), args.getReason());
		action.execute(event.getGuild(), event.getTextChannel());
	}
}
