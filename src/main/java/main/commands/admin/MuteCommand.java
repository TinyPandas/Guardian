package main.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.actions.ModAction;
import main.actions.MuteAction;
import main.lib.CommandArguments;
import main.lib.Utils;

public class MuteCommand extends Command {
	public MuteCommand() {
		name = "mute";
		help = "mute <userID> [messageID]";
		requiredRole = "staff";
	}
	
	@Override
	protected void execute(CommandEvent event) {
		CommandArguments args = Utils.getArgs(event, true, true);
		
		ModAction action = new MuteAction(args.getTargetUserID(), args.getTargetUser().getEffectiveName(), args.getAdminID(), args.getAdmin().getEffectiveName(), args.getReason());
		action.execute(event.getGuild(), event.getTextChannel());
	}
}
