package main.commands.admin;

import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import main.database.DBManager;
import main.database.ModerationLogDB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class MuteCommand extends Command {
	public MuteCommand() {
		name = "mute";
		help = "mute <userID> [messageID]";
		requiredRole = "staff";
	}
	
	@Override
	protected void execute(CommandEvent event) {
		String[] args = event.getArgs().split("\\s+");
		event.getMessage().delete().queue();
		String userQuery, messageID = null;
		Member user = null;
		String reason = "No reason provided";
		
		if (args.length == 1) {
			userQuery = args[0];
		} else if(args.length == 2) {
			userQuery = args[0];
			messageID = args[1];
		} else {
			event.reply("You have not provided a userID.");
			return;
		}
		
		if (userQuery != null) {
			List<Member> potMembers = FinderUtil.findMembers(userQuery, event.getGuild());
			if (potMembers.size() > 1) {
				event.reply("Multiple members found. Please be more specific.");
				return;
			} else if(potMembers.size() == 1) {
				user = potMembers.get(0);
			} else {
				event.reply("No users found.");
				return;
			}
			
			if (user == null) {
				event.reply("You have provided an invalid userID");
				return;
			}
		}
		
		if (messageID != null) {
			for (TextChannel c : event.getGuild().getTextChannels()) {
				Message m = c.retrieveMessageById(messageID).complete();
				if (m != null) {
					reason = m.getContentRaw().replaceAll("`", "");
					//TODO log message before delete
					m.delete().queue();
				}
			}
		}
		
		DBObject log = ModerationLogDB.generateLog(user.getId(), "mute", event.getMember().getId(), reason);
		DBCollection logs = DBManager.getInstance().addDocument(ModerationLogDB.DBName, user.getId(), log);
		int length = (int)log.get("length");
		
		EmbedBuilder result = new EmbedBuilder();
		result.setTitle(String.format("<%s> has been muted.", user.getEffectiveName()));
		result.setDescription("Reason:\n" + reason);
		result.addField("Length", Integer.toString(length), true);
		result.addField("Times muted", Long.toString(logs.count()), true);
		result.addField("Discord ID", user.getId(), true);
		result.addField("Name", user.getEffectiveName(), true);
		result.addField("Moderator ID", event.getMember().getId(), true);
		result.addField("Moderator", event.getMember().getEffectiveName(), true);
		
		event.reply(result.build());
	}
}
