package main.actions;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import main.database.DBManager;
import main.database.ModerationLogDB;
import main.lib.Utils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class HistoryAction extends ModAction {
	public HistoryAction(String targetUserID, String targetUserName, String adminID, String adminName, String reason) {
		super(targetUserID, targetUserName, adminID, adminName, reason);
	}

	@Override
	public boolean execute(Guild guild, TextChannel channelOfExecution) {
		DBCollection logs = DBManager.getInstance().getCollection(ModerationLogDB.DBName, getTargetUserID());
		
		String currentMessage = "";
		
		DBCursor cursor = logs.find();
		DBObject currentLog = cursor.next();
		
		//TODO Cleanup
		while (cursor.hasNext()) {
			String action = currentLog.get("action").toString();
			String adminID = currentLog.get("adminID").toString();
			String adminName = "**unknown**";
			//Can't fetch member here without try/catch, so check if not null.
			if (guild.getMemberById(adminID) != null) {
				adminName = guild.getMemberById(adminID).getEffectiveName();
			}
			String reason = currentLog.get("reason").toString();
			int actLength = (int)currentLog.get("length");
			String length = Utils.getLength(actLength);
			String date = Utils.getDate((long)currentLog.get("date"));
			
			String line1 = currentLog.get("_id") + ": **" + action + "** on " + date + "\n";
			String line2 = action + " by: `" + adminID + "` (`" + adminName + "`)\n";
			String line3 = "Length: " + length + "\n";
			String line4 = "Reason: \n```" + reason + "```\n";
			
			int count = line1.length() + line2.length() + line4.length();
			if (actLength > 0) {
				count += line3.length();
			}
			
			if (currentMessage.length() + count > 2000) {
				channelOfExecution.sendMessage(currentMessage).queue();
				currentMessage = "";
			}
			
			if (actLength > 0) {
				currentMessage += line1 + line2 + line3 + line4;
			} else {
				currentMessage += line1 + line2 + line4;
			}
			
			currentLog = cursor.next();
		}
		
		if (currentMessage.length() > 0) {
			//Clear out last message.
			channelOfExecution.sendMessage(currentMessage).queue();
		}
		
		return false;
	}
}
