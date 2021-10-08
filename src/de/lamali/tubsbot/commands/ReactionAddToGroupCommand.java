package de.lamali.tubsbot.commands;


import java.awt.Color;
import java.time.Instant;

import com.vdurmont.emoji.EmojiParser;

import de.lamali.tubsbot.TubsBot;
import de.lamali.tubsbot.commands.types.ServerCommand;
import de.lamali.tubsbot.reactionroles.ReactionGroup;
import de.lamali.tubsbot.reactionroles.ReactionGroupManager;
import de.lamali.tubsbot.reactionroles.ReactionRole;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

public class ReactionAddToGroupCommand implements ServerCommand {

	@Override
	public void performCommand(Member m, TextChannel chan, Message mess) {
		ReactionGroupManager groupMan = TubsBot.INSTANCE.getGroupMan();
		
		
		String[] args = mess.getContentDisplay().split(" ");
		String groupName = args[1];
		String channelID = args[2];
		String messageID = args[3];
		String roleID = args[4];
		String emote = EmojiParser.parseToUnicode(args[5]);
		
		String roleAsMention = mess.getJDA().getRoleById(roleID).getAsMention();
		TextChannel textchannel = mess.getJDA().getTextChannelById(channelID);
		
		ReactionGroup group = groupMan.getGroup(groupName);
		if(group == null) {
			chan.sendMessage("group "+groupName+" doesnt exist").queue();
			mess.delete().queue();
			return;
		}
		
		ReactionRole role = new ReactionRole(emote, channelID, messageID, roleID);
		for (ReactionRole roles : group.getReactionRoles()) {
			if (roles.equals(role)) {
				chan.sendMessage("Role "+roleAsMention+" already exists in group "+groupName).queue();
				mess.delete().queue();
				return;
			}
		}
		
		group.add(role);
	
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(Color.gray);
		embed.setTimestamp(Instant.now());
		embed.setTitle("Added Role to Role Group:");
		embed.setDescription("Added role "+roleAsMention+" to group "+groupName+" with emote "+emote+" in "+textchannel.getAsMention()+"\n");
				
		embed.addField("Command", mess.getContentDisplay(), true);
		
		MessageEmbed mes = embed.build();
		chan.sendMessageEmbeds(mes).queue();
		
		TextChannel channel = chan.getJDA().getTextChannelById(channelID);
		channel.addReactionById(messageID, emote).queue();
		
		groupMan.save();
		mess.delete().queue();
	}

}
