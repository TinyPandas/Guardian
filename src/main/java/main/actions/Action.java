package main.actions;

import net.dv8tion.jda.api.entities.Guild;

public interface Action {
	boolean execute(Guild guild);
}
