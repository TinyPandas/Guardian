package main.actions;

import main.handlers.MuteHandler;
import main.lib.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class UnmuteAction extends ModAction {
	public UnmuteAction(String targetUserID, String targetUserName, String adminID, String adminName, String reason) {
		super(targetUserID, targetUserName, adminID, adminName, reason);
	}

	@Override
	public boolean execute(Guild guild, TextChannel channelOfExecution) {
		boolean wasMuted = MuteHandler.unmute(getTargetUserID());
		
		if (wasMuted) {
			EmbedBuilder result = new EmbedBuilder();
			result.setTitle(String.format("<%s> has been unmuted.", getTargetUserName()));
			result.setDescription("unmuteManual");
			result.addField("Discord ID", getTargetUserID(), true);
			result.addField("Name", getTargetUserName(), true);
			result.addField("Moderator ID", getAdminID(), true);
			result.addField("Moderator", getAdminName(), true);
			
			TextChannel muteLog = guild.getTextChannelById(Constants.mute_log);
			if (muteLog == null) { 
				muteLog = guild.getTextChannelsByName("mute-log", true).get(0);
			}
			
			muteLog.sendMessage(result.build()).queue();
		} else {
			channelOfExecution.sendMessage("That user is not muted.").queue();
		}
		
		return true;
	}
}
