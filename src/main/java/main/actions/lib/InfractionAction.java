package main.actions.lib;

import java.util.List;

import javax.annotation.Nullable;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class InfractionAction extends ModAction {
	private String reason;
	private List<String> images;
	private String messageID;
	
	public InfractionAction(String targetUserID, String targetUserName, String adminID, String adminName, @Nullable String reason, @Nullable List<String> images, @Nullable String messageID) {
		super(targetUserID, targetUserName, adminID, adminName);
		this.reason = reason;
		this.images = images;
		this.messageID = messageID;
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

	@Override
	public boolean execute(Guild guild, TextChannel channelOfExecution) {
		// TODO Auto-generated method stub
		return false;
	}
}
