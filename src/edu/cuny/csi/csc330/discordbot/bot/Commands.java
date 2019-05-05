package edu.cuny.csi.csc330.discordbot.bot;


import java.util.ArrayDeque;
import java.util.HashMap;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter {
	
	private final String TOO_FEW_ARG_GENERIC = "Too few arguments in command!";
	private final String COMMAND_DNE = "Command does not exist. Please refer to [commands list](https://docs.google.com/document/d/1WBoUiqq8vrASRE-_-BIeKkW0Gw-S6Cs3g6vtbOYLScM/edit?usp=sharing).";
	
	private TextChannel channel;
	private Guild guild;
	private Member member;
	private User user;
	private String[] args;
	
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
				channel.sendMessage(CreateEmbed.make(1, COMMAND_DNE)).queue();
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
	
	// TODO need to actually create an instance of game
	// TODO if there are less than 3 players on the gameQueue the game cannot run
	private void startGame() {
		//Create new instance of game that sends over ArrayQueue
		Main.game = new Game(Main.playerQueue);
		
		//Switch game flag
		Main.gameStarted = true;
		
		//Destroy playerQueue-related objects and replace with new empty 
		Main.playerQueue = new ArrayDeque<Long>();
		Main.queueMap = new HashMap<Long, User>();
	}

	private boolean killGame() {
		
		if (!Main.gameStarted) {
			channel.sendMessage(CreateEmbed.make(1, "There is no game instance to kill...")).queue();
			return false;
		}
		else {
		
		//Print ending message
			Main.game.endGame();
			
		//Destroy game instance in Main by making it equal to null
		Main.game = null;
		
		//Destroy gameLiveServer
		Main.gameLiveServer = null;

		//Switch game flag
		Main.gameStarted = false;
		
		//Switch init flag
		Main.gameInit = false;
		
		return true;
		}
	}
	
	private void ownerCommand(int value) {
		
		String commandName = "!" + args[0];

		if (member.isOwner()) {
			switch (value) {
			case 0:
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
			channel.sendMessage(CreateEmbed.make(1, "Game has not been initialized!")).queue();
		} else if (!Main.gameStarted) {
			channel.sendMessage(CreateEmbed.make(1, "Game has not started!")).queue();
		} else {

			switch (value) {
			case 0:
				commandCheck();
				break;
			case 1:
				commandAction();
			}
		}
	}

	// TODO time game start
	private void commandInit() {
		if (!Main.gameInit) {
			Main.gameInit = true;
			Main.gameLiveServer = guild;
			channel.sendMessage(CreateEmbed.make(0, "Command success!")).queue();
		} else {
			channel.sendMessage(CreateEmbed.make(1, "Init flag already up in " + Main.gameLiveServer.getName()
					+ " server.\nPlease wait until the game has concluded.")).queue();
		}
	}
	
	// TODO this one isn't even working
	private void commandExtendTime() {
		
	}
	
	// TODO enforce that init flag must be up in order to start
	private void commandForceStart() {
		if (!Main.gameStarted) {
			startGame();
			channel.sendMessage(CreateEmbed.make(0, "Game has started!")).queue();
		} else {
			channel.sendMessage(CreateEmbed.make(1, "Game has already started!")).queue();
		}
	}
	
	private void commandForceKill() {
		if (killGame())
			channel.sendMessage(CreateEmbed.make(0, "Kill successful!")).queue();
	}
	
	private void commandJoin() {
		if (!Main.gameInit) {
			channel.sendMessage(CreateEmbed.make(1, "No game has been initialized!")).queue();
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

	private void commandHelp() {
		channel.sendMessage(CreateEmbed.make(new String[] {
				"** * * HELP * * **",
				"_Useful resources related to the game_",
				"**How to Play?**",
				"[Link to Commands](https://docs.google.com/document/d/1WBoUiqq8vrASRE-_-BIeKkW0Gw-S6Cs3g6vtbOYLScM/edit?usp=sharing)",
				"**Development Documentation**",
				"[Link to UML](https://drive.google.com/file/d/1u46U_4y0NRcFlcnnSxzvNUAra7sp3A28/view?usp=sharing) _(Save to drive & open with [draw.io](https://www.draw.io/) for clearer view)_\n"
				+ "[Link to OneNote](https://cixcsicuny-my.sharepoint.com/:o:/g/personal/jiali_chen_cix_csi_cuny_edu/EgL1A0uM7INEl3Vxz1p9ufsBWpwMMRULkCqCiNiF94uuYg?e=PqjOwP)"
				})).queue();
	}

	//TODO This one can only be finished when we have a game instance scheduled thing done
	private void commandNextGame() {
		channel.sendMessage(CreateEmbed.make(1, "I gotchu fam")).queue();
	}

	private void commandAction() {
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
			default:
				channel.sendMessage(CreateEmbed.make(1, COMMAND_DNE)).queue();
			}
		}
	}

	private void commandCheckFaction() {
		
		user.openPrivateChannel().queue((channel) ->
		{
			channel.sendMessage(CreateEmbed.make(guild, new String[] {"**FACTION**", "Your faction is " + Main.game.playerMap.get(user.getIdLong()).getFaction()})).queue();
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
				channel.sendMessage(CreateEmbed.make(1, COMMAND_DNE)).queue();
			}
		}
	}
	
	private void commandCheckTurnEnd() {
		user.openPrivateChannel().queue((channel) ->
		{
			channel.sendMessage(CreateEmbed.make(guild, new String[] {"**TURN END**", "example text"})).queue();
		});
	}
	
	private void commandCheckTurnCount() {
		user.openPrivateChannel().queue((channel) ->
		{
			channel.sendMessage(CreateEmbed.make(guild, new String[] {"**TURN COUNT**", "We are currently on Turn " + Main.game.getTurnCount() + ""})).queue();
		});
	}

}