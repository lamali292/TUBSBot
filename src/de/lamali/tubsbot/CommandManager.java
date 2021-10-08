package de.lamali.tubsbot;

import java.util.concurrent.ConcurrentHashMap;

import de.lamali.tubsbot.commands.*;
import de.lamali.tubsbot.commands.types.*;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class CommandManager {
	public ConcurrentHashMap<String, ServerCommand> serverCommands;
	public ConcurrentHashMap<String, PrivateCommand> privateCommands;

	public CommandManager() {
		this.serverCommands = new ConcurrentHashMap<>();
		this.privateCommands = new ConcurrentHashMap<>();
		
		this.serverCommands.put("help", new HelpCommand());
		this.serverCommands.put("rgadd", new ReactionGroupAddCommand());
		this.serverCommands.put("rglist", new ReactionGroupListCommand());
		this.serverCommands.put("addrole", new ReactionAddToGroupCommand());
		this.serverCommands.put("save", new ReactionSaveCommand());
		this.serverCommands.put("load", new ReactionLoadCommand());
	}

	public boolean performServerCommand(String command, Member m, TextChannel channel, Message message) {
		ServerCommand cmd;

		if ((cmd = this.serverCommands.get(command.toLowerCase())) != null) {
			cmd.performCommand(m, channel, message);
			return true;
		}

		return false;
	}
	
	public boolean performPrivateCommand(String command, User m, PrivateChannel channel, Message message) {
		PrivateCommand cmd;

		if ((cmd = this.privateCommands.get(command.toLowerCase())) != null) {
			cmd.performCommand(m, channel, message);
			return true;
		}

		return false;
	}
}
