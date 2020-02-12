package main.events;

import main.actions.ModAction;
import main.actions.MuteAction;
import main.lib.Constants;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveAllEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReactionEvent extends ListenerAdapter {
	@Override
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		super.onGuildMessageReactionAdd(event);
		String reactionName = event.getReactionEmote().getName();
		event.getChannel().retrieveMessageById(event.getMessageId()).queue(m -> {
			if (reactionName.equalsIgnoreCase(Constants.mute)) {
				Member author = m.getMember();
				Member admin = event.getMember();
				
				ModAction action = new MuteAction(author.getId(), author.getEffectiveName(), admin.getId(), admin.getEffectiveName(), m.getContentRaw());
				action.execute(event.getGuild(), event.getChannel());
				
				//TODO Log deletion in Constants.chat_log
				m.delete().queue();
			}
		});
	}

	@Override
	public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event) {
		// TODO Auto-generated method stub
		super.onGuildMessageReactionRemove(event);
	}

	@Override
	public void onGuildMessageReactionRemoveAll(GuildMessageReactionRemoveAllEvent event) {
		// TODO Auto-generated method stub
		super.onGuildMessageReactionRemoveAll(event);
	}
}