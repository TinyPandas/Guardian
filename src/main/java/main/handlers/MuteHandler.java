package main.handlers;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import main.Start;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;

public class MuteHandler extends Thread {
	//HashMap<userID, expirationOfMuteInMilli>
	private static HashMap<String, Long> isMuted = new HashMap<>();
	private static HashMap<String, String> userGuild = new HashMap<>();
	
	public static boolean finished = false;
	
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
			
			try {
				guild.mute(guild.getMemberById(userID), false).queue();
			} catch (Exception e) {
				//May not be in voice channel.
			}
			
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
		load();
		
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
	
	public void load() {
		File f = new File("mutedCache");
		if (f.exists()) {
			try (BufferedReader br = new BufferedReader(new FileReader(f))) {
				String line = "";
				while ((line = br.readLine()) != null) {
					System.out.println("Reading: " + line);
					String userID = line.split(":")[0];
					String endTimeStr = line.split(":")[1];
					Long endTime = Long.valueOf(endTimeStr);
					
					isMuted.put(userID, endTime);
				}
			} catch (IOException ie) {
				ie.printStackTrace();
			}
		}
	}
	
	public static void save() {
		File f = new File("mutedCache");
		if (f.exists()) {
			f.delete();
		}
		
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//assume f exists at this point.
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
			for (String userID:isMuted.keySet()) {
				Long endTime = isMuted.get(userID);
				
				String line = userID + ":" + endTime + "\n";
				
				System.out.println("Writing: " + line);
				bw.write(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		finished = true;
	}
}
