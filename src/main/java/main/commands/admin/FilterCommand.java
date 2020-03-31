package main.commands.admin;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

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

/**
 * Version 1.0
 * @author TinyPanda
 */

public class FilterCommand extends Command {
	public FilterCommand() {
		name = "filter";
		help = "add | removes a word from the filter.";
		category = new Category("staff");
	}
	
	@Override
	protected void execute(CommandEvent event) {
		Member user = event.getMember();
		
		if (Utils.hasRoleWithName(user, "staff")) {
			DBManager manager = DBManager.getInstance();
			DBCollection col = manager.getCollection(Constants.MainDB, Constants.FilterList);
			
			if (event.getArgs().length() == 0) {
				List<String> words = new ArrayList<>();
				
				DBCursor cur = col.find();
				DBObject c = cur.next();
				
				while (c != null) {
					words.add(c.get("word").toString());
					
					if (cur.hasNext()) {
						c = cur.next();
					} else {
						c = null;
					}
				}
				
				event.reply("**Filter List** \n " + String.join(", ", words));
			} else {
				String[] words = event.getArgs().split("\\s+");
				
				List<String> added = new ArrayList<>();
				List<String> removed = new ArrayList<>();
				
				for (String word:words) {
					DBObject query = new BasicDBObject("word", word);
					
					ObjectId index = manager.hasDocument(Constants.MainDB, Constants.FilterList, query);
					
					if (index == null) {
						manager.addDocument(Constants.MainDB, Constants.FilterList, query);
						added.add(word);
					} else {
						 DBCursor cursor = col.find(query);
						 
						 if (cursor.size() > 0) {
							 DBObject toDelete = cursor.next();
							 
							 manager.deleteDocument(Constants.MainDB, Constants.FilterList, toDelete);
							 removed.add(word);
						 }
					}
				}
				
				String reply = String.format("Added %s words and removed %s words from the filter.", added.size(), removed.size());
				event.reply(reply);
			}
		}
	}
}
