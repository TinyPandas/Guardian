package main.actions;

import main.database.DBManager;
import main.database.ModerationLogDB;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class RemoveMuteAction extends ModAction {
	private String index = "-1";
	
	public RemoveMuteAction(String targetUserID, String targetUserName, String adminID, String adminName, String reason, String index) {
		super(targetUserID, targetUserName, adminID, adminName, reason);
		this.index = index;
	}

	@Override
	public boolean execute(Guild guild, TextChannel channelOfExecution) {
		System.out.println("Removing index: " + index);
		DBManager.getInstance().deleteDocument(ModerationLogDB.DBName, getTargetUserID(), index);
		
		return false;
	}
}
