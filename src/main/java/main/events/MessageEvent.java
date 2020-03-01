package main.events;

import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageEmbedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageEvent extends ListenerAdapter {
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);
	}

	@Override
	public void onGuildMessageUpdate(GuildMessageUpdateEvent event) {
		// TODO Auto-generated method stub
		super.onGuildMessageUpdate(event);
	}

	@Override
	public void onGuildMessageDelete(GuildMessageDeleteEvent event) {
		// TODO Auto-generated method stub
		super.onGuildMessageDelete(event);
	}

	@Override
	public void onGuildMessageEmbed(GuildMessageEmbedEvent event) {
		// TODO Auto-generated method stub
		super.onGuildMessageEmbed(event);
	}
}