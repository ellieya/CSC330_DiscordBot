package edu.cuny.csi.csc330.discordbot.bot;


import java.util.ArrayDeque;
import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.HashMap;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter {
	
	private final String TOO_FEW_ARG_GENERIC = "Too few arguments in command!";
	private final String ERR_DEAD_MESSAGE = "Your unit is dead!";
	private final String ERR_COMMAND_DNE = "Command does not exist. Please refer to [commands list](https://docs.google.com/document/d/1WBoUiqq8vrASRE-_-BIeKkW0Gw-S6Cs3g6vtbOYLScM/edit?usp=sharing).";
	private final String ERR_NO_GAME_INIT = "No game has been initialized!";
	private final String ERR_GAME_ALREADY_STARTED = "Game has already started!";
	private final String ERR_NO_GAME_START = "Game has not started!";
	private final String ERR_NOT_IN_CURRENT_GAME = "You are not in the current game! Please wait for the next game...";
	private final String ERR_UNIT_DNE = "Unit does not exist.";
	
	private final int QUEUE_REQUIREMENT = 1; // TODO Change to 3 for deployment
	
	private TextChannel channel;
	private Guild guild;
	private Member member;
	private User user;
	private String[] args;
	private Player player;
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		channel = event.getChannel();
		guild = channel.getGuild();
		member = event.getMember();
		user = event.getAuthor();
		
		
		//Don't bother getting message if user is bot. Also avoids infinite loop if any messages from bot happen to start with !
		if (!user.isBot())
			args = event.getMessage().getContentRaw().split("\\s+");
		
		//Is message starting with desired char? If yes, then it is command...
		if (args[0].charAt(0) == Main.prefix) {
			
			//Fix the first arg
			String holder = "";
			for (int i = 1; i < args[0].length(); i++) {
				holder += args[0].charAt(i);
			}
			args[0] = holder;
			
			//Commands
			switch (args[0]) {
			//Owner-Type Commands
			case "init":
				ownerCommand(0);
				break;
			case "extend_time":
				ownerCommand(1);
				break;
			case "force_start":
				ownerCommand(2);
				break;
			case "force_kill":
				ownerCommand(3);
				break;
			case "cancel_init":
				ownerCommand(4);
				break;
				
			//Out-of-game Commands
			case "help":
				outgameCommand(0);
				break;
			case "join":
				outgameCommand(1);
				break;
			case "next_game":
				outgameCommand(2);
				break;
			
			//In-game commands
			case "check":
				ingameCommand(0);
				break;
			case "action":
				ingameCommand(1);
				break;
			
				
			default:
				channel.sendMessage(CreateEmbed.make(1, ERR_COMMAND_DNE)).queue();
			}
		}
	}
	
	private String genTooFewArgMsg(int neededLen) {
		int curPosition = neededLen-1;
		String holder = "Command '";
		
		for (int i = 0; i < curPosition; i++) {
			holder += args[i];
			if (i != curPosition - 1){
				holder += " ";
			}
		}
		
		holder += "' requires " + String.valueOf(neededLen) + " arguments!";
		
		return holder;
	}
	
	private boolean preStartCheck() {
		if (Main.playerQueue.size() < QUEUE_REQUIREMENT) {
			channel.sendMessage(CreateEmbed.make(1, "Game has less than "+ QUEUE_REQUIREMENT + " members on the queue. Cannot start.\nTime has been extended.")).queue(); {
				Main.nextScheduledGameEvent = Main.scheduler.schedule(runStartGame, 10, SECONDS);
			}
			return false;
		} else {
			return true;
		}
	}
	
	private void startGame() {
		//If there is a scheduled event, cancel it
		Main.nextScheduledGameEvent.cancel(true);
		Main.nextScheduledGameEvent = null;
		
		//Create new instance of game that sends over ArrayQueue
		Main.game = new Game(Main.playerQueue);
		
		//Message
		channel.sendMessage(CreateEmbed.make(0, "Game has started!")).queue();
		
		//Switch game flag
		Main.gameStarted = true;
		
		//Destroy playerQueue-related objects and replace with new empty 
		Main.playerQueue = new ArrayDeque<Long>();
		Main.queueMap = new HashMap<Long, User>();
		
		//Run Main.turn()
		Main.turn();
		
	}

	private Runnable runStartGame = new Runnable() {
		public void run() {
			if (preStartCheck())
				startGame();
		}
	};
	
	private void ownerCommand(int value) {
		
		String commandName = "!" + args[0];
		
		if (member.isOwner()) {
			switch (value) {
			case 0:
				
				/* TODO
				* init, and forcestart should not be able to be used when game is started.
				*/
				commandInit();
				break;
			case 1:
				commandExtendTime();
				break;
			case 2:
				commandForceStart();
				break;
			case 3:
				commandForceKill();
				break;
			case 4:
				commandCancelInit();
			}
		} else {
			channel.sendMessage(CreateEmbed.make(1, "\'"+ commandName + "\' command can only be used by owner!")).queue();
		}
	}

	private void outgameCommand(int value) {
		
		if (!Main.gameStarted || value == 0) {
			switch (value) {
			case 0:
				commandHelp();
				break;
			case 1:
				commandJoin();
				break;
			case 2:
				commandNextGame();
				break;
			default:
				System.err.println("ERROR");
			}
		} else {
			channel.sendMessage(CreateEmbed.make(1, "A game is currently in progress on server '" + Main.gameLiveServer.getName() + "'. This command cannot be used while a game is in progress.")).queue();
		}
	}

	private void ingameCommand(int value) {

		if (!Main.gameInit) {
			channel.sendMessage(CreateEmbed.make(1, ERR_NO_GAME_INIT)).queue();
		} else if (!Main.gameStarted) {
			channel.sendMessage(CreateEmbed.make(1, ERR_NO_GAME_START)).queue();
		} else {

			if (Main.game.playerMap.get(user.getIdLong()) != null) {
				
				player = Main.game.playerMap.get(user.getIdLong());
				
				switch (value) {
				case 0:
					commandCheck();
					break;
				case 1:
					commandAction();
				}
			}
			else {
				channel.sendMessage(CreateEmbed.make(1, ERR_NOT_IN_CURRENT_GAME)).queue();
			}
		}
	}

	private void commandInit() {
		if (!Main.gameInit && !Main.gameStarted) {
			Main.gameInit = true;
			Main.gameLiveServer = guild;
			Main.gameLiveChannel = channel;
			
			Main.nextScheduledGameEvent = Main.scheduler.schedule(runStartGame, 10, SECONDS);
			channel.sendMessage(CreateEmbed.make(0, "Command success! Game will be starting in 10 seconds from now.")).queue();
		} else {
			channel.sendMessage(CreateEmbed.make(1, "Init flag already up in " + Main.gameLiveServer.getName()
					+ " server.\nPlease wait until the game has concluded.")).queue();
		}
	}
	
	private void commandExtendTime() {
		
		if (!Main.gameStarted) {
			
			long remainingTime = Main.nextScheduledGameEvent.getDelay(SECONDS);
			
			Main.nextScheduledGameEvent.cancel(true);
			channel.sendMessage(CreateEmbed.make(0, "Command success! Time has been extended by 10 seconds.")).queue();
			Main.nextScheduledGameEvent = Main.scheduler.schedule(runStartGame, 10 + remainingTime, SECONDS);
		} else {
			channel.sendMessage(CreateEmbed.make(1, ERR_GAME_ALREADY_STARTED)).queue();
		}
	}
	
	// TODO enforce that init flag must be up in order to start
	private void commandForceStart() {
		if (Main.gameInit && !Main.gameStarted) {
			startGame();
		} else if (!Main.gameInit) {
			channel.sendMessage(CreateEmbed.make(1, ERR_NO_GAME_INIT)).queue();
		}
		else {
			channel.sendMessage(CreateEmbed.make(1, ERR_GAME_ALREADY_STARTED)).queue();
		}
	}
	
	private void commandForceKill() {
		if (Main.killGame())
			channel.sendMessage(CreateEmbed.make(0, "Kill successful!")).queue();
	}
	
	private void commandJoin() {
		if (!Main.gameInit) {
			channel.sendMessage(CreateEmbed.make(1, ERR_NO_GAME_INIT)).queue();
		} else {
			
			//If the user has not already been queued, then put them into the playerQueue
			//Otherwise, print error message
			if (Main.queueMap.get(user.getIdLong()) == null) {
				Main.playerQueue.add(user.getIdLong());
				Main.queueMap.put(user.getIdLong(), user);
				channel.sendMessage(CreateEmbed.make(0, member.getAsMention() + " has been added to the playerQueue!")).queue();
			} else {
				channel.sendMessage(CreateEmbed.make(1, member.getAsMention() + " - you are already on the queue!")).queue();
			}
		}
	}
	
	private void commandCancelInit() {
		if (Main.gameInit) {
			Main.nextScheduledGameEvent.cancel(true);
			channel.sendMessage(CreateEmbed.make(0, "Queuing has been canceled.")).queue();
		} else {
			channel.sendMessage(CreateEmbed.make(1, "There is no queue to cancel...")).queue();
		}
	}

	private void commandHelp() {
		channel.sendMessage(CreateEmbed.make(new String[] {
				"** * * HELP * * **",
				"_Useful resources related to the game_",
				"**How to Play?**",
				"[Link to Commands](https://docs.google.com/document/d/1WBoUiqq8vrASRE-_-BIeKkW0Gw-S6Cs3g6vtbOYLScM/edit?usp=sharing)",
				"**Development Documentation**",
				"[Link to UML](https://drive.google.com/file/d/1u46U_4y0NRcFlcnnSxzvNUAra7sp3A28/view?usp=sharing) _(Save to drive & open with [draw.io](https://www.draw.io/) for clearer view)_\n"
				+ "[Link to OneNote](https://cixcsicuny-my.sharepoint.com/:o:/g/personal/jiali_chen_cix_csi_cuny_edu/EgL1A0uM7INEl3Vxz1p9ufsBWpwMMRULkCqCiNiF94uuYg?e=PqjOwP)\n"
				+ "[Link to PPT](https://docs.google.com/presentation/d/1VRikxkKxs6pOuRElx6Q2X1VH6noINu65Qfdn1FVTVuk/edit?usp=sharing)"
				+ "[Link to Document](https://docs.google.com/document/d/1SWWJSwb7y5oTOTyxqz0nwcuiL1OM_RUAzmLpFRQrF0c/edit?ouid=109096553479654508712&usp=docs_home&ths=true)"
				})).queue();
	}

	private void commandNextGame() {
		if (Main.nextScheduledGameEvent != null)
			channel.sendMessage(CreateEmbed.make(0, "The next game will be starting in " + Main.nextScheduledGameEvent.getDelay(SECONDS) + " seconds.")).queue();
		else
			channel.sendMessage(CreateEmbed.make(1, "There is no game scheduled at the moment.")).queue();
	}

	private void commandCheck() {
		if (args.length < 2) {
			channel.sendMessage(CreateEmbed.make(1, genTooFewArgMsg(2))).queue();
		} else {
			switch (args[1]) {
			case "faction":
				commandCheckFaction();
				break;
			case "turn":
				commandCheckTurn();
				break;
			case "AP":
			case "ap":
				commandCheckAP();
				break;
			case "unit":
				commandCheckUnit();
				break;
			default:
				channel.sendMessage(CreateEmbed.make(1, ERR_COMMAND_DNE)).queue();
			}
		}
	}

	private void commandCheckFaction() {
		
		user.openPrivateChannel().queue((channel) ->
		{
			channel.sendMessage(CreateEmbed.make(guild, new String[] {"**FACTION**", "Your faction is " + player.getFaction()})).queue();
		});
	}

	private void commandCheckTurn() {
		if (args.length < 3) {
			channel.sendMessage(CreateEmbed.make(1, genTooFewArgMsg(3))).queue();
		} else {
			switch (args[2]) {
			case "end":
				commandCheckTurnEnd();
				break;
			case "count":
				commandCheckTurnCount();
				break;
			default:
				channel.sendMessage(CreateEmbed.make(1, ERR_COMMAND_DNE)).queue();
			}
		}
	}
	
	private void commandCheckTurnEnd() {
		user.openPrivateChannel().queue((channel) ->
		{
			channel.sendMessage(CreateEmbed.make(guild, new String[] {"**TURN END**", "Turn will end in " + Main.nextScheduledTurnEvent.getDelay(SECONDS) + " seconds."})).queue();
		});
	}
	
	private void commandCheckTurnCount() {
		user.openPrivateChannel().queue((channel) ->
		{
			channel.sendMessage(CreateEmbed.make(guild, new String[] {"**TURN COUNT**", "We are currently on Turn " + Main.game.getTurnCount() + "."})).queue();
		});
	}

	private void commandCheckAP() {
		user.openPrivateChannel().queue((channel) ->
		{
			channel.sendMessage(CreateEmbed.make(guild, new String[] {"**CHECK AP**", "You have " + player.getAp() + " left!"})).queue();
		});
		
	}
	
	private void commandCheckUnit() {
		if (args.length < 3) {
			channel.sendMessage(CreateEmbed.make(1, genTooFewArgMsg(3))).queue();
		} else {
			switch (args[2]) {
			case "list":
				commandCheckUnitList();
				break;
			default:

				switch (args[2].charAt(0)) {
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
					commandCheckUnitNum();
					break;
				default:
					channel.sendMessage(CreateEmbed.make(1, ERR_COMMAND_DNE)).queue();
				}
			}
		}
		
	}
	
	private String createUnitString(Unit unit, int i) {
		String holder = "";
		
		//Do not print any information if unit is dead
		if (!unit.isDead()) {
			holder += "**Party Member #" + (i + 1) + " - ALIVE**\n";
			holder += "**NAME** - " + unit.getName() + "\n";
			holder += "**HP** - " + unit.getCurHP() + "/" + unit.getMaxHP() + "\n";
			holder += "**ATK** - " + unit.getAtk() + "\n";
			holder += "**DEF** - " + unit.getDef() + "\n";
			holder += "\n";
		} else {
			holder += "**Party Member #" + (i + 1) + " - DEAD**\n";
			holder += "**NAME** - " + unit.getName() + "\n";
			holder += "Rest in pepperoni. \n";
			holder += "\n";
		}
		
		return holder;
	}
	
	private void commandCheckUnitList() {
		int partySize = player.getParty().size();
		Unit unit;
		String holder = "";
		
		for (int i = 0; i < partySize; i++) {
			holder += createUnitString(player.getParty().get(i), i);
		}
		
		//Make final to allow use in enclosing
		final String finalHolder = holder;
		
		user.openPrivateChannel().queue((channel) ->
		{
			channel.sendMessage(CreateEmbed.make(guild, new String[] {"**UNIT LIST**", finalHolder})).queue();
		});
		
	}
	
	// TODO handle situation where unit is dead
	private void commandCheckUnitNum() {
		
		// Check if unit exists before doing anything
		try {
			int i = Integer.parseInt(args[2]) - 1;
			Unit target = player.getParty().get(i);
			
			
			if (args.length == 3) {
				user.openPrivateChannel().queue((channel) -> {
					channel.sendMessage(
							CreateEmbed.make(guild, new String[] { "**UNIT**", createUnitString(target, i) })).queue();
				});
			} else {
				switch (args[3]) {
				case "map_info":
					commandCheckUnitNumMapinfo(target, i);
					break;
				default:
					channel.sendMessage(CreateEmbed.make(1, ERR_COMMAND_DNE)).queue();
				}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			user.openPrivateChannel().queue((channel) -> {
				channel.sendMessage(CreateEmbed.make(1, ERR_UNIT_DNE)).queue();
			});
		}
		

	}

	// TODO - This may need to be revised at a later time
	// TODO - would be nice if it weren't explicitly just rest, for later implementation
	// TODO - some of the code will probably end up becoming nother method
	private void commandCheckUnitNumMapinfo(Unit target, int i) {
		
		if (!target.isDead()) {
			String holder = "";
			boolean activity = false;
			Coordinate unitCoordinate = new Coordinate(target.getPosition1(), target.getPosition2());
			
			if (Main.game.gameMap.get(unitCoordinate).isRest()) {
				holder += "Rest";
				activity = true;
			}
			
			if (!activity) {
				holder += "N/A";
			}
			
			final String finalHolder = holder;
			
			user.openPrivateChannel().queue((channel) -> {
				channel.sendMessage(
					CreateEmbed.make(guild, new String[] { "**Party Member #" + (i+1) +  " Map Coordinates**",  "X: " + target.getPosition1() + "\nY: " + target.getPosition2(), "**Activites at this map position**", finalHolder})).queue();
			});
		} else {
			user.openPrivateChannel().queue((channel) -> {
				channel.sendMessage(CreateEmbed.make(1, ERR_DEAD_MESSAGE)).queue();
			});
		}
	}

	
	// TODO need to debug
	private void commandAction() {
		if (args.length < 2) {
			channel.sendMessage(CreateEmbed.make(1, genTooFewArgMsg(2))).queue();
		} else {
			switch (args[1].charAt(0)) {
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				commandActionNum();
				break;
			default:
				channel.sendMessage(CreateEmbed.make(1, ERR_COMMAND_DNE)).queue();
			}
		}
	}

	
	// TODO need to debug
	private void commandActionNum() {
		//check user unit is dead or not
		if (args.length < 3) {
			channel.sendMessage(CreateEmbed.make(1, genTooFewArgMsg(3))).queue();
		} else {
			try {
				int i = Integer.parseInt(args[1]) - 1;
				Unit target = player.getParty().get(i);

				if (!target.isDead()) {
					switch (args[2]) {
					case "move":
						commandActionNumMove(i);
						break;
					case "rest":
						commandActionNumRest();
						break;
					}
				} else {
					user.openPrivateChannel().queue((channel) -> {
						channel.sendMessage(CreateEmbed.make(1, ERR_DEAD_MESSAGE)).queue();
					});
				}

			} catch (Exception e) {
				e.printStackTrace();
				user.openPrivateChannel().queue((channel) -> {
					channel.sendMessage(CreateEmbed.make(1, ERR_UNIT_DNE)).queue();
				});
			}
		}
	}

	
	// TODO debugged
	private void commandActionNumMove(int partyNum) {
		if (args.length < 5) {
			channel.sendMessage(CreateEmbed.make(1, genTooFewArgMsg(5))).queue();
		} else {
			System.out.println("Move is running");
			Main.game.moveUnit(user.getIdLong(), partyNum, Integer.parseInt(args[3]), Integer.parseInt(args[4]));
			System.out.println("Move has run");
		}
	}

	// TODO cannot be completed until movement & map revision is done
	private void commandActionNumRest() {
		
	}
	
	
	
	
	
}