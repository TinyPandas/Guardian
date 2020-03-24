package main.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.DBObject;

import main.lib.Constants;

public class DBParser {
	public static void importOldData(String fileName) {
		File f = new File(fileName);
		if (f.exists()) {
			try (BufferedReader br = new BufferedReader(new FileReader(f))) {
				String line = "";
				while ((line = br.readLine()) != null) {
					JSONObject userIndex = new JSONObject(line);
					String targetID = userIndex.getString("id");
					System.out.println("Updating " + targetID);
					JSONArray mutes = userIndex.getJSONArray("mutes");
					
					mutes.forEach((muteItem) -> {
						JSONObject entry = (JSONObject)muteItem;
						long date = entry.getLong("date");
						String adminID = entry.getString("who");
						String reason = entry.getString("reason");
						int length = entry.getInt("len");
						String action = "";
						
						if (entry.has("warn")) {
							action = "Warned";
						} else if(entry.has("kick")) {
							action = "Kicked";
						} else {
							action = "Muted";
						}
						
						DBObject log = ModerationLogDB.generateLog(targetID, action, adminID, reason, null, null, length, date);
						DBManager.getInstance().addDocument(Constants.ModLogs, targetID, log);
					});
				}
			} catch (IOException ie) {
				ie.printStackTrace();
			}
		}
	}
}
