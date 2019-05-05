package edu.cuny.csi.csc330.discordbot.main;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

public class Main {
	public static JDA jda;
	public static char prefix = '!';
	
	protected static boolean gameInit = false;
	protected static boolean gameStarted = false;
	protected static Guild gameLiveServer;
	protected static Queue<User> playerQueue = new ArrayDeque<User>();
	protected static Map<Long, User> queueMap = new HashMap<Long, User>(); //used to keep track of users that are already on the playerQueue

	public static void main(String[] args) throws LoginException {

		// To maintain privacy, send bot token through configurations
		jda = new JDABuilder(AccountType.BOT).setToken(args[0]).build();
		jda.getPresence().setGame(Game.playing("!help for command list"));

		jda.addEventListener(new Commands());
	}
}
