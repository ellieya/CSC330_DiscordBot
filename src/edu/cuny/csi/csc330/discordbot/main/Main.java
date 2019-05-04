package edu.cuny.csi.csc330.discordbot.main;

import java.util.ArrayDeque;
import java.util.Queue;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

public class Main {
	public static JDA jda;
	public static char prefix = '!';
	
	protected static boolean gameInit = false;
	protected static boolean gameStarted = false;
	protected static Guild gameLiveServer;
	protected static Queue<Member> playerQueue = new ArrayDeque<Member>();

	public static void main(String[] args) throws LoginException {

		// To maintain privacy, send bot token through configurations
		jda = new JDABuilder(AccountType.BOT).setToken(args[0]).build();
		jda.getPresence().setGame(Game.playing("!help for command list"));

		jda.addEventListener(new Commands());
	}
}
