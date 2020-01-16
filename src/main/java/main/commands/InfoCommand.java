package main.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class InfoCommand extends Command {
	public InfoCommand() {
		name = "info";
		help = "prompt the user to provide more information";
		guildOnly = true;
		category = new Category("utility");
	}
	
	@Override
	protected void execute(CommandEvent event) {
		event.getMessage().delete().queue();
		event.reply("Hello there, we see you are attempting to ask for help! However, there seems to not be enough informations. "
				  + "We ask that you provide the code you are working with, any errors in the output window. As well as what your "
				  + "intentions are, and what you have attempted to get there.");
	}
}
