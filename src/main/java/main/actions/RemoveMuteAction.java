package main.actions;

import java.util.List;

import main.database.DBManager;
import main.lib.Constants;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class RemoveMuteAction extends ModAction {
	private String index = "-1";
	
	public RemoveMuteAction(String targetUserID, String targetUserName, String adminID, String adminName, String reason, String index, List<String> images, String messageID) {
		super(targetUserID, targetUserName, adminID, adminName, reason, images, messageID);
		this.index = index;
	}

	@Override
	public boolean execute(Guild guild, TextChannel channelOfExecution) {
		System.out.println("Removing index: " + index);
		DBManager.getInstance().deleteDocument(Constants.ModLogs, getTargetUserID(), index);
		
		return false;
	}
}
