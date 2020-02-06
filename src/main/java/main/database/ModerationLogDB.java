package main.database;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class ModerationLogDB {
	public static final String DBName = "ModerationLogs";
	private static final long dayInMilli = 1000 * 60 * 60 * 24;

	private static int getTimeForWarn(int level) {
		if (level == 1)
			return 0;

		int muteTime = (int) (Math.pow(2.0, level - 3) * 60.0);

		// limit mute time to 72h
		return Math.min(muteTime, 72 * 60 * 60);
	}

	public static int getWarnsInDay(DBCollection logs, long start) {
		int ret = 0;

		for (int i = (int) logs.count(); i > 0; i--) {
			DBObject query = new BasicDBObject("_id", i);
			DBCursor cursor = logs.find(query);
			DBObject index = cursor.one();
			long occurance = (long) index.get("date");
			if (start - occurance < dayInMilli) {
				ret += 1;
			}
		}

		if (ret >= 9)
			ret = 9;

		return ret;
	}

	public static DBObject generateLog(String userID, String modAction, String adminID, String reason) {
		DBCollection logs = DBManager.getInstance().getCollection(DBName, userID);
		long warns = logs.count() + 1; // Get how many warns/mutes a user has. + 1 for this infraction.

		long start = System.currentTimeMillis();
		int warnsPastDay = getWarnsInDay(logs, start);

		DBObject log = new BasicDBObject("_id", warns).append("action", modAction).append("date", start)
				.append("length", getTimeForWarn(warnsPastDay)).append("adminID", adminID).append("reason", reason);

		return log;
	}
}
