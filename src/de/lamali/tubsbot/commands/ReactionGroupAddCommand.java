package de.lamali.tubsbot.commands;

import de.lamali.tubsbot.TubsBot;
import de.lamali.tubsbot.commands.types.ServerCommand;
import de.lamali.tubsbot.reactionroles.ReactionGroup;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ReactionGroupAddCommand implements ServerCommand {

	@Override
	public void performCommand(Member m, TextChannel chan, Message mess) {
		if (!m.hasPermission(Permission.ADMINISTRATOR)) { 
			return;
		}
		String[] args = mess.getContentDisplay().split(" ");
		String name = args[1];
		int maxRoles = 100;
		if(args.length > 2) {
			maxRoles = Integer.parseInt(args[2]);
		}
		ReactionGroup group = new ReactionGroup(name, maxRoles);
		if(TubsBot.INSTANCE.getGroupMan().add(group)) {
			chan.sendMessage("Reaction Group "+name+" added").queue();
		} else {
			chan.sendMessage("Reaction Group "+name+" already exists").queue();
		}
		
		mess.delete().queue();
	}
}
