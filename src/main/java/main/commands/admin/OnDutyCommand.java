package main.commands.admin;

import org.bson.types.ObjectId;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import main.database.DBManager;
import main.lib.Constants;
import main.lib.Utils;
import net.dv8tion.jda.api.entities.Member;

public class OnDutyCommand extends Command {
	public OnDutyCommand() {
		name = "onduty";
		help = "marks you for mentionable via request command";
	}
	
	@Override
	protected void execute(CommandEvent event) {
		Member user = event.getMember();
		
		if (Utils.hasRoleWithName(user, "staff")) {
			DBManager manager = DBManager.getInstance();
			DBObject query = new BasicDBObject("adminID", user.getId());
			
			ObjectId index = manager.hasDocument(Constants.MainDB, Constants.DutyList, query);
			
			if (index == null) {
				manager.addDocument(Constants.MainDB, Constants.DutyList, query);
				event.reply("You have been marked for duty.");
			} else {
				event.reply("You are already marked for duty.");
			}
		}
	}
}
