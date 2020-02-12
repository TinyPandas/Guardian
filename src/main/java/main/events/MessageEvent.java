package main.events;

import main.handlers.MuteHandler;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageEmbedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageEvent extends ListenerAdapter {
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);
		
		String targetID = event.getAuthor().getId();
		
		if (MuteHandler.isMuted(targetID)) {
			event.getMessage().delete().queue();
		}
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