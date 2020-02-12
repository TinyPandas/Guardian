package main.handlers;

import java.util.HashMap;

public class MuteHandler extends Thread {
	//HashMap<userID, expirationOfMuteInMilli>
	private static HashMap<String, Long> isMuted = new HashMap<>();
	
	public static void mute(String userID, long endTime) {
		if (!isMuted(userID)) {
			isMuted.put(userID, endTime);
		}
	}
	
	public static boolean isMuted(String userID) {
		return isMuted.containsKey(userID);
	}
	
	public static boolean unmute(String userID) {
		if (isMuted(userID)) {
			isMuted.remove(userID);
			return true;
			//TODO Inform player they have been unmuted.
		}
		return false;
	}
	
	public void run() {
		while (true) {
			try {
				Thread.sleep(60 * 1000); //60 seconds | 1 minute
			} catch (InterruptedException ie) {
				
			} finally {
				long current = System.currentTimeMillis();
				
				for (String muted:isMuted.keySet()) {
					long endTime = isMuted.get(muted);
					
					if (current >= endTime) {
						unmute(muted);
					}
				}
			}
		}
	}
}
