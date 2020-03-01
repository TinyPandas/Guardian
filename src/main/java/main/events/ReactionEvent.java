package main.events;

import main.actions.ModAction;
import main.actions.MuteAction;
import main.actions.WarnAction;
import main.lib.Constants;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
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
			Member author = m.getMember();
			Member admin = event.getMember();
			
			boolean isStaff = false;
			for (Role r:admin.getRoles()) {
				if (r.getName().equalsIgnoreCase("staff")) {
					isStaff = true;
				}
			}
			
			
			if (isStaff) return;
			
			ModAction action = null;
			
			//TODO Log deletion in Constants.chat_log
			m.delete().queue();
			
			
			if (reactionName.equalsIgnoreCase(Constants.mute)) {
				action = new MuteAction(author.getId(), author.getEffectiveName(), admin.getId(), admin.getEffectiveName(), m.getContentRaw());
				action.execute(event.getGuild(), event.getChannel());
			} else if(reactionName.equalsIgnoreCase(Constants.warn)) {
				action = new WarnAction(author.getId(), author.getEffectiveName(), admin.getId(), admin.getEffectiveName(), m.getContentRaw());
				action.execute(event.getGuild(), event.getChannel());
			} else if(reactionName.equalsIgnoreCase(Constants.mute_context)) {
				action = new MuteAction(author.getId(), author.getEffectiveName(), admin.getId(), admin.getEffectiveName(), m.getContentRaw());
				((MuteAction)action).deleteContext(event.getChannel(), m.getId());
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