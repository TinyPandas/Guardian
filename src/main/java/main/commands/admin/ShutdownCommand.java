package main.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.handlers.MuteHandler;
import main.lib.Utils;
import net.dv8tion.jda.api.entities.Member;

public class ShutdownCommand extends Command {
	public ShutdownCommand() {
		name = "shutdown";
		help = "shutdown";
		category = new Category("staff");
	}
	
	@Override
	protected void execute(CommandEvent event) {
		Member user = event.getMember();
		
		boolean isStaff = Utils.hasRoleWithName(user, "staff");
		
		if (isStaff) {
			event.getMessage().delete().queue(a -> {
				MuteHandler.save();
				
				while (!(MuteHandler.finished)) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException ie) {
						
					}
				}
				event.getJDA().shutdown();
				System.exit(0);
			});
		}
	}
}
