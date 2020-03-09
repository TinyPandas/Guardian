package main.actions;

import java.util.List;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import main.actions.lib.ModAction;
import main.database.DBManager;
import main.lib.Constants;
import main.lib.Utils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class HistoryAction extends ModAction {
	List<String> filters;
	
	public HistoryAction(String targetUserID, String targetUserName, String adminID, String adminName, List<String> filters) {
		super(targetUserID, targetUserName, adminID, adminName);
		this.filters = filters;
	}

	@Override
	public boolean execute(Guild guild, TextChannel channelOfExecution) {
		DBCollection logs = DBManager.getInstance().getCollection(Constants.ModLogs, getTargetUserID());
		
		String currentMessage = "";
		
		DBCursor cursor = logs.find();
		DBObject currentLog = cursor.next();
		
		//TODO Cleanup
		while (currentLog != null) {
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
			String imageList = null, messageID = null;
			if (currentLog.containsField("images")) {
				imageList = currentLog.get("images").toString();
			}
			if (currentLog.containsField("messageID")) {
				messageID = currentLog.get("messageID").toString();
			}
			
			boolean messagePassesFilter = false;
			
			if (filters.size() > 0) {
				for (String filter:filters) {
					String[] parts = filter.split("=");
					String field = parts[0];
					String query = parts[1];
					
					if (currentLog.containsField(field)) {
						if (currentLog.get(field).toString().equalsIgnoreCase(query)) {
							messagePassesFilter = true;
						}
					}
				}
			} else {
				messagePassesFilter = true;
			}
			
			if (messagePassesFilter) {
				String line1 = currentLog.get("_id") + ": **" + action + "** on " + date + "\n";
				String line2 = action + " by: `" + adminID + "` (`" + adminName + "`)\n";
				String line3 = "Length: " + length + "\n";
				String line4 = "Reason: \n```" + reason + "```\n";
				String line5 = "";
				
				if (imageList != null && imageList.length() > 0 && messageID != null && messageID.length() > 0) {
					line5 = "**ImagesLogged** \n MsgID: " + messageID + " \n Imgs: " + imageList + " \n";
				}
				
				int count = line1.length() + line2.length() + line4.length();
				if (actLength > 0) {
					count += line3.length();
				}
				if (line5.length() > 0) {
					count += line5.length();
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
				
				if (line5.length() > 0) {
					currentMessage += line5;
				}
			}
			
			if (cursor.hasNext()) {
				currentLog = cursor.next();
			} else {
				currentLog = null;
			}
		}
		
		if (currentMessage.length() > 0) {
			//Clear out last message.
			channelOfExecution.sendMessage(currentMessage).queue();
		}
		
		return false;
	}
}
