package main.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.actions.ModAction;
import main.actions.RemoveMuteAction;
import main.lib.CommandArguments;
import main.lib.Utils;

public class RemoveMuteCommand extends Command {
	public RemoveMuteCommand() {
		name = "removemute";
		help = "removemute <userQuery> <muteIndex>";
		requiredRole = "staff";
	}
	
	@Override
	protected void execute(CommandEvent event) {
		CommandArguments args = Utils.getArgs(event, true, true);
		String index = event.getMessage().getContentStripped().split("\\s+")[2];
		
		ModAction action = new RemoveMuteAction(args.getTargetUserID(), args.getTargetUser().getEffectiveName(), args.getAdminID(), args.getAdmin().getEffectiveName(), args.getReason(), index);
		action.execute(event.getGuild(), event.getTextChannel());
	}
}
