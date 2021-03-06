package edu.cuny.csi.csc330.discordbot.bot;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

public class CreateEmbed {
	
	public static EmbedBuilder embed;
	
	/**
	 * createEmbed - Type 1: Simple Message
	 * 
	 * @param type
	 * 0 - Simple Message
	 * 1 - Error
	 * 
	 * @param args
	 * @return
	 */
	
	public static MessageEmbed make(int value, String message) {
		
		embed = new EmbedBuilder();
		
		switch (value) {
		case 1: // Error type
			embed.setColor(0xff0000);
			embed.setTitle("ERROR");
			break;
		default:
			embed.setColor(0x15992b);
			embed.setTitle("MESSAGE");
			break;
		}
		embed.setDescription(message);
		
		return embed.build();
	}
	
	public static MessageEmbed make(String[] args) {

		embed = new EmbedBuilder();
		
		embed.setColor(0x42f4c2);
		embed.setTitle(args[0]);
		embed.setDescription(args[1]);
		
		
		if (args.length > 2) {
			for (int i = 2; i < args.length; i += 2) {
				embed.addField(args[i], args[i+1], false);
			}
		}
		
		return embed.build();
	}
	
	
	/**
	 * Signature type #2
	 * Used to print information with user footer
	 * 
	 * @param member
	 * @param args
	 * 
	 */
	
	public static MessageEmbed make(User user, String[] args) {
			
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
			return embed.build();
		}
	
	/**
	 * Signature type #3
	 * Used to print information with server footer
	 * 
	 * @param member
	 * @param args
	 * 
	 */
	
	public static MessageEmbed make(Guild guild, String[] args) {
		
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
		return embed.build();
	}
	





}



