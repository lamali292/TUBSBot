package de.lamali.tubsbot.reactionroles;

public class ReactionRole {

	private String emote, messageID, roleID, channelID;
	
	public ReactionRole(String emote, String channelId, String messageID, String roleID) {
		this.emote = emote;
		this.channelID = channelId;
		this.messageID = messageID;
		this.roleID = roleID;
	}

	public String getMessageID() {
		return messageID;
	}

	public String getEmote() {
		return emote;
	}

	public String getRoleID() {
		return roleID;
	}
	
	public String getChannelID() {
		return channelID;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ReactionRole) {
			ReactionRole other = (ReactionRole) obj;
			if (messageID.equals(other.messageID) && roleID.equals(other.roleID) && emote.equals(other.emote)) {
				return true;
			}
		}
		return false;
	}


}
