package main.events;

import main.lib.Constants;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
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
	}

	@Override
	public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
		super.onGuildVoiceMove(event);
		
		TextChannel log = getVoiceLog(event);
		log.sendMessage(String.format("**%s** moves to %s", event.getMember().getEffectiveName(), event.getChannelJoined().getName())).queue();
	}

	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		super.onGuildVoiceLeave(event);
		
		TextChannel log = getVoiceLog(event);
		log.sendMessage(String.format("**%s** leaves %s", event.getMember().getEffectiveName(), event.getChannelLeft().getName())).queue();
	}
}
