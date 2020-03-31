package main.commands.admin;

import java.io.File;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.lib.Utils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * Version 1.0
 * @author TinyPanda
 */

public class ViewImageCommand extends Command {
	public ViewImageCommand() {
		name = "viewimage";
		help = "View an image that was logged to a users infraction";
		category = new Category("staff");
	}
	
	@Override
	protected void execute(CommandEvent event) {
		Member member = event.getMember();
		
		if (!Utils.hasRoleWithName(member, "staff")) {
			return;
		}
		
		String[] args = event.getArgs().split("\\s+");
		if (args.length != 3) {
			event.reply("Not enough arguments provided.");
		} else {
			String userQuery = args[0];
			String messageID = args[1];
			String imageName = args[2];
			
			File imageFile = new File("./imageCollections/" + userQuery + "/" + messageID + "/" + imageName);
			if (!imageFile.exists()) {
				event.reply("No image found with provided information. Please verify it is correct. [Ensure the image name has the extension as well]");
			} else {
				TextChannel c = event.getTextChannel();
				
				c.sendMessage("Image Found").addFile(imageFile, imageFile.getName()).queue();
			}
		}
	}
}
