package main.actions;

import java.awt.Color;
import java.util.List;

import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import main.Start;
import main.database.DBManager;
import main.database.ModerationLogDB;
import main.lib.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class BanAction extends ModAction {
	public BanAction(String targetUserID, String targetUserName, String adminID, String adminName, String reason) {
		super(targetUserID, targetUserName, adminID, adminName, reason);
	}

	@Override
	public boolean execute(Guild guild, TextChannel channelOfExecution) {
		DBObject log = ModerationLogDB.generateLog(getTargetUserID(), "Banned", getAdminID(), getReason());
		DBCollection logs = DBManager.getInstance().addDocument(ModerationLogDB.DBName, getTargetUserID(), log);
		
		Member m = guild.getMemberById(getTargetUserID());
		if (m != null) {
			m.ban(0, getReason());
		} else {
			guild.ban(getTargetUserID(), 0, getReason()).queue();
		}
		
		User temp = null;
		
		if (getTargetUserName().equalsIgnoreCase(getTargetUserID())) {
			List<User> users = FinderUtil.findUsers(getTargetUserID(), Start.getAPI());
			if (users.size() > 0) {
				temp = users.get(0);
			}
		}
		
		EmbedBuilder result = new EmbedBuilder();
		result.setTitle(String.format("User banned: <%s>", temp != null ? temp.getName() : getTargetUserName()));
		result.setDescription(getReason());
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
