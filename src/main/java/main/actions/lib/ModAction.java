package main.actions.lib;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public abstract class ModAction implements Action {
	// Define shared fields
	private String targetUserID;
	private String targetUserName;
	private String adminID;
	private String adminName;
	
	public ModAction(String targetUserID, String targetUserName, String adminID, String adminName) {
		this.targetUserID = targetUserID;
		this.targetUserName = targetUserName;
		this.adminID = adminID;
		this.adminName = adminName;
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

	// Define shared functions
	public abstract boolean execute(Guild guild, TextChannel channelOfExecution);
}
