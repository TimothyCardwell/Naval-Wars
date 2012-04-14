package tec.battleship;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import android.graphics.Rect;

public class Ship{
	boolean rotated;
	String name;
    int ix, iy, size, timesHit;  
    Rect hull;
    public List<Point> shipLocation;
    Grid grid;
    
    public Ship(String n, int x, int y, int sz, Grid grid_){
       name = new String(n);
       hull = new Rect();
       ix = x;
       iy = y;
       size = sz;
       timesHit = 0;
       rotated = false;
       shipLocation = new ArrayList<Point>(sz);
       setupShipLocation();
       grid = grid_;
    }
    
    public void setupShipLocation(){ //not working for keyboard inputs
    	int i;
        this.shipLocation.clear();
    	for(i = 0; i < this.size; i++){
    		if(this.rotated == false)//vertical ship
    			this.shipLocation.add(new Point(this.ix, this.iy + i));
    		else //horizontal ship
    			this.shipLocation.add(new Point(this.ix + i, this.iy));
    	}
    }
    
    public void setRect(int x, int y){
    	if(rotated == false)
    	   hull.set((int) (x * grid.width), (int) (y * grid.height), (int) (x * grid.width + grid.width), (int) (y*grid.height + grid.height*this.size));
    	else
    	   hull.set((int) (x * grid.width), (int) (y * grid.height), (int) (x * grid.width + this.size*grid.width), (int) (y*grid.height + grid.height));

    }
    
    public void rotate(){
    	if(this.rotated == false)
    		this.rotated = true;
    	else
    		this.rotated = false;
    	this.checkBorders();
    	this.setupShipLocation();
    	grid.invalidate();
    }
    
    public void checkBorders(){
    	if(this.rotated == true){
    		if(this.ix > 8 - this.size)
    			this.ix = 8 - this.size;
    	}
    	else{
    		if(this.iy > 16 - this.size)
    			this.iy = 16 - this.size;
    	}
    }
    
    public void moveTo(int x, int y){
      if(this.rotated == false){
        x = Math.min(Math.max(x, 0), 7);
        y = Math.min(Math.max(y, 8), 16);
    	if(y > 16 - this.size)
    		y = 16 - this.size;
    	        	
    	if(this.ix == x && this.iy == y)
    		rotate();

    	this.ix = x;
    	this.iy = y;
      }
      else{//ship was rotated
        x = Math.min(Math.max(x, 0), 7);
        y = Math.min(Math.max(y, 8), 16);
      	if(x > 8 - this.size)
      		x = 8 - this.size;

      	if(this.ix == x && this.iy == y)
      		rotate();

      	this.ix = x;
      	this.iy = y;
      }
      
      this.checkBorders();
      grid.invalidate();
    }
    
    public void moveUp(){
      if(this.iy > 8)
         this.iy = this.iy - 1;
      grid.invalidate();
      return;
    }
    
    public void moveDown(){
        if(this.iy < 16)
            this.iy = this.iy + 1;

        grid.invalidate();
        return;  
    }
    
    public void moveLeft(){
        if(this.ix > 0)
            this.ix = this.ix - 1;
         
        grid.invalidate();
        return;   
    }
    
    public void moveRight(){
        if(this.ix < 7)
            this.ix = this.ix + 1;
        
        grid.invalidate();
        return;   
    }

    public Rect getHull(){
       return (new Rect(hull));
    }
    
    public int getX(){
       return (ix);
    }
    public int getY(){
       return (iy);
    }
    public int getSize(){
       return (size);
    }
}
