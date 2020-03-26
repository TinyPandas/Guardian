package main.commands;

import java.util.ArrayList;
import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.lib.Constants;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

public class RequestCommand extends Command {
	public RequestCommand() {
		name = "request";
		help = "request <reason>";
		cooldownScope = CooldownScope.CHANNEL;
		cooldown = 180; // 3 minutes.
		category = new Category("utility");
	}
	
	@Override
	protected void execute(CommandEvent event) {
		String reason = event.getArgs();
		
		if (reason.length() > 0) {
		
			TextChannel cmds = event.getGuild().getTextChannelById(Constants.commands);
			if (cmds == null) {
				cmds = event.getGuild().getTextChannelsByName("commands", true).get(0);
			}
			
			Guild guild = event.getGuild();
			Role staff = guild.getRolesByName("staff", true).get(0);
			
			List<String> staffList = new ArrayList<>();
			
			for (Member m:guild.getMembersWithRoles(staff)) {			
				if (!m.getUser().isBot()) { 
					if (m.getOnlineStatus() == OnlineStatus.ONLINE) {
						staffList.add(m.getAsMention());
					}
				}
			}
			
			String requester = event.getMember().getAsMention();
			String channel = event.getTextChannel().getAsMention();
			String jumpTo = event.getMessage().getJumpUrl();
			
			cmds.sendMessage(String.format("%s | %s requested a mod in %s with reason: `%s` \n <%s>", String.join(", ", staffList), requester, channel, reason, jumpTo)).queue();
			event.reply(String.format("%s staff have been notified.", staffList.size()));
		} else {
			event.reply("Please provide a reason when using the `;request` command. For example: `;request PersonA is spamming.`");
		}
	}
}
