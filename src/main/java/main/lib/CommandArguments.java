package main.lib;

import net.dv8tion.jda.api.entities.Member;

public class CommandArguments {
	String targetUserID;
	Member targetUser;
	String adminID;
	Member admin;
	String reason;
	
	public CommandArguments(Member targetUser, Member admin, String reason) {
		this.targetUser = targetUser;
		this.targetUserID = targetUser.getId();
		this.admin = admin;
		this.adminID = admin.getId();
		this.reason = reason;
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
}
