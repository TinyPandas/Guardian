package main.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.lib.Constants;

public class OfftopicCommand extends Command {
	public OfftopicCommand() {
		name = "offtopic";
		help = "redirects members to where offtopic conversation can be had.";
		guildOnly = true;
		category = new Category("utility");
		requiredRole = "staff";
	}
	
	@Override
	protected void execute(CommandEvent event) {
		event.getMessage().delete().queue();
		event.reply("This is not the place to enjoy off-topic conversation. We advise that you carry your conversation over to " 
					+ event.getGuild().getTextChannelById(Constants.commonsID)
					+ " or "
					+ event.getGuild().getTextChannelById(Constants.offtopicID)
					+ ". **Any further off-topic conversation can/will be reprimanded accordingly.**");
	}
}
