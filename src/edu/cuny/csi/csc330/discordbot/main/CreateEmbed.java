package edu.cuny.csi.csc330.discordbot.main;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

public class CreateEmbed {
	
	static EmbedBuilder embed;
	
	/**
	 * Signature type #1
	 * @param args
	 * Element #0 is always interpreted as title
	 * Element #1 is always interpreted as description
	 */
	public static EmbedBuilder createEmbed(String[] args) {
		
		embed = new EmbedBuilder();
		initDefault(args);
		
		return embed;
	}
	
	
	/**
	 * Signature type #2
	 * Used to print user-related information
	 * 
	 * @param member
	 * @param args
	 * 
	 */
	
	public static EmbedBuilder createEmbed(User user, String[] args) {
			
			embed = new EmbedBuilder();
			embed.setTitle(args[0]);
			embed.setDescription(args[1]);
			embed.setColor(0x5f42f4);
			
			if (args.length > 2) {
				for (int i = 2; i < args.length; i += 2) {
					embed.addField(args[i], args[i+1], false);
				}
			}
			
			embed.setFooter("Information provided for " + user.getName(), user.getAvatarUrl());
			return embed;
		}
	
	public static EmbedBuilder createEmbed(Guild guild, String[] args) {
		
		embed = new EmbedBuilder();
		embed.setTitle(args[0]);
		embed.setDescription(args[1]);
		embed.setColor(0x5f42f4);
		
		if (args.length > 2) {
			for (int i = 2; i < args.length; i += 2) {
				embed.addField(args[i], args[i+1], false);
			}
		}
		
		embed.setFooter("Information provided from " + guild.getName() + " server", guild.getIconUrl());
		return embed;
	}
	
	// TODO this should throw exception if args !even or > 0
	private static void initDefault(String[] args) {
		embed.setTitle(args[0]);
		embed.setDescription(args[1]);
		embed.setColor(0x42f4c2);
		
		if (args.length > 2) {
			for (int i = 2; i < args.length; i += 2) {
				embed.addField(args[i], args[i+1], false);
			}
		}

	}
	





}



