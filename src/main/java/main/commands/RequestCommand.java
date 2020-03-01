package main.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.lib.Constants;
import net.dv8tion.jda.api.entities.TextChannel;

public class RequestCommand extends Command {
	public RequestCommand() {
		name = "request";
		help = "request <reason>";
		cooldownScope = CooldownScope.CHANNEL;
		cooldown = 180; // 3 minutes.
	}
	
	@Override
	protected void execute(CommandEvent event) {
		String reason = event.getArgs();
		
		TextChannel cmds = event.getGuild().getTextChannelById(Constants.commands);
		if (cmds == null) {
			cmds = event.getGuild().getTextChannelsByName("commands", true).get(0);
		}
		
		//Get all onduty staff members.. :oof:
		
		String list = "[listOfMods]";
		String requester = event.getMember().getAsMention();
		String channel = event.getTextChannel().getAsMention();
		String jumpTo = event.getMessage().getJumpUrl();
		
		cmds.sendMessage(String.format("%s | %s requested a mod in %s with reason: `%s` \n <%s>", list, requester, channel, reason, jumpTo)).queue();
	}
}
