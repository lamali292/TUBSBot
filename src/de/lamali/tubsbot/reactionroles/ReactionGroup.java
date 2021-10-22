package de.lamali.tubsbot.reactionroles;

import java.util.ArrayList;

public class ReactionGroup {
	private ArrayList<ReactionRole> group;
	private int maxRoles;
	private String name;
	
	public ReactionGroup(String name, int maxRoles) {
		this.group = new ArrayList<>();
		this.maxRoles = maxRoles;
		this.name = name;
	}
	
	public void add(ReactionRole role) {
		group.add(role);
	}
	
	public void remove(ReactionRole role) {
		group.remove(role);
	}
	
	public ReactionRole getFromEmote(String emote) {
		for (ReactionRole role : group) {
			if (role.getEmote().equals(emote)) {
				return role;
			}
		}
		return null;
	}
	
	public ArrayList<ReactionRole> getReactionRoles() {
		return group;
	}

	public int getMaxRoles() {
		return maxRoles;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ReactionGroup) {
			ReactionGroup other = (ReactionGroup) obj;
			return name.equals(other.name);
		}
		return false;
	}

}