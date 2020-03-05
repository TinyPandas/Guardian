package main.commands.admin;

import java.util.HashMap;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.lib.Constants;
import main.lib.Utils;
import net.dv8tion.jda.api.EmbedBuilder;

public class RulesCommand extends Command {
	private static HashMap<String, String> rulesByIndex = new HashMap<>();
	
	public RulesCommand() {
		name = "rules";
		help = "displays rule of given name.";
		guildOnly = true;
		category = new Category("staff");
		
		rulesByIndex.put("guidelines", "Global Guidelines are enforced. https://scriptinghelpers.org/help/community-guidelines");
		rulesByIndex.put("nsfw", "Sexually explicit, strongly suggestive, and/or gory media is not allowed.");
		rulesByIndex.put("ads", "Excessive advertisements, excessive rapid messaging, special characters which surpass the intended boundaries of a discord message, or the excessive mentioning of a user with intent to annoy constitutes as spam.");
		rulesByIndex.put("advertise", "Excessive advertisements, excessive rapid messaging, special characters which surpass the intended boundaries of a discord message, or the excessive mentioning of a user with intent to annoy constitutes as spam.");
		rulesByIndex.put("spam", "Excessive advertisements, excessive rapid messaging, special characters which surpass the intended boundaries of a discord message, or the excessive mentioning of a user with intent to annoy constitutes as spam.");
		rulesByIndex.put("attacks", "Derogatory terms or attacks against other individuals is not allowed. Targeting of users based on race, religion, sexual orientation, gender, nationality, or other form of class is prohibited.");
		rulesByIndex.put("wait", "Wait for assistance if you need help with scripting. Do not mention users or staff to help you or directly message them unsolicited. Do not use the ;request command for assistance. Assistance with Scripting Helpers may be sought after at https://scriptinghelpers.org/ask or in the #questions-1 channel.");
		rulesByIndex.put("mentions", "Wait for assistance if you need help with scripting. Do not mention users or staff to help you or directly message them unsolicited. Do not use the ;request command for assistance. Assistance with Scripting Helpers may be sought after at https://scriptinghelpers.org/ask or in the #questions-1 channel.");
		rulesByIndex.put("addition", "Depending on the channel, additional rules apply. See channel descriptions for details.");
		rulesByIndex.put("extra", "Depending on the channel, additional rules apply. See channel descriptions for details.");
		rulesByIndex.put("appeal", "The spirit of the rules, and not the technical wording of these rules, are enforced. You may appeal mutes by directly messaging \"Administrator\" or \"Super Administrator\" role. Bans may be appealed or complaints may be filed by contacting support at https://scriptinghelpers.org/contact. You must contact us via email assigned to an account on Scripting Helpers for release of actions details or revocation of suspension in concern with the affiliated account.");
		rulesByIndex.put("logged", "Messages edited or deleted, voice channels joined and left, server entry, and moderation records amended are all recorded for moderation purposes and quality assurance.");
		rulesByIndex.put("nickname", "The nickname bypass role is a privilege and is arbitrarily given. If you're given this role, keep the nickname in good taste and within reason.");
		rulesByIndex.put("alt", "Use of alternate accounts or rejoining the server to evade a mute or ban in this server will result in the ban of the alternate account. If you wish to have a ban appealed, contact Scripting Helpers support at https://scriptinghelpers.org/contact.");
		rulesByIndex.put("troll", "Any post which is intended to invoke a negative, hurtful, annoyed, or baited response in which a reasonable person may conclude invokes such baited response may be handled at the discretion of the moderators. (E.g, excessive trolling or 'meme-ing')");
		rulesByIndex.put("discretion", "Any post which is intended to invoke a negative, hurtful, annoyed, or baited response in which a reasonable person may conclude invokes such baited response may be handled at the discretion of the moderators. (E.g, excessive trolling or 'meme-ing')");
		rulesByIndex.put("commonsense", "Overall, common sense is strongly encouraged. If the mods ask you to stop doing something, it's probably best to listen.");
		rulesByIndex.put("selfbot", " Selfbots are heavily discouraged here, we'll take necessary action towards use and abuse of selfbots.");
		rulesByIndex.put("request", "Mention the staff role or use the ;request command to report abuse. The ;request [reason] command is to be used to report abuse only. Do not mass mention us to help with a script.");
		rulesByIndex.put("discord", "All Discord Rules, Terms of Service, and Policies are enforced. https://discordapp.com/guidelines and https://discordapp.com/terms.");
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if (!(Utils.hasRoleWithName(event.getMember(), "staff"))) {
			return;
		}

		boolean flag = false;
		event.getMessage().delete().queue();
		
		if (event.getArgs().length() > 0) {
			flag = true;
			String index = event.getArgs();
			if (rulesByIndex.containsKey(index)) {
				event.reply(rulesByIndex.get(index));
			} else {
				event.getMember().getUser().openPrivateChannel().queue(pc -> {
					EmbedBuilder builder = new EmbedBuilder();
					builder.setTitle("Rule shortcuts");
					
					for (String key:rulesByIndex.keySet()) {
						String desc = rulesByIndex.get(key);
						
						builder.addField(key, desc, false);
					}
					
					pc.sendMessage(builder.build()).queue();
				});
			}
		}
		
		if (!flag) {
			event.reply("Please view the " + event.getGuild().getTextChannelById(Constants.rulesID) + " channel.");
		}
	}
	
	public static HashMap<String, String> getRulesIndex() {
		return rulesByIndex;
	}
}
