package main.actions.infractions;

import java.awt.Color;
import java.util.List;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import main.actions.lib.InfractionAction;
import main.database.DBManager;
import main.database.ModerationLogDB;
import main.lib.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class WarnAction extends InfractionAction {
	public WarnAction(String targetUserID, String targetUserName, String adminID, String adminName, String reason, List<String> images, String messageID) {
		super(targetUserID, targetUserName, adminID, adminName, reason, images, messageID);
	}

	@Override
	public boolean execute(Guild guild, TextChannel channelOfExecution) {
		DBObject log = ModerationLogDB.generateLog(getTargetUserID(), "Warned", getAdminID(), getReason(), getImages(), getMessageID());
		DBCollection logs = DBManager.getInstance().addDocument(Constants.ModLogs, getTargetUserID(), log);
		
		EmbedBuilder result = new EmbedBuilder();
		result.setTitle(String.format("User Warned: <%s>", getTargetUserName()));
		result.setDescription(log.get("reason").toString());
		result.addField("Discord ID", getTargetUserID(), true);
		result.addField("Name", getTargetUserName(), true);
		result.addField("Moderator ID", getAdminID(), true);
		result.addField("Moderator", getAdminName(), true);
		result.setColor(Color.ORANGE);
		
		TextChannel muteLog = guild.getTextChannelById(Constants.mute_log);
		if (muteLog == null) {
			muteLog = guild.getTextChannelsByName("mute-log", true).get(0);
		}
		muteLog.sendMessage(result.build()).queue();
		
		if (channelOfExecution.getParent().getId().equalsIgnoreCase("356054271680184324")) {
			channelOfExecution.sendMessage(result.build()).queue();
		}
		
		guild.getMemberById(getTargetUserID()).getUser().openPrivateChannel().queue(pc -> {
			EmbedBuilder warnMsg = new EmbedBuilder();
			warnMsg.setTitle(String.format("You have been warned in %s.", guild.getName()));
			warnMsg.setDescription(log.get("reason").toString());
			warnMsg.setColor(Color.ORANGE);
			warnMsg.setFooter("This has been logged to your record.");
			pc.sendMessage(warnMsg.build()).queue();
		});
		
		return true;
	}
}
