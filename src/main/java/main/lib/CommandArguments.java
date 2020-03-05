package main.lib;

import java.util.List;

import net.dv8tion.jda.api.entities.Member;

public class CommandArguments {
	String query;
	String targetUserID;
	Member targetUser;
	String adminID;
	Member admin;
	String reason;
	String messageID;
	List<String> images;
	
	public CommandArguments(String query, Member targetUser, Member admin, String reason, List<String> attachments, String messageID) {
		this.query = query;
		this.targetUser = targetUser;
		this.targetUserID = (targetUser != null) ? targetUser.getId() : "-1";
		this.admin = admin;
		this.adminID = admin.getId();
		this.reason = reason;
		this.images = attachments;
		this.messageID = messageID;
	}
	
	public String getQuery() {
		return query;
	}

	public Member getTargetUser() {
		return targetUser;
	}
	
	public String getTargetUserID() { 
		return targetUserID;
	}

	public Member getAdmin() {
		return admin;
	}
	
	public String getAdminID() { 
		return adminID;
	}

	public String getReason() {
		return reason;
	}
	
	public List<String> getImages() {
		return images;
	}
	
	public String getMessageID() {
		return messageID;
	}
}
