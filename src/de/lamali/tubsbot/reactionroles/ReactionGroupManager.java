package de.lamali.tubsbot.reactionroles;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

import de.lamali.tubsbot.Contants;
import de.lamali.tubsbot.TubsBot;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

public class ReactionGroupManager {

	private ArrayList<ReactionGroup> groups;

	public ReactionGroupManager() {
		groups = new ArrayList<>();
		load();
	}

	public void save() {
		try {					
			FileWriter writer = new FileWriter("reactionrole.txt");
			
			String data = "";
			data += groups.size() + "\n";
			for (ReactionGroup group : groups) {
				data += group.getName() + " " + group.getMaxRoles() + " " + group.getReactionRoles().size() + "\n";
				for (ReactionRole role : group.getReactionRoles()) {
					data += role.getChannelID() + " " + role.getMessageID() + " " + role.getRoleID() + " "	+ role.getEmote() + "\n";
				}
			}
			writer.close();
			
			InputStream is = new ByteArrayInputStream(data.getBytes());

			TubsBot.INSTANCE.getAWSS3().putObject(Contants.BUCKET_NAME, "reactionrole.txt", is, new ObjectMetadata());

			System.out.println("Successfully wrote to the file.");
		} catch (Exception e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		
	}

	public void load() {
		try {
			S3Object object = TubsBot.INSTANCE.getAWSS3().getObject(new GetObjectRequest(Contants.BUCKET_NAME, "reactionrole.txt"));
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(object.getObjectContent()));
			
			String line = reader.readLine();
			int groupCount = Integer.parseInt(line);

			for (int j = 0; j < groupCount; j++) {
				line = reader.readLine();
				String[] args = line.split(" ");
				String groupName = args[0];
				int maxRoles = Integer.parseInt(args[1]);
				int roleCount = Integer.parseInt(args[2]);
				
				ReactionGroup group = new ReactionGroup(groupName, maxRoles);
				add(group);
				for (int i = 0; i < roleCount; i++) {
					line = reader.readLine();
					args = line.split(" ");
					String channelId = args[0];
					String messageID = args[1];
					String roleID = args[2];
					String emote = args[3];
					ReactionRole role = new ReactionRole(emote, channelId, messageID, roleID);
					group.add(role);
				}
			}

			reader.close();
		} catch (Exception e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

	}

	public void addEvent(MessageReactionAddEvent event) {
		String user = event.getUserId();
		for (ReactionGroup group : groups) {
			for (ReactionRole role : group.getReactionRoles()) {
				String channelID = event.getChannel().getId();
				String emote = event.getReactionEmote().getEmoji();
				String messageID = event.getMessageId();

				if (emote.equals(role.getEmote()) && channelID.equals(role.getChannelID())
						&& messageID.equals(role.getMessageID())) {
					event.getGuild().addRoleToMember(user, event.getJDA().getRoleById(role.getRoleID())).queue();
				}
			}
		}
	}

	public void removeEvent(MessageReactionRemoveEvent event) {
		String user = event.getUserId();
		for (ReactionGroup group : groups) {
			for (ReactionRole role : group.getReactionRoles()) {
				String channelID = event.getChannel().getId();
				String emote = event.getReactionEmote().getEmoji();
				String messageID = event.getMessageId();

				if (emote.equals(role.getEmote()) && channelID.equals(role.getChannelID())
						&& messageID.equals(role.getMessageID())) {
					event.getGuild().removeRoleFromMember(user, event.getJDA().getRoleById(role.getRoleID()))
							.queue();
				}
			}
		}
	}

	public boolean add(ReactionGroup group) {
		for (ReactionGroup oGroup : groups) {
			if (oGroup.equals(group)) {
				return false;
			}
		}
		groups.add(group);
		return true;
	}

	public void remove(String name) {
		for (ReactionGroup group : groups) {
			if (group.getName().equals(name)) {
				groups.remove(group);
				return;
			}
		}
	}

	public ReactionGroup getGroup(String name) {
		for (ReactionGroup group : groups) {
			if (group.getName().equals(name)) {
				return group;
			}
		}
		return null;
	}

	public ArrayList<ReactionGroup> getGroups() {
		return groups;
	}
}
