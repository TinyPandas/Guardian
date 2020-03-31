package main.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

/**
 * Version 1.0
 * @author TinyPanda
 */

public class InviteCommand extends Command {
	public InviteCommand() {
		name = "invite";
		help = "gets invite code";
		guildOnly = true;
		category = new Category("utility");
	}
	
	@Override
	protected void execute(CommandEvent event) {
		event.getMessage().delete().queue();
		event.reply("Invite your friends using: https://discord.gg/WHTAYrK");
	}
}
