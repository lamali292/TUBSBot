package de.lamali.tubsbot.listener;

import de.lamali.tubsbot.TubsBot;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter{

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String message = event.getMessage().getContentDisplay();
		
		if(event.isFromType(ChannelType.TEXT)) {
			TextChannel channel = event.getTextChannel();
			if(message.startsWith("!")){
				String[] args = message.substring(1).split(" ");
				if(args.length > 0) {
					if(!TubsBot.INSTANCE.getCmdMan().performServerCommand(args[0], event.getMember(), channel, event.getMessage())) {
						channel.sendMessage("Unbekanntes Commando!").queue();
					}
				}	
			}
		} 
		if(event.isFromType(ChannelType.PRIVATE)) {
			PrivateChannel channel = event.getPrivateChannel();
			if(message.startsWith("!")){
				String[] args = message.substring(1).split(" ");
				if(args.length > 0) {
					if(!TubsBot.INSTANCE.getCmdMan().performPrivateCommand(args[0], event.getAuthor(), channel, event.getMessage())) {
						channel.sendMessage("Unbekanntes Commando!").queue();
					}
				}	
			}
		}
	}
	
	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		TubsBot.INSTANCE.getGroupMan().addEvent(event);
	}
	
	
	@Override
	public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
		TubsBot.INSTANCE.getGroupMan().removeEvent(event);
	}
}
