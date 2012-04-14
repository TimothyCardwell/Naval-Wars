package tec.battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Point;
import android.graphics.Rect;

public class AIController {
    List<Point> hitCoord;    
    List<Point> missCoord;
    List<Rect> hitList;
    List<Rect> missList;
    List<Ship> ships;
    
	List<String> names; 
	int[] sizes = {5, 4, 3, 3, 2};
	
    Grid grid;
    
    public AIController(Grid grid_){
        hitCoord = new ArrayList<Point>(17);    
        missCoord = new ArrayList<Point>(47);
        hitList = new ArrayList<Rect>(17);
        missList = new ArrayList<Rect>(47);
        ships = new ArrayList<Ship>(5);
        
        names = new ArrayList<String>(5);
    	names.add("Carrier");
    	names.add("Battleship");
    	names.add("Destroyer");
    	names.add("Submarine");
    	names.add("Frigate");
    	
        grid = grid_;
        setupShipPositions();
    }
    
    /**
     * sets up computer ship locations
     */
    private void setupShipPositions(){
    	Random random = new Random();
    	boolean checkNeighbors;
    	boolean rotate = false;
    	int i, x, y;
    	for(i = 0; i < 5; i++){
    		checkNeighbors = false;
			rotate = random.nextBoolean();
			ships.add(new Ship(names.get(i), 0, 0, sizes[i], grid));
			ships.get(i).rotated = rotate;
			Ship curr = ships.get(i);
    		while(!checkNeighbors){
    			x = random.nextInt(8);
    			y = random.nextInt(8);
    			curr.ix = x;
    			curr.iy = y;
    			checkBorders(curr);
    			curr.setupShipLocation();
    			checkNeighbors = validPlacement(i);    	
    		}
    	}
    }
    
    /**
     * 
     * @param ship
     * makes sure the current ship isn't off the grid
     */
    private void checkBorders(Ship ship){
    	if(ship.rotated == true){
    		if(ship.ix > 8 - ship.size)
    			ship.ix = 8 - ship.size;
    	}
    	else{
    		if(ship.iy > 8 - ship.size)
    			ship.iy = 8 - ship.size;
    	}
    }
    
    /**
     * 
     * @param curr
     * @return returns true if the current placement scheme has no overlapping ships.
     */
    private boolean validPlacement(int curr){
    	int i, j, k, l;
    	if(curr + 1 == 1)
    		return true;
    	for(i = 0; i < curr + 1; i++){
    		for(j = i + 1; j < curr + 1; j++){
    			for(k = 0; k < ships.get(i).getSize(); k++){
    				for(l = 0; l < ships.get(j).getSize(); l++){
    					if(ships.get(i).shipLocation.get(k).equals(ships.get(j).shipLocation.get(l)))
    						return false;
    				}
    			}
    		}
    	}
    	return true;
    }
    
    /**
     * code for AI turns, see AIeasy.java and AIhard.java
     */
    public void AIshot(){
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
    }
}
