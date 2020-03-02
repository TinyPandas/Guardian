package main.handlers;

import java.awt.Color;
import java.util.HashMap;

import main.Start;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;

public class MuteHandler extends Thread {
	//HashMap<userID, expirationOfMuteInMilli>
	private static HashMap<String, Long> isMuted = new HashMap<>();
	private static HashMap<String, String> userGuild = new HashMap<>();
	
	public static void mute(String guildID, String userID, long endTime) {
		if (!isMuted(userID)) {
			System.out.println(String.format("Start: %s, End: %s", System.currentTimeMillis(), endTime));
			isMuted.put(userID, endTime);
			userGuild.put(userID, guildID);
		}
	}
	
	public static boolean isMuted(String userID) {
		return isMuted.containsKey(userID);
	}
	
	public static boolean unmute(String userID, boolean early, boolean hasRole) {
		if (isMuted(userID) || hasRole) {
			isMuted.remove(userID);
			
			Guild guild = Start.getAPI().getGuildById(userGuild.get(userID));
			guild.removeRoleFromMember(userID, guild.getRolesByName("muted", true).get(0)).queue();
			guild.mute(guild.getMemberById(userID), false).queue();
			
			Start.getAPI().getUserById(userID).openPrivateChannel().queue(pc -> {
				if (early) {
					EmbedBuilder unmuteEarly = new EmbedBuilder();
					unmuteEarly.setTitle("You've been unmuted early by a moderator.");
					unmuteEarly.setDescription("Welcome back.");
					unmuteEarly.setColor(Color.GREEN);
					pc.sendMessage(unmuteEarly.build()).queue();
				} else {
					pc.sendMessage("You have been unmuted.").queue();
				}
			});
			
			return true;
		}
		return false;
	}
	
	public void run() {
		while (true) {
			try {
				Thread.sleep(60 * 1000); //60 seconds | 1 minute
			} catch (InterruptedException ie) {
				
			} finally {
				long current = System.currentTimeMillis();
				
				for (String muted:isMuted.keySet()) {
					long endTime = isMuted.get(muted);
					
					if (current >= endTime) {
						unmute(muted, false, false);
					}
				}
			}
		}
	}
}
