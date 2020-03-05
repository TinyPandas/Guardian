package main.actions;

import java.util.List;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public abstract class ModAction implements Action {
	// Define shared fields
	private String targetUserID;
	private String targetUserName;
	private String adminID;
	private String adminName;
	private String reason;
	private List<String> images;
	private String messageID;
	
	public ModAction(String targetUserID, String targetUserName, String adminID, String adminName, String reason, List<String> images, String messageID) {
		this.targetUserID = targetUserID;
		this.targetUserName = targetUserName;
		this.adminID = adminID;
		this.adminName = adminName;
		this.reason = reason;
		this.images = images;
		this.messageID = messageID;
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
	
	public void setReason(String string) {
		this.reason = string;
	}
	
	public List<String> getImages() {
		return images;
	}
	
	public String getMessageID() {
		return messageID;
	}

	// Define shared functions
	public abstract boolean execute(Guild guild, TextChannel channelOfExecution);
}
