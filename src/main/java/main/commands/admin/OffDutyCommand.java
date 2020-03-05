package main.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import main.database.DBManager;
import main.lib.Constants;
import main.lib.Utils;
import net.dv8tion.jda.api.entities.Member;

public class OffDutyCommand extends Command {
	public OffDutyCommand() {
		name = "offduty";
		help = "removes you from the mentionable list for the request command";
		category = new Category("staff");
	}
	
	@Override
	protected void execute(CommandEvent event) {
		Member user = event.getMember();
		
		if (Utils.hasRoleWithName(user, "staff")) {
			DBManager manager = DBManager.getInstance();
			DBObject query = new BasicDBObject("adminID", user.getId());
			
			DBCollection col = manager.getCollection(Constants.MainDB, Constants.DutyList);
			DBCursor cursor = col.find(query);
			
			if (cursor.size() > 0) {
				DBObject toDelete = cursor.next();
				
				manager.deleteDocument(Constants.MainDB, Constants.DutyList, toDelete);
				event.reply("You have been taken off the duty list.");
			} else {
				event.reply("You are not marked for duty.");
			}
		}
	}
}
