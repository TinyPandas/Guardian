package main.handlers;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;

public class CommandLoader {
	static CommandLoader instance = null;
	
	CommandLoader() {
		instance = this;
	}
	
	public static CommandLoader getInstance() {
		if (instance == null) {
			new CommandLoader();
		}
		
		return instance;
	}
	
	File baseCommandFolder = new File("./src/main/java/main/commands");
	File adminCommandFolder = new File("./src/main/java/main/commands/admin");
	
	private String stripExtension(String str) {
		if (str == null)
			return null;
		int pos = str.lastIndexOf(".");
		if (pos == -1)
			return str;
		return str.substring(0, pos);
	}
	
	private boolean doesFileExistInFolder(File folder, String fileName) {
		String[] fileList = folder.list();
		for (String file:fileList) {
			if (stripExtension(file).equalsIgnoreCase(fileName)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean reloadCommand(CommandClient client, String commandName) {
		if (unloadCommand(client, commandName)) {
			//unloaded success
			if (loadCommand(client, commandName)) {
				//loaded success
				return true;
			}
		}
		
		return false;
	}
	
	public boolean loadCommand(CommandClient client, String commandName) {
		List<Command> commands = client.getCommands();
		
		for (Command command:commands) {
			String cmdName = command.getName();
			
			if (commandName.equalsIgnoreCase(cmdName)) {
				return false;
			}
		}
		
		commandName += "Command";
		
		String cmdName = "";
		Command command;
		
		if (doesFileExistInFolder(baseCommandFolder, commandName)) {
			cmdName = "main.commands." + commandName;
		} else if (doesFileExistInFolder(adminCommandFolder, commandName)) {
			cmdName = "main.commands.admin." + commandName;
		} else {
			return false;
		}
		
		try {
			command = (Command) Class.forName(cmdName).getDeclaredConstructor().newInstance();
			client.addCommand(command);
			return true;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean unloadCommand(CommandClient client, String commandName) {
		List<Command> commands = client.getCommands();
		
		for (Command command:commands) {
			String cmdName = command.getName();
			
			if (commandName.equalsIgnoreCase(cmdName)) {
				client.removeCommand(commandName);
				return true;
			}
		}
		
		return false;
	}
}
