package main.actions;

import java.awt.Color;
import java.util.List;

import main.actions.lib.ModAction;
import main.handlers.MuteHandler;
import main.lib.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

public class UnmuteAction extends ModAction {
	public UnmuteAction(String targetUserID, String targetUserName, String adminID, String adminName) {
		super(targetUserID, targetUserName, adminID, adminName);
	}

	@Override
	public boolean execute(Guild guild, TextChannel channelOfExecution) {
		boolean isMuted = MuteHandler.isMuted(getTargetUserID());
		boolean flagged = false;
		
		if (!isMuted) {
			Member member = guild.getMemberById(getTargetUserID());
			List<Role> roles = member.getRoles();
			for (Role r:roles) {
				if (r.getName().equalsIgnoreCase("muted")) {
					isMuted = true;
					flagged = true;
				}
			}
		}
		
		if (isMuted) {
			EmbedBuilder result = new EmbedBuilder();
			result.setTitle(String.format("<%s> has been unmuted.", getTargetUserName()));
			result.setDescription("unmuteManual");
			result.addField("Discord ID", getTargetUserID(), true);
			result.addField("Name", getTargetUserName(), true);
			result.addField("Moderator ID", getAdminID(), true);
			result.addField("Moderator", getAdminName(), true);
			result.setColor(Color.GREEN);
			
			TextChannel muteLog = guild.getTextChannelById(Constants.mute_log);
			if (muteLog == null) { 
				muteLog = guild.getTextChannelsByName("mute-log", true).get(0);
			}
			
			if (channelOfExecution.getParent().getId().equalsIgnoreCase("356054271680184324")) {
				channelOfExecution.sendMessage(result.build()).queue();
			}
			
			//guild.removeRoleFromMember(getTargetUserID(), guild.getRolesByName("muted", true).get(0)).queue();
			
			muteLog.sendMessage(result.build()).queue();
			MuteHandler.unmute(getTargetUserID(), true, flagged);
		} else {
			channelOfExecution.sendMessage("That user is not muted.").queue();
		}
		
		return true;
	}
}
