package main.actions;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public interface Action {
	boolean execute(Guild guild, TextChannel channelOfExecution);
}
