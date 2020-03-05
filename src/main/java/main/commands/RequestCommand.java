package main.commands;

import java.util.ArrayList;
import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import main.database.DBManager;
import main.lib.Constants;
import net.dv8tion.jda.api.entities.Member;
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
		
		TextChannel cmds = event.getGuild().getTextChannelById(Constants.commands);
		if (cmds == null) {
			cmds = event.getGuild().getTextChannelsByName("commands", true).get(0);
		}
		
		DBManager manager = DBManager.getInstance();
		DBCollection onDuty = manager.getCollection(Constants.MainDB, Constants.DutyList);
		DBCursor list = onDuty.find();
		DBObject currentMember = null;
		if (list.hasNext()) {
			currentMember = list.next();
		}
		
		List<String> staffList = new ArrayList<>();
		
		if (list.size() > 0) {
			while (currentMember != null) {
				String adminID = currentMember.get("adminID").toString();
				Member member = event.getGuild().getMemberById(adminID);
				
				if (member != null) {
					staffList.add(member.getAsMention());
				}
				
				if (list.hasNext()) {
					currentMember = list.next();
				} else {
					currentMember = null;
				}
			}
		}
		
		String requester = event.getMember().getAsMention();
		String channel = event.getTextChannel().getAsMention();
		String jumpTo = event.getMessage().getJumpUrl();
		
		cmds.sendMessage(String.format("%s | %s requested a mod in %s with reason: `%s` \n <%s>", String.join(", ", staffList), requester, channel, reason, jumpTo)).queue();
		event.reply(String.format("%s staff have been notified.", list.size()));
	}
}
