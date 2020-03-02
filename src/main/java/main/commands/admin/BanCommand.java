package main.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.actions.BanAction;
import main.actions.ModAction;
import main.lib.CommandArguments;
import main.lib.Utils;
import net.dv8tion.jda.api.entities.Member;

public class BanCommand extends Command {
	public BanCommand() {
		name = "ban";
		help = "ban <userQuery> <reason>";
	}
	
	@Override
	protected void execute(CommandEvent event) {
		Member user = event.getMember();
		
		if (Utils.hasRoleWithName(user, "staff")) {
			CommandArguments args = Utils.getArgs(event, false, true);
			
			if (args.getTargetUser().getUser().isBot()) {
				return;
			}
		
			ModAction action = new BanAction((args.getTargetUserID().equalsIgnoreCase("-1")) ? args.getQuery() : args.getTargetUserID(), (args.getTargetUser() != null) ? args.getTargetUser().getEffectiveName() : args.getQuery(), args.getAdminID(), args.getAdmin().getEffectiveName(), args.getReason());
			action.execute(event.getGuild(), event.getTextChannel());
		}
	}
}
