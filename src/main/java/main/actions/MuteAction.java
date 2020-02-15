package main.actions;

import java.util.Date;
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
				
				if (m.getAuthor().getId().equalsIgnoreCase(getTargetUserID()) && dif <= 300) {
					//TODO Log to chat log
					contextString = m.getContentRaw().replaceAll("'", "") + "\n" + contextString;
					m.delete().queue();
				}
			}
			
			if (contextString.length() > 0) {
				contextString += getReason();
				setReason(contextString);
			}
			
			System.out.println("--REASON--\n" + getReason() + "--==--");
			
			execute(channelOfExecution.getGuild(), channelOfExecution);
		});
	}
	
	@Override
	public boolean execute(Guild guild, TextChannel channelOfExecution) {		
		DBObject log = ModerationLogDB.generateLog(getTargetUserID(), "Muted", getAdminID(), getReason());
		DBCollection logs = DBManager.getInstance().addDocument(ModerationLogDB.DBName, getTargetUserID(), log);
		int length = (int)log.get("length");
		
		EmbedBuilder result = new EmbedBuilder();
		result.setTitle(String.format("<%s> has been muted.", getTargetUserName()));
		result.setDescription("Reason:\n" + getReason());
		result.addField("Length", Utils.getLength(length), true);
		result.addField("Times muted", Long.toString(logs.count()), true);
		result.addField("Discord ID", getTargetUserID(), true);
		result.addField("Name", getTargetUserName(), true);
		result.addField("Moderator ID", getAdminID(), true);
		result.addField("Moderator", getAdminName(), true);
		
		//TODO Implement Mute thread.
		//TODO Actually mute member.
		if (length > 0) {
			MuteHandler.mute(getTargetUserID(), System.currentTimeMillis() + length*60*1000);
			guild.getTextChannelsByName("mute-log", true).get(0).sendMessage(String.format("%s has been muted until %s", getTargetUserName(), new Date(System.currentTimeMillis() + length*60*1000))).queue();
		}
		
		TextChannel muteLog = guild.getTextChannelById(Constants.mute_log);
		if (muteLog == null) { 
			muteLog = guild.getTextChannelsByName("mute-log", true).get(0);
		}
	
		muteLog.sendMessage(result.build()).queue();
		
		return true;
	}
}
