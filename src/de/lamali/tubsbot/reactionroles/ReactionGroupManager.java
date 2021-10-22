package de.lamali.tubsbot.reactionroles;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

import de.lamali.tubsbot.Contants;
import de.lamali.tubsbot.TubsBot;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

public class ReactionGroupManager {

	private HashMap<String, ArrayList<ReactionGroup>> serverCache;

	public ReactionGroupManager() {
		serverCache = new HashMap<>();
	}

	public void save(String serverID) {
		ArrayList<ReactionGroup> groups = getGroups(serverID);
		try {					
	
			String data = "";
			data += groups.size() + "\n";
			for (ReactionGroup group : groups) {
				data += group.getName() + " " + group.getMaxRoles() + " " + group.getReactionRoles().size() + "\n";
				for (ReactionRole role : group.getReactionRoles()) {
					data += role.getChannelID() + " " + role.getMessageID() + " " + role.getRoleID() + " "	+ role.getEmote() + "\n";
				}
			}
			
			InputStream is = new ByteArrayInputStream(data.getBytes());

			TubsBot.INSTANCE.getAWSS3().putObject(Contants.BUCKET_NAME, serverID+".txt", is, new ObjectMetadata());

			System.out.println("Successfully wrote to the file.");
		} catch (Exception e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		
	}

	public void load(String serverID) {
		ArrayList<ReactionGroup> groups = serverCache.get(serverID);
		if (groups == null) {
			groups = new ArrayList<>();
			serverCache.put(serverID, groups);
		}
		try {
			S3Object object = TubsBot.INSTANCE.getAWSS3().getObject(new GetObjectRequest(Contants.BUCKET_NAME, serverID+".txt"));
			System.out.println("Loaded File for Server "+serverID);
			
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
				add(serverID, group);
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
		String serverID = event.getGuild().getId();
		ArrayList<ReactionGroup> groups = getGroups(serverID);
		String user = event.getUserId();
		for (ReactionGroup group : groups) {
			for (ReactionRole role : group.getReactionRoles()) {
				String channelID = event.getChannel().getId();
				String emote = event.getReactionEmote().getEmoji();
				String messageID = event.getMessageId();

				if (emote.equals(role.getEmote()) && channelID.equals(role.getChannelID())
						&& messageID.equals(role.getMessageID())) {
					event.getGuild().addRoleToMember(user, event.getJDA().getRoleById(role.getRoleID())).queue();
					return;
				}
			}
		}
	}

	public void removeEvent(MessageReactionRemoveEvent event) {
		String serverID = event.getGuild().getId();
		ArrayList<ReactionGroup> groups = getGroups(serverID);
		
		String user = event.getUserId();
		for (ReactionGroup group : groups) {
			for (ReactionRole role : group.getReactionRoles()) {
				String channelID = event.getChannel().getId();
				String emote = event.getReactionEmote().getEmoji();
				String messageID = event.getMessageId();

				if (emote.equals(role.getEmote()) && channelID.equals(role.getChannelID())
						&& messageID.equals(role.getMessageID())) {
					event.getGuild().removeRoleFromMember(user, event.getJDA().getRoleById(role.getRoleID())).queue();
					return;
				}
			}
		}
	}

	public boolean add(String serverID, ReactionGroup group) {
		ArrayList<ReactionGroup> groups = getGroups(serverID);
		for (ReactionGroup oGroup : groups) {
			if (oGroup.equals(group)) {
				return false;
			}
		}
		groups.add(group);
		return true;
	}

	public boolean remove(String serverID, String name) {
		ArrayList<ReactionGroup> groups = getGroups(serverID);
		for (ReactionGroup group : groups) {
			if (group.getName().equals(name)) {
				groups.remove(group);
				return true;
			}
		}
		return false;
	}

	public ReactionGroup getGroup(String serverID, String name) {
		ArrayList<ReactionGroup> groups = getGroups(serverID);
		for (ReactionGroup group : groups) {
			if (group.getName().equals(name)) {
				return group;
			}
		}
		return null;
	}

	public ArrayList<ReactionGroup> getGroups(String serverID) {
		ArrayList<ReactionGroup> groups = serverCache.get(serverID);
		if (groups == null) {
			load(serverID);
			return serverCache.get(serverID);
		}
		return groups;
	}
}
