package main.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.actions.KickAction;
import main.actions.ModAction;
import main.lib.CommandArguments;
import main.lib.Utils;
import net.dv8tion.jda.api.entities.Member;

public class KickCommand extends Command {
	public KickCommand() {
		name = "kick";
		help = "kick <userQuery> <reason>";
	}
	
	@Override
	protected void execute(CommandEvent event) {
		Member user = event.getMember();
		
		if (Utils.hasRoleWithName(user, "staff")) {
			CommandArguments args = Utils.getArgs(event, true, true);
			
			ModAction action = new KickAction(args.getTargetUserID(), args.getTargetUser().getEffectiveName(), args.getAdminID(), args.getAdmin().getEffectiveName(), args.getReason());
			action.execute(event.getGuild(), event.getTextChannel());
		}
	}
}
