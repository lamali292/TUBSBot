package de.lamali.tubsbot.commands;

import java.awt.Color;
import java.time.Instant;

import de.lamali.tubsbot.commands.types.ServerCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

public class HelpCommand implements ServerCommand{
	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		String[] args = message.getContentDisplay().split(" ");
		if (args.length > 0) {
			EmbedBuilder embed = new EmbedBuilder();
			embed.setColor(Color.gray);
			embed.setTimestamp(Instant.now());
			embed.setTitle("Commands:");
			embed.setDescription("[]: required \n (): unrequired");
			
			embed.addField("Stuff", "!help \n !groupAdd [name] (maxRoles)\n !addRole [name] [channelID] [messageID] [roleID] [emote]", true);
			embed.addField("", "open help menu \n add new ReactionRoleGroup\n add role to role group", true);
			
			MessageEmbed mes = embed.build();
			channel.sendMessageEmbeds(mes).queue();
			message.delete().queue();
		}
	}
	
}