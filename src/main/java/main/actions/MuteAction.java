package main.actions;

import java.awt.Color;
import java.util.List;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import main.database.DBManager;
import main.database.ModerationLogDB;
import main.handlers.MuteHandler;
import main.lib.Constants;
import main.lib.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class MuteAction extends ModAction {
	public MuteAction(String targetUserID, String targetUserName, String adminID, String adminName, String reason) {
		super(targetUserID, targetUserName, adminID, adminName, reason);
	}
	
	private String contextString = "";
	
	public void deleteContext(TextChannel channelOfExecution, String messageId) {
		final long start = System.currentTimeMillis() / 1000L;
		
		channelOfExecution.getHistoryBefore(messageId, 50).queue(history -> {
			List<Message> retrieved = history.getRetrievedHistory();
			for (Message m:retrieved) {
				long postDate = m.getTimeCreated().toInstant().getEpochSecond();
				long dif = start-postDate;
				if (dif > 300) {
					break;
				}
				
				
				TextChannel chatLog = channelOfExecution.getGuild().getTextChannelById(Constants.chat_log);
				if (chatLog == null) {
					chatLog = channelOfExecution.getGuild().getTextChannelsByName("chat-log", true).get(0);
				}
				if (m.getAuthor().getId().equalsIgnoreCase(getTargetUserID()) && dif <= 300) {
					EmbedBuilder builder = new EmbedBuilder();
					builder.setTitle("**Message Deleted**");
					builder.addField("User", m.getMember().getEffectiveName(), true);
					builder.addField("Channel", m.getTextChannel().getAsMention(), true);
					builder.addField("Content", m.getContentRaw(), false);
					
					chatLog.sendMessage(builder.build()).queue();
					contextString = m.getContentRaw().replaceAll("'", "") + "\n" + contextString;
					m.delete().queue();
				}
			}
			
			if (contextString.length() > 0) {
				contextString += getReason();
				setReason(contextString);
			}
			
			execute(channelOfExecution.getGuild(), channelOfExecution);
		});
	}
	
	@Override
	public boolean execute(Guild guild, TextChannel channelOfExecution) {		
		DBObject log = ModerationLogDB.generateLog(getTargetUserID(), "Muted", getAdminID(), getReason());
		DBCollection logs = DBManager.getInstance().addDocument(Constants.ModLogs, getTargetUserID(), log);
		int length = (int)log.get("length");
		
		EmbedBuilder result = new EmbedBuilder();
		result.setTitle(String.format("<%s> has been muted.", getTargetUserName()));
		result.setDescription("Reason:\n" + log.get("reason"));
		result.addField("Length", Utils.getLength(length), true);
		result.addField("Times muted", Long.toString(logs.count()), true);
		result.addField("Discord ID", getTargetUserID(), true);
		result.addField("Name", getTargetUserName(), true);
		result.addField("Moderator ID", getAdminID(), true);
		result.addField("Moderator", getAdminName(), true);
		result.setColor(Color.RED);
		
		MuteHandler.mute(guild.getId(), getTargetUserID(), System.currentTimeMillis() + (length*60*1000));
		
		TextChannel muteLog = guild.getTextChannelById(Constants.mute_log);
		if (muteLog == null) { 
			muteLog = guild.getTextChannelsByName("mute-log", true).get(0);
		}

		guild.addRoleToMember(getTargetUserID(), guild.getRolesByName("muted", true).get(0)).queue();
		try {
			guild.mute(guild.getMemberById(getTargetUserID()), true).queue();
		} catch (Exception e) {
			//May not be in voice channel.
		}
		
		muteLog.sendMessage(result.build()).queue();
		guild.getMemberById(getTargetUserID()).getUser().openPrivateChannel().queue(pc -> {
			result.clearFields();
			result.setTitle(String.format("You have been muted in %s.", guild.getName()));
			result.addField("Length", Utils.getLength(length), true);
			result.addField("Times muted", Long.toString(logs.count()), true);
			pc.sendMessage(result.build()).queue();
		});
		
		return true;
	}
}
