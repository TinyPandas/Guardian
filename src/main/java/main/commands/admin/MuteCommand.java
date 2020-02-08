package main.commands.admin;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;

import main.actions.ModAction;
import main.actions.MuteAction;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

public class MuteCommand extends Command {
	public MuteCommand() {
		name = "mute";
		help = "mute <userID> [messageID]";
		requiredRole = "staff";
	}
	
	@Override
	protected void execute(CommandEvent event) {
		String[] args = event.getArgs().split("\\s+");
		//TODO Log deletion in Constants.chat_log
		event.getMessage().delete().queue();
		String targetUserQuery, messageID = null;
		Member targetUser = null;
		String reason = "No reason provided";
		
		if (args.length == 1) {
			targetUserQuery = args[0];
		} else if(args.length >= 2) {
			targetUserQuery = args[0];
			messageID = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");
		} else {
			event.reply("You have not provided a userID.");
			return;
		}
		
		if (targetUserQuery != null) {
			List<Member> potMembers = FinderUtil.findMembers(targetUserQuery, event.getGuild());
			if (potMembers.size() > 1) {
				event.reply("Multiple members found. Please be more specific.");
				return;
			} else if(potMembers.size() == 1) {
				targetUser = potMembers.get(0);
			} else {
				event.reply("No users found.");
				return;
			}
			
			if (targetUser == null) {
				event.reply("You have provided an invalid userID");
				return;
			}
		}
		
		if (messageID != null) {
			for (TextChannel c : event.getGuild().getTextChannels()) {
				try {
					Message m = c.retrieveMessageById(messageID).complete();
					if (m != null) {
						reason = m.getContentRaw().replaceAll("`", "");
						//TODO log message before delete
						m.delete().queue();
					}
				} catch (IllegalArgumentException iae) {
					reason = messageID;
				} catch (ErrorResponseException ere) {
					//Provided a valid snowflakeID, however no message was found.
					//Nothing to handle.
				}
			}
		}
		
		ModAction action = new MuteAction(targetUser.getId(), targetUser.getEffectiveName(), event.getMember().getId(), event.getMember().getEffectiveName(), reason);
		action.execute(event.getGuild());
	}
}
