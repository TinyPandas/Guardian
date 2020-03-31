package main.commands;

import java.awt.Color;
import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.lib.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

/**
 * Version 1.0
 * @author TinyPanda
 */

public class HelpCommand extends Command {
	public HelpCommand() {
		name = "help";
		help = "Displays all commands and their basic help description.";
		category = new Category("utility");
	}
	
	@Override
	protected void execute(CommandEvent event) {		
		Member member = event.getMember();
		boolean isStaff = Utils.hasRoleWithName(member, "staff");
		
		CommandClient client = event.getClient();
		List<Command> commands = client.getCommands();
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("**SHBot** Commands");
		builder.setColor(Color.GREEN);
		
		String utility = "";
		String staff = "";
		String undefined = "";
		
		for (Command command:commands) {
			String helpLine = "`" + client.getPrefix() + command.getName() + "` - " + command.getHelp();
			Category cat = command.getCategory();
			
			if (cat != null) {
				if (cat.getName() == "utility") {
					utility += helpLine + "\n";
				} else if(cat.getName() == "staff") {
					staff += helpLine + "\n";
				}
			} else {
				undefined += helpLine + "\n";
			}
		}
		
		builder.addField("Utility", utility, false);
		if (isStaff) {
			builder.addField("Staff", staff, false);
		}
		builder.addField("No Category", undefined, false);
		
		member.getUser().openPrivateChannel().queue(s -> {
			s.sendMessage(builder.build()).queue();
		}, e -> {
			event.reply("Unable to DM you help message.");
		});
	}
}
