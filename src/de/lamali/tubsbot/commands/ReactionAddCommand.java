package de.lamali.tubsbot.commands;


import de.lamali.tubsbot.commands.types.ServerCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ReactionAddCommand implements ServerCommand {

	@Override
	public void performCommand(Member m, TextChannel chan, Message mess) {
		String[] args = mess.getContentDisplay().split(" ");
		String channelId = args[1];
		String messageId = args[2];
		String emote = args[3];
		System.out.println("channelId: "+channelId);
		System.out.println("messageId: "+messageId);
		System.out.println("emote: "+emote);
		TextChannel channel = chan.getJDA().getTextChannelById(channelId);
		channel.addReactionById(messageId, emote).queue();
	}

}
