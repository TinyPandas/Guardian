package main.lib;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

public class Utils {
	public static CommandArguments getArgs(CommandEvent event, boolean required, boolean delete) {
		String[] args = event.getArgs().split("\\s+");
		Member admin = event.getMember();
		String targetUserQuery = null, messageID = null;
		Member targetUser = null;
		String reason = "No reason provided";
		List<String> attachments = new ArrayList<String>();

		if (delete) {
			if (event.getMessage() != null)
				event.getMessage().delete().queue();
		}

		if (args.length == 1) {
			targetUserQuery = args[0];
		} else if (args.length >= 2) {
			targetUserQuery = args[0];
			messageID = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");
		} else {
			event.reply("You have not provided a userID.");
		}

		if (targetUserQuery != null) {
			List<Member> potMembers = FinderUtil.findMembers(targetUserQuery, event.getGuild());
			if (potMembers.size() > 1) {
				event.reply("Multiple members found. Please be more specific.");
			} else if (potMembers.size() == 1) {
				targetUser = potMembers.get(0);
			} else {
				if (required)
					event.reply("No users found with the query provided.");
			}
		}

		if (messageID != null) {
			for (TextChannel c : event.getGuild().getTextChannels()) {
				try {
					Message m = c.retrieveMessageById(messageID).complete();
					if (m != null) {
						reason = m.getContentRaw().replaceAll("`", "");

						attachments = downloadAttachments(m);

						m.delete().queueAfter(2, TimeUnit.SECONDS);
					}
				} catch (IllegalArgumentException iae) {
					reason = messageID;
				} catch (ErrorResponseException ere) {
					// Provided a valid snowflakeID, however no message was found.
					// Nothing to handle.
				}
			}
		}

		return new CommandArguments(targetUserQuery, targetUser, admin, reason, attachments, messageID);
	}
	
	public static List<String> downloadAttachments(Message m) {
		List<String> ret = new ArrayList<>();
		List<Attachment> images = m.getAttachments();

		if (images.size() > 0) {
			for (Attachment image : images) {
				File dir = new File("./imageCollections/" + m.getAuthor().getId() + "/" + m.getId() + "/");
				if (!dir.exists()) {
					dir.mkdirs();
				}
				
				ret.add(image.getFileName());
				
				image.downloadToFile(dir + "/" + image.getFileName())
						.thenAccept(file -> System.out.println("Saved attachment to " + file.getName()))
						.exceptionally(t -> { // handle failure
							t.printStackTrace();
							return null;
						});
			}
		}
		
		return ret;
	}

	public static boolean hasRoleWithName(Member member, String roleName) {
		List<Role> roles = member.getRoles();
		for (Role r : roles) {
			if (r.getName().equalsIgnoreCase(roleName)) {
				return true;
			}
		}
		return false;
	}

	public static String getDate(long milli) {
		Date date = new Date(milli);

		return date.toGMTString();
	}

	public static String getLength(long length) {
		if (length >= 60) {
			return Long.toString(length / 60) + " hours";
		} else {
			return Long.toString(length) + " minutes";
		}
	}
}
