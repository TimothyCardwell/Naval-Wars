package tec.battleship;

import java.util.Random;

public class AIeasy extends AIController{

	public AIeasy(Grid grid_) {
		super(grid_);
		System.out.println("22222");
	}
	
	/**
	 * Most simple way to code for battleship.
	 * All random shots.
	 */
    public void AIshot(){
    	System.out.println("here");
    	Random shot = new Random();
    	int x, y;
    	boolean check = true;
    	while(check){
        	x = shot.nextInt(8);
        	y = shot.nextInt(8);
        	y = y + 8;
        	grid.selX = x;
        	grid.selY = y;
        	check = grid.alreadyShot(1);
    	}
    	grid.checkHit(1);
    }

}
