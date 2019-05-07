package edu.cuny.csi.csc330.discordbot.bot;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

public class CreateEmbed {
	
	//TODO all of them should throw exception if args !even or > 0, except for the simple message version
	
	static EmbedBuilder embed;
	
	
	/**
	 * Signature type #0
	 * Takes String array of many
	 * 
	 * @param args
	 * 
	 */
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
	 * createEmbed - Type 1: Simple Message
	 * Key diff - Only takes ONE string
	 * 
	 * @param type
	 * 0 - Simple Message
	 * 1 - Error
	 * 
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
	
	
	/**
	 * Signature type #2
	 * Used to print information with user footer
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



