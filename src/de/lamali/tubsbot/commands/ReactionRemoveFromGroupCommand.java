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
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

public class ReactionRemoveFromGroupCommand implements ServerCommand {

	@Override
	public void performCommand(Member m, TextChannel chan, Message mess) {
		if (!m.hasPermission(Permission.ADMINISTRATOR)) { 
			return;
		}
		ReactionGroupManager groupMan = TubsBot.INSTANCE.getGroupMan();
		String serverID = chan.getGuild().getId();
		
		String[] args = mess.getContentDisplay().split(" ");
		String groupName = args[1];
		String emote = EmojiParser.parseToUnicode(args[2]);
		ReactionGroup group = groupMan.getGroup(serverID, groupName);
		
		if(group == null) {
			chan.sendMessage("group "+groupName+" doesnt exist").queue();
			mess.delete().queue();
			return;
		}
			
		ReactionRole role = group.getFromEmote(emote);
		group.remove(role);
		
		String roleAsMention = mess.getJDA().getRoleById(role.getRoleID()).getAsMention();
		TextChannel textchannel = mess.getJDA().getTextChannelById(role.getChannelID());
	
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(Color.gray);
		embed.setTimestamp(Instant.now());
		embed.setTitle("Removed Role to Role Group:");
		embed.setDescription("Removed role "+roleAsMention+" from group "+groupName+" with emote "+emote+" in "+textchannel.getAsMention()+"\n");
				
		embed.addField("Command", mess.getContentDisplay(), true);
		
		MessageEmbed mes = embed.build();
		chan.sendMessageEmbeds(mes).queue();
		
		textchannel.removeReactionById(role.getMessageID(), emote).queue();
		
		groupMan.save(serverID);
		mess.delete().queue();
	}

}
