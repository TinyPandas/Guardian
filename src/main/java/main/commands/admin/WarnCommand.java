package main.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.actions.ModAction;
import main.actions.WarnAction;
import main.lib.CommandArguments;
import main.lib.Utils;
import net.dv8tion.jda.api.entities.Member;

public class WarnCommand extends Command {
	public WarnCommand() {
		name = "warn";
		help = "warn <userID> [messageID]";
		category = new Category("staff");
	}
	
	@Override
	protected void execute(CommandEvent event) {
		Member user = event.getMember();
		
		if (Utils.hasRoleWithName(user, "staff")) {
			CommandArguments args = Utils.getArgs(event, true, true);
			
			if (args.getTargetUser().getUser().isBot()) {
				return;
			}
			
			ModAction action = new WarnAction(args.getTargetUserID(), args.getTargetUser().getEffectiveName(), args.getAdminID(), args.getAdmin().getEffectiveName(), args.getReason(), args.getImages(), args.getMessageID());
			action.execute(event.getGuild(), event.getTextChannel());
		}
	}
}
