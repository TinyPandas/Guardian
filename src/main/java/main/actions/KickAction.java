package main.actions;

import java.awt.Color;

import com.mongodb.DBObject;

import main.database.DBManager;
import main.database.ModerationLogDB;
import main.lib.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class KickAction extends ModAction {
	public KickAction(String targetUserID, String targetUserName, String adminID, String adminName, String reason) {
		super(targetUserID, targetUserName, adminID, adminName, reason);
	}

	@Override
	public boolean execute(Guild guild, TextChannel channelOfExecution) {
		DBObject log = ModerationLogDB.generateLog(getTargetUserID(), "Kicked", getAdminID(), getReason());
		DBManager.getInstance().addDocument(Constants.ModLogs, getTargetUserID(), log);
		
		guild.getMemberById(getTargetUserID()).kick().queue();
		
		EmbedBuilder result = new EmbedBuilder();
		result.setTitle(String.format("User kicked: <%s>", getTargetUserName()));
		result.setDescription(log.get("reason").toString());
		result.addField("Discord ID", getTargetUserID(), true);
		result.addField("Name", getTargetUserName(), true);
		result.addField("Moderator ID", getAdminID(), true);
		result.addField("Moderator", getAdminName(), true);
		result.setColor(Color.WHITE);
		
		TextChannel kickBanLog = guild.getTextChannelById(Constants.kick_ban_log);
		if (kickBanLog == null) {
			kickBanLog = guild.getTextChannelsByName("kick-ban-log", true).get(0);
		}
		kickBanLog.sendMessage(result.build()).queue();
		
		return false;
	}
	
}
