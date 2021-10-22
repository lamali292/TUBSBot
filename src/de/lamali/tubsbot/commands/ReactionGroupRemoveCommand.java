package de.lamali.tubsbot.commands;

import de.lamali.tubsbot.TubsBot;
import de.lamali.tubsbot.commands.types.ServerCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ReactionGroupRemoveCommand implements ServerCommand {

	@Override
	public void performCommand(Member m, TextChannel chan, Message mess) {
		if (!m.hasPermission(Permission.ADMINISTRATOR)) { 
			return;
		}
		String serverID = chan.getGuild().getId();
		String[] args = mess.getContentDisplay().split(" ");
		String name = args[1];		
		if(TubsBot.INSTANCE.getGroupMan().remove(serverID, name)) {
			chan.sendMessage("Reaction Group "+name+" removed").queue();
		} else {
			chan.sendMessage("Reaction Group "+name+" doesnt exist").queue();
		}
		
		mess.delete().queue();
	}
}
