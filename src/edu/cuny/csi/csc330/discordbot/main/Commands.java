package edu.cuny.csi.csc330.discordbot.main;


import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter {
	
	private final String TOO_FEW_ARG_GENERIC = "Too few arguments in command!";
	
	private TextChannel channel;
	private Guild guild;
	private Message message;
	private Member member;
	private User user;
	private String[] args;
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		channel = event.getChannel();
		guild = channel.getGuild();
		message = event.getMessage();
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
				channel.sendMessage("Hey, I got an exclaimation point. Printing default~!").queue();
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
	
	private void startGame() {
		//Create new instance of game
		//Switch game flag
		Main.gameStarted = true;
	}

	private void ownerCommand(int value) {
		
		String commandName = "!" + args[0];

		if (member.isOwner()) {
			channel.sendMessage(commandName + " - Owner recognized.").queue();

			switch (value) {
			case 0:
				commandInit();
				break;
			case 1:
				commandExtendTime();
				break;
			case 2:
				commandForceStart();
			}
		} else {
			channel.sendMessage("\'"+ commandName + "\' command can only be used by owner!").queue();
		}
	}

	private void outgameCommand(int value) {
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
	}

	private void ingameCommand(int value) {

		if (!Main.gameInit) {
			channel.sendMessage("Game has not been initialized!").queue();
		} else if (!Main.gameStarted) {
			channel.sendMessage("Game has not started!").queue();
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

	private void commandInit() {
		if (!Main.gameInit) {
			Main.gameInit = true;
			Main.gameLiveServer = guild;
			channel.sendMessage("Command success!").queue();
		} else {
			channel.sendMessage("Init flag already up in " + Main.gameLiveServer.getName()
					+ " server.\nPlease wait until the game has concluded.").queue();
		}
	}
	
	private void commandExtendTime() {
		
	}
	
	// TODO need to implement an actual game start
	// TODO enforce that init flag must be up in order to start
	private void commandForceStart() {
		if (!Main.gameStarted) {
			startGame();
			channel.sendMessage("Game has started!").queue();
		} else {
			channel.sendMessage("Game has already started!").queue();
		}
	}
	
	//TODO Handle case where player has already joined queue
	private void commandJoin() {
		if (!Main.gameInit) {
			channel.sendMessage("No game has been initialized!").queue();
		} else {
			Main.playerQueue.add(member);
			channel.sendMessage(member.getAsMention() + " has been added to the playerQueue!").queue();
		}
	}

	private void commandHelp() {
		channel.sendMessage(CreateEmbed.createEmbed(new String[] {
				"** * * HELP * * **",
				"_Useful resources related to the game_",
				"**How to Play?**",
				"[Link to Commands](https://docs.google.com/document/d/1WBoUiqq8vrASRE-_-BIeKkW0Gw-S6Cs3g6vtbOYLScM/edit?usp=sharing)",
				"**Development Documentation**",
				"[Link to UML](https://drive.google.com/file/d/1u46U_4y0NRcFlcnnSxzvNUAra7sp3A28/view?usp=sharing) _(Save to drive & open with [draw.io](https://www.draw.io/) for clearer view)_\n"
				+ "[Link to OneNote](https://cixcsicuny-my.sharepoint.com/:o:/g/personal/jiali_chen_cix_csi_cuny_edu/EgL1A0uM7INEl3Vxz1p9ufsBWpwMMRULkCqCiNiF94uuYg?e=PqjOwP)"
				}).build()).queue();
	}

	//TODO This one can only be finished when we have a game instance scheduled thing done
	private void commandNextGame() {
		channel.sendMessage("I gotchu fam").queue();
	}

	private void commandAction() {
	}

	private void commandCheck() {
		if (args.length < 2) {
			channel.sendMessage(genTooFewArgMsg(2)).queue();
		} else {
			switch (args[1]) {
			case "faction":
				commandCheckFaction();
				break;
			case "turn":
				commandCheckTurn();
			}
		}
	}

	private void commandCheckFaction() {
		user.openPrivateChannel().queue((channel) ->
		{
			channel.sendMessage(CreateEmbed.createEmbed(guild, new String[] {"**FACTION**", "example text"}).build()).queue();
		});
	}

	private void commandCheckTurn() {
		if (args.length < 3) {
			channel.sendMessage(genTooFewArgMsg(3)).queue();
		} else {
			switch (args[2]) {
			case "end":
				commandCheckTurnEnd();
				break;
			case "count":
				commandCheckTurnCount();
				break;
			}
		}
	}
	
	private void commandCheckTurnEnd() {
		user.openPrivateChannel().queue((channel) ->
		{
			channel.sendMessage(CreateEmbed.createEmbed(guild, new String[] {"**TURN END**", "example text"}).build()).queue();
		});
	}
	
	private void commandCheckTurnCount() {
		user.openPrivateChannel().queue((channel) ->
		{
			channel.sendMessage(CreateEmbed.createEmbed(guild, new String[] {"**TURN COUNT**", "example text"}).build()).queue();
		});
	}

}