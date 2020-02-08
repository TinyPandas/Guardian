package main.actions;

import net.dv8tion.jda.api.entities.Guild;

public abstract class ModAction implements Action {
	// Define shared fields
	private String targetUserID;
	private String targetUserName;
	private String adminID;
	private String adminName;
	private String reason;
	
	public ModAction(String targetUserID, String targetUserName, String adminID, String adminName, String reason) {
		this.targetUserID = targetUserID;
		this.targetUserName = targetUserName;
		this.adminID = adminID;
		this.adminName = adminName;
		this.reason = reason;
	}
	
	public String getTargetUserID() {
		return targetUserID;
	}

	public String getTargetUserName() {
		return targetUserName;
	}

	public String getAdminID() {
		return adminID;
	}
	
	public String getAdminName() {
		return adminName;
	}

	public String getReason() {
		return reason;
	}

	// Define shared functions
	public abstract boolean execute(Guild guild);
}
