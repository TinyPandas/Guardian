package main.events;

import main.lib.Constants;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MemberUpdateEvent extends ListenerAdapter {
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		super.onGuildMemberJoin(event);
		
		Guild guild = event.getGuild();
		TextChannel joinLog = guild.getTextChannelById(Constants.join_log);
		if (joinLog == null) {
			joinLog = guild.getTextChannelsByName("join-log", true).get(0);
		}
		
		joinLog.sendMessage(String.format("**Joined:** %s", event.getMember().getAsMention())).queue();
	}

	@Override
	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
		super.onGuildMemberLeave(event);
		
		Guild guild = event.getGuild();
		TextChannel joinLog = guild.getTextChannelById(Constants.join_log);
		if (joinLog == null) {
			joinLog = guild.getTextChannelsByName("join-log", true).get(0);
		}
		
		joinLog.sendMessage(String.format("**Left:** %s [%s]", event.getMember().getAsMention(), event.getUser().getAsTag())).queue();
	}

	@Override
	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
		// TODO Auto-generated method stub
		super.onGuildMemberRoleAdd(event);
	}

	@Override
	public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
		// TODO Auto-generated method stub
		super.onGuildMemberRoleRemove(event);
	}

	@Override
	public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
		// TODO Auto-generated method stub
		super.onGuildMemberUpdateNickname(event);
	}

	@Override
	public void onGuildMemberUpdateBoostTime(GuildMemberUpdateBoostTimeEvent event) {
		// TODO Auto-generated method stub
		super.onGuildMemberUpdateBoostTime(event);
	}
}