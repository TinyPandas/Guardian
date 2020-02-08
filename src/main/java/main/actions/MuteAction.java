package main.actions;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import main.database.DBManager;
import main.database.ModerationLogDB;
import main.lib.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class MuteAction extends ModAction {
	public MuteAction(String targetUserID, String targetUserName, String adminID, String adminName, String reason) {
		super(targetUserID, targetUserName, adminID, adminName, reason);
	}

	@Override
	public boolean execute(Guild guild) {
		DBObject log = ModerationLogDB.generateLog(getTargetUserID(), "mute", getAdminID(), getReason());
		DBCollection logs = DBManager.getInstance().addDocument(ModerationLogDB.DBName, getTargetUserID(), log);
		int length = (int)log.get("length");
		
		EmbedBuilder result = new EmbedBuilder();
		result.setTitle(String.format("<%s> has been muted.", getTargetUserName()));
		result.setDescription("Reason:\n" + getReason());
		result.addField("Length", Integer.toString(length), true);
		result.addField("Times muted", Long.toString(logs.count()), true);
		result.addField("Discord ID", getTargetUserID(), true);
		result.addField("Name", getTargetUserName(), true);
		result.addField("Moderator ID", getAdminID(), true);
		result.addField("Moderator", getAdminName(), true);
		
		TextChannel muteLog = guild.getTextChannelById(Constants.mute_log);
		muteLog.sendMessage(result.build()).queue();
		
		return true;
	}
}
