package main.handlers;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClientBuilder;

import main.lib.Constants;

public class CommandHandler extends CommandClientBuilder {
	static String stripExtension(String str) {
		if (str == null)
			return null;
		int pos = str.lastIndexOf(".");
		if (pos == -1)
			return str;
		return str.substring(0, pos);
	}

	public CommandHandler() {
		setPrefix(";");
		useHelpBuilder(false);
		setShutdownAutomatically(false);
		setOwnerId(Constants.pandaID);
		setCoOwnerIds(Constants.megaID);
		setEmojis("✅", "⚠", "❌");

		File file = new File("./src/main/java/main/commands");
		String[] fileList = file.list();
		for (String name : fileList) {
			String cmdName = "main.commands." + stripExtension(name);
			Command x;
			try {
				x = (Command) Class.forName(cmdName).getDeclaredConstructor().newInstance();
				addCommand(x);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException
					| ClassNotFoundException e) {
				//e.printStackTrace();
			}
			

		}

		file = new File("./src/main/java/main/commands/admin");
		fileList = file.list();
		for (String name : fileList) {
			String cmdName = "main.commands.admin." + stripExtension(name);
			Command x;
			try {
				x = (Command) Class.forName(cmdName).getDeclaredConstructor().newInstance();
				addCommand(x);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException
					| ClassNotFoundException e) {
				//e.printStackTrace();
			}
		}
	}
}
