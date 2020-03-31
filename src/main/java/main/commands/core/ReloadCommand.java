package main.commands.core;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.handlers.CommandLoader;
import main.lib.Utils;
import net.dv8tion.jda.api.entities.Member;

public class ReloadCommand extends Command {
	public ReloadCommand() {
		name = "reloadCommand";
		help = "reloads a command";
	}
	
	@Override
	protected void execute(CommandEvent event) {
		Member comandee = event.getMember();
		
		if (Utils.hasRoleWithName(comandee, "staff")) {
			CommandLoader loader = CommandLoader.getInstance();
			String commandName = event.getArgs().split(" ")[0];
			
			if (commandName.length() > 0) { 
				boolean result = loader.reloadCommand(event.getClient(), commandName);
				
				if (result) {
					event.reactSuccess();
				} else {
					event.reactError();
					event.reply("Something went wrong in unloading | loading the command.");
				}
			}
		}
	}
}
