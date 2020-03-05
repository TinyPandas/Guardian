package main.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class NoCodeCommand extends Command {
	public NoCodeCommand() {
		name = "nocode";
		help = "prompt user to provide code.";
		guildOnly = true;
		category = new Category("utility");
	}
	
	@Override
	protected void execute(CommandEvent event) {
		event.getMessage().delete().queue();
		event.reply("To get assistance, we recommend you provide the code you are working with inside of a codeblock. \n\n \\`\\`\\`lua \n--code\n \\`\\`\\`");
	}
}
