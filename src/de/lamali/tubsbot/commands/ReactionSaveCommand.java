package de.lamali.tubsbot.commands;

import de.lamali.tubsbot.TubsBot;
import de.lamali.tubsbot.commands.types.ServerCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ReactionSaveCommand implements ServerCommand{
	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		if (!m.hasPermission(Permission.ADMINISTRATOR)) { 
			return;
		}
		String serverID = channel.getGuild().getId();
		TubsBot.INSTANCE.getGroupMan().save(serverID);
	}

}
