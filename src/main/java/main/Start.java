package main;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.security.auth.login.LoginException;

import org.apache.commons.lang3.StringUtils;

import main.database.DBManager;
import main.handlers.CommandHandler;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class Start {
	private JDA api;
	
	public Start(String token) {
		if (System.getProperty("file.encoding").equalsIgnoreCase("UTF-8")) {
			new DBManager(); //Simply start the DBManager.
			
			setupBot(token);
		} else {
			try {
				relaunchInUTF8();
			} catch (UnsupportedEncodingException | InterruptedException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}
	
	private void setupBot(String token) {
		try {
			JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT).setToken(token);
			jdaBuilder.setActivity(Activity.playing("Beta_0.2"));
			
			jdaBuilder.addEventListeners(new CommandHandler().build()); //Instantiate a new CommandHandler, then build and add to JDA instance.
			
			api = jdaBuilder.build(); //Finalize setup of the JDA instance
			api.awaitReady(); //Wait for Discord connection to setup.
		} catch (InterruptedException | LoginException e) {
			e.printStackTrace();
			System.exit(0);
		} 
	}
	
	private File getThisJarFile() throws UnsupportedEncodingException {
		String path = Start.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String decodedPath = URLDecoder.decode(path, "UTF-8");
		
		if (!decodedPath.endsWith(".jar")) {
			return new File("SHBot.jar");
		}
		
		return new File(decodedPath);
	}
	
	private void relaunchInUTF8() throws UnsupportedEncodingException, InterruptedException {
		String[] command = new String[] {"java", "-Dfile.encoding=UTF-8", "-jar", getThisJarFile().getAbsolutePath()};
		
		//Relaunched the bot using UTF-8 mode
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		processBuilder.inheritIO(); //Tells the new process to use the same command line as this one.
		try {
			Process process = processBuilder.start();
			process.waitFor(); //Wait until actual bot stops. This is to allow same command line usage.
			System.exit(process.exitValue());
		} catch (IOException e) {
			if (e.getMessage().contains("\"java\""))
            {
                System.err.println("BotLauncher: There was an error relaunching the bot. We couldn't find Java to launch with.");
                System.err.println("BotLauncher: Attempted to relaunch using the command:\n   " + StringUtils.join(command, " ", 0, command.length));
                System.err.println("BotLauncher: Make sure that you have Java properly set in your Operating System's PATH variable.");
                System.err.println("BotLauncher: Stopping here.");
            }
            else
            {
                e.printStackTrace();
            }
		}
	}
	
	public static void main(String[] args) {
		if (args.length > 0) {
			new Start(args[0]);
		} else {
			System.out.println("No token provided.");
			System.exit(0);
		}
	}
}
