package main.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.actions.ModAction;
import main.actions.MuteAction;
import main.lib.CommandArguments;
import main.lib.Utils;
import net.dv8tion.jda.api.entities.Member;

public class MuteCommand extends Command {
	public MuteCommand() {
		name = "mute";
		help = "mute <userID> [messageID]";
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
			
			ModAction action = new MuteAction(args.getTargetUserID(), args.getTargetUser().getEffectiveName(), args.getAdminID(), args.getAdmin().getEffectiveName(), args.getReason(), args.getImages(), args.getMessageID());
			action.execute(event.getGuild(), event.getTextChannel());
		}
	}
}
