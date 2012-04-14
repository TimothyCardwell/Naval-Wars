package tec.battleship;

import java.util.Random;

public class AIhard extends AIController{
	boolean lastShotHit, sunk;
	int offset;
	int firstHitX;
	int firstHitY;
	boolean keepRight, keepLeft, keepUp, keepDown;

	public AIhard(Grid grid_) {
		super(grid_);
		lastShotHit = false;
		sunk = true;
		
		//location of the first hit on an enemy ship
		firstHitX = 0;
		firstHitY = 0;
		offset = 1;
		
		//keep track of rolling hits
		keepRight = true;
		keepLeft = false;
		keepUp = false;
		keepDown = false;
	}
	
	/**
	 * A more difficult shot picking scheme for the AI.
	 * Checks to see if his last shot was a hit, and if so, 
	 * shoots accordingly.
	 */
    public void AIshot(){    	
    	//as long as the current ship we are firing at isn't sunk, we still have a target
    	if(!sunk){
    			if(keepRight){
    				grid.selX = firstHitX + offset;//sets up a potential shot to the right
    				grid.selY = firstHitY;
    				if(!grid.alreadyShot(1) && grid.selX < 7){ //shoot if its not already shot at and on the gameboard
    					if(!grid.checkHit(1)){ //miss
    						keepRight = false;
    						keepLeft = true;
    						offset = 1;
    						return;
    						}
    					else{//another hit
    						offset++;
    						sunk = grid.checkSunk();
    						if(sunk)
    							reinitializeVariables(); //sunk the ship, reset
    						return;
    					}
    				}
    				keepRight = false;
    				keepLeft = true;
    				offset = 1;
    			}
    			if(keepLeft){//similar to keepRight
    				grid.selX = firstHitX - offset;
    				grid.selY = firstHitY;
    				if(!grid.alreadyShot(1) && grid.selX > 0){
    					if(!grid.checkHit(1)){ //miss
    						keepLeft = false;
    						keepUp = true;
    						offset = 1;
    						return;
    						}
    					else{//another hit
    						offset++;
    						sunk = grid.checkSunk();
    						if(sunk)
    							reinitializeVariables();
    						return;
    					}
    				}
    				keepLeft = false;
    				keepUp = true;
    				offset = 1;
    			}
    			if(keepUp){
    				grid.selX = firstHitX;
    				grid.selY = firstHitY - offset;
    				if(!grid.alreadyShot(1) && grid.selY > 7){
    					if(!grid.checkHit(1)){ //miss
    						keepUp = false;
    						keepDown = true;
    						offset = 1;
    						return;
    						}
    					else{//another hit
    						offset++;
    						sunk = grid.checkSunk();
    						if(sunk)
    							reinitializeVariables();
    						return;
    					}
    				}
    				keepUp = false;
    				keepDown = true;
    				offset = 1;
    			}
    			if(keepDown){//another hit
    				grid.selX = firstHitX;
    				grid.selY = firstHitY + offset;
    				if(!grid.alreadyShot(1) && grid.selY < 15){
    					if(!grid.checkHit(1)){ //miss
    						keepRight = true;
    						keepDown = false;
    						offset = 1;
    						return;
    						}
    					else{//another hit
    						offset++;
    						sunk = grid.checkSunk();
    						if(sunk)
    							reinitializeVariables();
    						return;
    					}
    				}
    				keepRight = true;
    				keepDown = false;
    				offset = 1;
    			}
    	}
    	Random shot = new Random();
    	int x=0, y=0;
    	boolean check = true;
    	
    	//random points until a valid shot location is found.
    	while(check){
        	x = shot.nextInt(8);
        	y = shot.nextInt(8);
        	y = y + 8;
        	grid.selX = x;
        	grid.selY = y;
        	check = grid.alreadyShot(1);
    	}
    	lastShotHit = grid.checkHit(1);
    	if(lastShotHit){ //upon hitting a ship, next shot won't be random
    		sunk = false;
    		firstHitX = x;
    		firstHitY = y;
    	}
    	return;
    }
    
    public void reinitializeVariables(){
		lastShotHit = false;
		sunk = true;
		firstHitX = 0;
		firstHitY = 0;
		offset = 1;
		keepRight = true;
		keepLeft = false;
		keepUp = false;
		keepDown = false;
    }
}
