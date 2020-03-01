package main.database;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import main.lib.Constants;

public class ModerationLogDB {
	private static final long monthInMilli = 1000L * 60L * 60L * 24L * 30L;

	/**
	 * 
	 * @param level
	 * @return muteTime in minutes
	 */
	private static int getTimeForWarn(int level) {
		System.out.println(level);
        if (level <= 1)
            return 0;

        int muteTime = (int) (Math.pow(2.0, level - 3) * 60.0);

        System.out.println(muteTime);
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
				long occurance = (long) index.get("date");				
				if (start - occurance < monthInMilli * 2L) { //Check if log occurred in past 2 months.
					ret += 1;
				}
			}
		}

		if (ret >= 9)
			ret = 9;

		return ret;
	}

	public static DBObject generateLog(String userID, String modAction, String adminID, String reason) {
		DBCollection logs = DBManager.getInstance().getCollection(Constants.ModLogs, userID);
		long warns = logs.count() + 1; // Get how many warns/mutes a user has. + 1 for this infraction.

		long start = System.currentTimeMillis();
		int warnsPastMonthSpan = getWarnsInMonthSpan(logs, start);

		DBObject log = new BasicDBObject("_id", warns).append("action", modAction).append("date", start)
				.append("length", getTimeForWarn(warnsPastMonthSpan)).append("adminID", adminID).append("reason", reason);

		return log;
	}
}
