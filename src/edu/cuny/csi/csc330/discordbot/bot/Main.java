package edu.cuny.csi.csc330.discordbot.bot;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;



public class Main {
	public static JDA jda;
	public static char prefix = '!';
	
	private static long TURN_TIME = 90;
	
	protected final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	protected static boolean gameInit = false;
	protected static boolean gameStarted = false;
	protected static Guild gameLiveServer;
	protected static TextChannel gameLiveChannel;
	protected static Queue<Long> playerQueue = new ArrayDeque<Long>();
	protected static Map<Long, User> queueMap = new HashMap<Long, User>(); //used to keep track of users that are already on the playerQueue
	protected static Game game;
	protected static ScheduledFuture<?> nextScheduledGameEvent;
	protected static ScheduledFuture<?> nextScheduledTurnEvent;
	
	private static Runnable runGameTurnEnd = new Runnable() {
		public void run() {
			game.turnEnd();
			if (!game.isGameDone()) {
				gameLiveChannel.sendMessage(CreateEmbed.make(0, "Turn " + game.getTurnCount() + "!")).queue();
			} else {
				nextScheduledTurnEvent.cancel(true);
				killGame();
			}
		}
	};
	
	protected static void turn() {
		gameLiveChannel.sendMessage(CreateEmbed.make(0, "Turn " + game.getTurnCount() + "!")).queue();
		nextScheduledTurnEvent = scheduler.scheduleAtFixedRate(runGameTurnEnd, TURN_TIME, TURN_TIME, SECONDS);
	}
	
	protected static boolean killGame() {
		
		if (!Main.gameStarted) {
			gameLiveChannel.sendMessage(CreateEmbed.make(1, "There is no game instance to kill...")).queue();
			return false;
		}
		else {

			// Print ending message
			gameLiveChannel.sendMessage(CreateEmbed.make(0, Main.game.endGame())).queue();
			

			// Destroy nextScheduledTurnEvent
			Main.nextScheduledTurnEvent.cancel(true);
			Main.nextScheduledGameEvent = null;

			// Destroy game instance in Main by making it equal to null
			Main.game = null;

			// Destroy gameLiveServer
			Main.gameLiveServer = null;

			// Switch game flag
			Main.gameStarted = false;

			// Switch init flag
			Main.gameInit = false;

			return true;
		}
	}
	
	public static void main(String[] args) throws LoginException {

		// To maintain privacy, send bot token through configurations
		jda = new JDABuilder(AccountType.BOT).setToken(args[0]).build();
		jda.getPresence().setGame(net.dv8tion.jda.core.entities.Game.playing("!help for command list"));

		jda.addEventListener(new Commands());
	}
	
	
}
