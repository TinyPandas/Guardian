package main.events;

import main.lib.Constants;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class VoiceEvent extends ListenerAdapter {
	private TextChannel getVoiceLog(GenericGuildEvent event) {
		Guild guild = event.getGuild();
		TextChannel ret = guild.getTextChannelById(Constants.voice_log);
		if (ret == null) {
			ret = guild.getTextChannelsByName("voice-log", true).get(0);
		}
		
		return ret;
	}
	
	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
		super.onGuildVoiceJoin(event);
		
		TextChannel log = getVoiceLog(event);
		log.sendMessage(String.format("**%s** joins %s", event.getMember().getEffectiveName(), event.getChannelJoined().getName())).queue();
		
		Guild guild = event.getGuild();
		TextChannel correspondingChatChannel = null;
		
		String voiceChannelID = event.getChannelJoined().getId();
		switch(voiceChannelID) {
			case Constants.voice_voice:
				correspondingChatChannel = guild.getTextChannelById(Constants.voice_chat);
				break;
			case Constants.stream_voice:
				correspondingChatChannel = guild.getTextChannelById(Constants.stream_chat);
				break;
		}
		
		if (correspondingChatChannel != null) {			
			correspondingChatChannel.createPermissionOverride(event.getMember()).grant(Permission.MESSAGE_READ).queue();
			
//			correspondingChatChannel.getMemberPermissionOverrides().forEach(override -> {
//				Member allowee = override.getMember();
//				GuildVoiceState state = allowee.getVoiceState();
//				VoiceChannel channel = state.getChannel();
//				
//				boolean deleteOverride = false;
//				
//				if (channel != null) {
//					if (!(channel.getId().equalsIgnoreCase(voiceChannelID))) {
//						deleteOverride = true;
//					}
//				} else {
//					deleteOverride = true;
//				}
//				
//				if (deleteOverride) {
//					override.delete().queue();
//				}
//			});
		}
	}

	@Override
	public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
		super.onGuildVoiceMove(event);
		
		TextChannel log = getVoiceLog(event);
		log.sendMessage(String.format("**%s** moves to %s", event.getMember().getEffectiveName(), event.getChannelJoined().getName())).queue();
		
		Guild guild = event.getGuild();
		TextChannel newChatChannel = null;
		TextChannel oldChatChannel = null;
		
		String newVoiceChannel = event.getChannelJoined().getId();
		String oldVoiceChannel = event.getChannelLeft().getId();
		
		switch(oldVoiceChannel) {
			case Constants.voice_voice:
				oldChatChannel = guild.getTextChannelById(Constants.voice_chat);
				break;
			case Constants.stream_voice:
				oldChatChannel = guild.getTextChannelById(Constants.stream_chat);
				break;
		}
		
		switch(newVoiceChannel) {
			case Constants.voice_voice:
				newChatChannel = guild.getTextChannelById(Constants.voice_chat);
				break;
			case Constants.stream_voice:
				newChatChannel = guild.getTextChannelById(Constants.stream_chat);
				break;
		}
		
		if (newChatChannel != null) {			
			newChatChannel.createPermissionOverride(event.getMember()).grant(Permission.MESSAGE_READ).queue();
		}
		
		if (oldChatChannel != null) {			
			oldChatChannel.getPermissionOverride(event.getMember()).delete().queue();
		}
	}

	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		super.onGuildVoiceLeave(event);
		
		TextChannel log = getVoiceLog(event);
		log.sendMessage(String.format("**%s** leaves %s", event.getMember().getEffectiveName(), event.getChannelLeft().getName())).queue();
		
		Guild guild = event.getGuild();
		TextChannel correspondingChatChannel = null;
		
		if (event.getChannelJoined() != null) {
			String voiceChannelID = event.getChannelJoined().getId();
			switch(voiceChannelID) {
				case Constants.voice_voice:
					correspondingChatChannel = guild.getTextChannelById(Constants.voice_chat);
					break;
				case Constants.stream_voice:
					correspondingChatChannel = guild.getTextChannelById(Constants.stream_chat);
					break;
			}
		}
		
		if (correspondingChatChannel != null) {			
			correspondingChatChannel.getPermissionOverride(event.getMember()).delete().queue();
		}
	}
}
