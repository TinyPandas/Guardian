package main.database;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class ModerationLogDB {
	public static final String DBName = "ModerationLogs";
	
	private static long getTimeForWarn(int level) {
		switch(level) {
			case 1:
				return 0;
			case 2:
				return 30*60; //30 minutes
			case 3:
				return 60*60; //1 hour
			case 4:
				return (60*4)*60; //2 hours
			case 5:
				return (60*4)*60; //4 hours
			case 6:
				return (60*8)*60; //8 hours
			case 7:
				return (60*16)*60; //16 hours
			case 8:
				return (60*24)*60; //24 hours
			case 9:
				return (60*32)*60; //32 hours
			default:
				return 30*60;
		}
	}
	
	public static DBObject generateLog(String userID, String modAction, String adminID, String reason) {
		DBCollection logs = DBManager.getCollection(DBName, userID);		
		long warns = logs.count() + 1; //Get how many warns/mutes a user has. + 1 for this infraction.
		
		int warnsPastDay = 0;
		long start = System.currentTimeMillis();
		long dayInMilli = 1000*60*60*24;
		for (int i=(int)logs.count();i>0;i--) {
			DBObject query = new BasicDBObject("_id", i);
			DBCursor cursor = logs.find(query);
			DBObject index = cursor.one();
			long occurance = (long)index.get("date");
			if (start-occurance < dayInMilli) {
				warnsPastDay += 1;
			}
		}
		
		if (warnsPastDay >= 9) {
			warnsPastDay = 9;
		}
		
		DBObject log = new BasicDBObject("_id", warns)
						.append("action", modAction)
						.append("date", start)
						.append("length", getTimeForWarn(warnsPastDay))
						.append("adminID", adminID)
						.append("reason", reason);
		
		return log;
	}
}
