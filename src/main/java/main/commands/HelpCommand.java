package main.commands;

import java.awt.Color;
import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.lib.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

public class HelpCommand extends Command {
	public HelpCommand() {
		name = "help";
		help = "help";
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
		
		for (Command command:commands) {
			String helpLine = "`" + client.getPrefix() + command.getName() + "` - " + command.getHelp();
			
			if (command.getCategory().getName() == "utility") {
				utility += helpLine + "\n";
			} else if(command.getCategory().getName() == "staff") {
				staff += helpLine + "\n";
			}
		}
		
		builder.addField("Utility", utility, false);
		if (isStaff) {
			builder.addField("Staff", staff, false);
		}
		
		member.getUser().openPrivateChannel().queue(s -> {
			s.sendMessage(builder.build()).queue();
		}, e -> {
			event.reply("Unable to DM you help message.");
		});
	}
}
