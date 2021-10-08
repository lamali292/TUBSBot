package de.lamali.tubsbot.commands.types;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;

public interface PrivateCommand {
	public void performCommand(User u, PrivateChannel channel, Message message);
}
