package main.lib;

public class MessageObject {
	private String authorID;
	private long timestamp;
	private boolean deleted = false;
	
	public MessageObject(String authorID, long milliTime) {
		this.authorID = authorID;
		this.timestamp = milliTime;
		
		Thread exec = new Thread() {
			public void run() {
				while (!deleted) {
					try {
						Thread.sleep(250); 
					} catch (InterruptedException ie) {
						//
					} finally {
						long c = System.currentTimeMillis();
						if (c-timestamp >= 5000) {
							deleted = true;
						}
					}
				}
			}
		};
		exec.start();
	}
	
	public String getAuthorID() {
		return authorID;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public boolean isDeleted() {
		return deleted;
	}
}
