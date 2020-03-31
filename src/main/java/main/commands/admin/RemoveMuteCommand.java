package main.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.actions.RemoveMuteAction;
import main.actions.lib.ModAction;
import main.lib.CommandArguments;
import main.lib.Utils;
import net.dv8tion.jda.api.entities.Member;

/**
 * Version 1.0
 * @author TinyPanda
 */

public class RemoveMuteCommand extends Command {
	public RemoveMuteCommand() {
		name = "removemute";
		help = "removemute <userQuery> <muteIndex>";
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
			
			String index = event.getMessage().getContentStripped().split("\\s+")[2];
			
			ModAction action = new RemoveMuteAction(args.getTargetUserID(), args.getTargetUser().getEffectiveName(), args.getAdminID(), args.getAdmin().getEffectiveName(), index);
			action.execute(event.getGuild(), event.getTextChannel());
		}
	}
}
