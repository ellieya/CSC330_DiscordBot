package edu.cuny.csi.csc330.discordbot.bot;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class BotMain extends Game { //WAIT TIME HAS TO BE GLOBAL

	private boolean waiting; //Init has been called and Game does not exist yet
	private Queue<Long> playerQueue = new LinkedList<Long>(); // List of Discord IDs of players who join
	
	public BotMain() {
		
		this.waiting = true;
		
	}
	
	public void init() throws InterruptedException { //!init() - Called by GM/Admin
		
		long endTime; //Used for measuring elapsed time
		long startTime; //Used for measuring elapsed time
		long timeElapsed = 0; //How much time has passed in milliseconds
		
		//Game object will run in 2 minutes!
		
		startTime = Calendar.getInstance().getTime().getTime();

		while(timeElapsed < 120000) { //2 minutes in milliseconds
			
			this.waiting = true;
			
			endTime = Calendar.getInstance().getTime().getTime();
			
			timeElapsed = endTime - startTime;
			
		} //Wait is over!
	
		this.waiting = false;
		
		Game newGame = new Game(this.playerQueue); //Create a new game! (And pass through the playerQueue)
		
	} //End of init
	
	public void create() {
		
		this.waiting = false;
		
		Game newGame = new Game(this.playerQueue); //Create a new game! (And pass through the playerQueue)
		
		
	} //End create
	
	public void join(Long ID) { //!join() - Can only be called when waiting for instance of Game
		
		if(waiting == true) {

			System.out.println("Player " + ID + " has joined the game!");

			this.playerQueue.add(ID); // Add the player's Discord ID to the queue

		} //Player has been added to Queue
		
	}

}
