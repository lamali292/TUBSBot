package de.lamali.tubsbot.commands;

import java.awt.Color;
import java.time.Instant;

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

public class ReactionGroupListCommand implements ServerCommand {

	@Override
	public void performCommand(Member m, TextChannel chan, Message mess) {
		ReactionGroupManager groupMan = TubsBot.INSTANCE.getGroupMan();
		String[] args = mess.getContentDisplay().split(" ");
		String serverID = chan.getGuild().getId();
		if (args.length == 1) {
			String names = "";
			for (ReactionGroup group : groupMan.getGroups(serverID)) {
				names = names + group.getName() + "\n";
			}
			
			EmbedBuilder embed = new EmbedBuilder();
			embed.setColor(Color.gray);
			embed.setTimestamp(Instant.now());
			embed.setTitle("Reaction Groups:");
			embed.setDescription(names);
							
			MessageEmbed mes = embed.build();
			chan.sendMessageEmbeds(mes).queue();
			mess.delete().queue();
		
		} else {
			String name = args[1];
			ReactionGroup group = groupMan.getGroup(serverID, name);
			if(group != null) {
				String emotes = "";
				String roles = "";
				for (ReactionRole role : group.getReactionRoles()) {
					emotes = emotes + role.getEmote()+"\n";
					String roleAsMention = mess.getJDA().getRoleById(role.getRoleID()).getAsMention();
					roles = roles + roleAsMention+"\n";
				}
				
				EmbedBuilder embed = new EmbedBuilder();
				embed.setColor(Color.gray);
				embed.setTimestamp(Instant.now());
				embed.setTitle("Reaction Groups:");
				embed.setDescription(name);
							
				embed.addField("Roles", emotes, true);
				embed.addField("", roles, true);
				
				MessageEmbed mes = embed.build();
				chan.sendMessageEmbeds(mes).queue();
				mess.delete().queue();
			}
		}
	}

}
