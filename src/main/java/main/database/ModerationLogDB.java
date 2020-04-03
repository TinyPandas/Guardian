package main.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import main.commands.admin.RulesCommand;
import main.lib.Constants;

public class ModerationLogDB {
	private static final long monthInMilli = 1000L * 60L * 60L * 24L * 30L;

	/**
	 * 
	 * @param level
	 * @return muteTime in minutes
	 */
	private static int getTimeForWarn(int level) {
        if (level < 1)
            return 0;

        int muteTime = (int) (Math.pow(2.0, level - 3) * 60.0);

        // limit mute time to 72h
        return Math.min(muteTime, 72 * 60);
    }

	public static int getWarnsInMonthSpan(DBCollection logs, long start) {
		int ret = 1; //Starting at 1 guarantees a 30 minute mute.

		for (int i = (int) logs.count(); i > 0; i--) {
			DBObject query = new BasicDBObject("_id", i);
			DBCursor cursor = logs.find(query);
			DBObject index = cursor.one();
			if (index != null) {
				String action = index.get("action").toString();
				
				if (action.equalsIgnoreCase("muted")) {
					long occurance = (long) index.get("date");		
					if (start - occurance < monthInMilli * 2L) { //Check if log occurred in past 2 months.
						if (((String)index.get("action")).equalsIgnoreCase("muted")) {
							ret += 1;
						}
					}
				}
			}
		}

		if (ret >= 9)
			ret = 9;

		return ret;
	}
	
	public static DBObject generateLog(String userID, String modAction, String adminID, String reason, List<String> images, String messageID, int length, long date) {
		if (reason.length() == 0 || reason.equalsIgnoreCase("")) {
			reason = "No reason provided.";
		}
		
		DBCollection logs = DBManager.getInstance().getCollection(Constants.ModLogs, userID);
		long warns = logs.count() + 1; // Get how many warns/mutes a user has. + 1 for this infraction.

		long start = (date != -1) ? date : System.currentTimeMillis();
		int warnsPastMonthSpan = getWarnsInMonthSpan(logs, start);

		String temp = "";
		List<String> list = new ArrayList<>();
		String[] parts = reason.split("\\s+");
		for (String part:parts) {
			String toAdd = part;
			
			if (part.startsWith("$")) {
				toAdd = part.substring(1);
				HashMap<String, String> rules = RulesCommand.getRulesIndex();
				if (rules.containsKey(toAdd)) {
					toAdd = rules.get(toAdd);
				}
			}
			
			list.add(toAdd);
		}
		
		if (temp.length() == 0) {
			if (list.size() > 0) {
				temp = String.join(" ", list);
			} else {
				temp = reason;
			}
		}
		
		DBObject log = new BasicDBObject("_id", warns)
						.append("action", modAction)
						.append("date", start)
						.append("adminID", adminID)
						.append("reason", temp);
		
		if (modAction.equalsIgnoreCase("muted") && length > -1) {
			((BasicDBObject)log).append("length", length);
		} else if(length == -1) {
			((BasicDBObject)log).append("length", getTimeForWarn(warnsPastMonthSpan));
		}

		if (messageID != null) {
			((BasicDBObject)log).append("messageID", messageID);
		}
		
		if (images != null && images.size() > 0) {
			((BasicDBObject)log).append("images", String.join(", ", images));
		}
		
		return log;
	}

	public static DBObject generateLog(String userID, String modAction, String adminID, String reason, List<String> images, String messageID, int length) {
		return generateLog(userID, modAction, adminID, reason, images, messageID, length, -1);
	}
}
