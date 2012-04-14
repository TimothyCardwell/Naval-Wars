package tec.battleship;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class Grid extends View {
        @SuppressWarnings("unused")
		private final GameActivity gameboard;
		private int gameboardWidth;
		private int gameboardHeight;
        private boolean deploy_phase = true;
        public float width; // width of one tile
        public float height; // height of one tile
        int selX; // X index of selection
        int selY; // Y index of selection
        private final Rect selRect = new Rect();
        
        AIController AI;
        public PlayerController PC;
        

        public Ship lastShipHit;
        private int currSelect;
        int diff;
        boolean AIhitlast;
        
    public Grid(Context context, int difficulty) {
        super(context);
        this.gameboard = (GameActivity) context;
        
        diff = difficulty;
        if(difficulty == 0)
        	AI = new AIeasy(this);
        else
        	AI = new AIhard(this);
        System.out.println(difficulty);
        //AI = new AIController(this);
        PC = new PlayerController(this);
            
        PC.ships.add(new Ship("Carrier", 0, 8, 5, this));
        PC.ships.add(new Ship("Battleship", 1, 8, 4, this));
        PC.ships.add(new Ship("Destroyer", 2, 8, 3, this));
        PC.ships.add(new Ship("Submarine",3, 8, 3, this));
        PC.ships.add(new Ship("Frigate", 4, 8, 2, this));

        deploy_phase = true;
        AIhitlast = false;
        currSelect = 0;
        setFocusable(true);
        setFocusableInTouchMode(true);
    }
    /**
     * 
     * @param turn: player or AI turn
     * @return returns true if the spot was already shot at, false otherwise
     */
    public boolean alreadyShot(int turn){
    	Point shot = new Point(selX, selY);
    	int i, numMisses, numHits;
    	if(turn == 0){
    		numMisses = PC.missCoord.size();
    		numHits = PC.hitCoord.size();
    		for(i = 0; i < numMisses; i++){
    			if(shot.equals(PC.missCoord.get(i)))
    					return true; //display something
    		}
    		for(i = 0; i < numHits; i++){
    			if(shot.equals(PC.hitCoord.get(i)))
    				return true; //display something
    		}
    	}
    	else{
    		numMisses = AI.missCoord.size();
    		numHits = AI.hitCoord.size();
    		for(i = 0; i < numMisses; i++){
    			if(shot.equals(AI.missCoord.get(i)))
    				return true;
    		}
    		for(i = 0; i < numHits; i++){
    			if(shot.equals(AI.hitCoord.get(i)))
    				return true;
    		}
    	}
    	return false;
    }
    
    /**
     * 
     * @param turn player or AI
     * @return returns true if the current shot was a hit, false otherwise
     */
    public boolean checkHit(int turn){
    	int i, j, shipSize;
    	Point shot;
    	if(turn == 0){ //player shot
    		shot = new Point(selX, selY);
    		for(i = 0; i < AI.ships.size(); i++){ //enemyShips.size will be 5
    			shipSize = AI.ships.get(i).getSize();
    			for(j = 0; j < shipSize; j++){
    				if(shot.equals(AI.ships.get(i).shipLocation.get(j))){
    					AI.ships.get(i).timesHit++;
    					lastShipHit = AI.ships.get(i);
    					colorHitSquare(turn);
    					return true;
    				}
    			}
    		}
    	}
		else{ //computers turn
    		shot = new Point(selX, selY);
    		for(i = 0; i < PC.ships.size(); i++){ //enemyShips.size will be 5
    			shipSize = PC.ships.get(i).getSize();
    			for(j = 0; j < shipSize; j++){
    				if(shot.equals(PC.ships.get(i).shipLocation.get(j))){
    					System.out.println("HIT!");
    					PC.ships.get(i).timesHit++;
    					lastShipHit = PC.ships.get(i);
    					AIhitlast = true;
    					colorHitSquare(turn);
    					return true;
    				}
    			}
    		}
			
		}
    	return false;
    }
    /**
     * 
     * @param turn player or AI
     * sets up the grid for an invalidate (redraw)
     */
    public void colorHitSquare(int turn){
    	Rect hitRect = new Rect();
    	getRect(selX, selY, hitRect);
    	if(turn == 0){
    		PC.hitCoord.add(new Point(selX, selY));
    		PC.hitList.add(hitRect);
    	}
    	else{
    		AI.hitCoord.add(new Point(selX, selY));
    		AI.hitList.add(hitRect);
    	}
    }
    
    public void colorMissSquare(int turn){
    	Rect missRect = new Rect();
    	getRect(selX, selY, missRect);
    	if(turn == 0){
    		PC.missCoord.add(new Point(selX, selY));
    		PC.missList.add(missRect);
    	}
    	else{
    		AI.missCoord.add(new Point(selX, selY));
    		AI.missList.add(missRect);
    	}
    }
    
    /**
     * 
     * @return returns true if the last shot sank a ship. false o/w
     */
    public boolean checkSunk(){
    	if(lastShipHit != null){
    		if(lastShipHit.size == lastShipHit.timesHit){
    			return true;
    		}
    	}
    	return false;
    }
    
    public boolean isDeploy_phase() {
                return deploy_phase;
        }

    public void setDeploy_phase(boolean deploy_phase) {
                this.deploy_phase = deploy_phase;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    	    gameboardWidth = w;
    	    gameboardHeight = h;
            width = w / 8f;
            height = h / 16f;
            getRect(selX, selY, selRect);
            super.onSizeChanged(w, h, oldw, oldh);
    }
       
       private void getRect(int x, int y, Rect rect) {
        rect.set((int) (x * width), (int) (y * height), (int) (x
                        * width + width), (int) (y * height + height));
    }
    
    @Override
    /**
     * called whenever invalidate() is called.
     * Just redraws my gameboard
     */
    protected void onDraw(Canvas canvas) 
    {
            // Draw the background...
            Style style = Paint.Style.STROKE;
            Paint background = new Paint();
            background.setColor(getResources().getColor(R.color.battleship_background));
            canvas.drawRect(0, 0, getWidth(), getHeight(), background);
            
            // set colors
            Paint dark = new Paint();
            Paint ShipBorder = new Paint();
            Paint ShipColor =new Paint();
            Paint hilite = new Paint();
            Paint light = new Paint();
            Paint hit = new Paint();
            Paint miss = new Paint();
            dark.setColor(getResources().getColor(R.color.battleship_dark));    
            ShipColor.setColor(getResources().getColor(R.color.battleship_dark));    
            ShipBorder.setColor(getResources().getColor(R.color.battleship_black));    
            hilite.setColor(getResources().getColor(R.color.battleship_hilite));
            light.setColor(getResources().getColor(R.color.battleship_light));
            hit.setColor(getResources().getColor(R.color.battleship_hit));
            miss.setColor(getResources().getColor(R.color.battleship_miss));
            
            // Draw the minor grid lines
            for (int i = 0; i < 16; i++){ //horizontal lines
               canvas.drawLine(0, i * height, getWidth(), i * height, light);
               canvas.drawLine(0, i * height + 1, getWidth(), i * height + 1, hilite);
            }
            for (int i = 0; i < 8; i++){ //vertical lines
               canvas.drawLine(i * width, 0, i * width, getHeight(), light);
               canvas.drawLine(i * width + 1, 0, i * width + 1, getHeight(), hilite);
            }

            canvas.drawLine(0, 8 * height, getWidth(), 8 * height, dark); //middle line
            canvas.drawLine(0, 8 * height + 1, getWidth(), 8 * height + 1, hilite);
                
            //draws the ships
            for(int i =0; i<5;i++){
               PC.ships.get(i).setRect(PC.ships.get(i).getX(), PC.ships.get(i).getY());
               canvas.drawRect((PC.ships.get(i)).getHull(), ShipColor);
               ShipBorder.setStyle(style);
               canvas.drawRect((PC.ships.get(i)).getHull(), ShipBorder);
               
               //Used to see AI maps
               /*AI.ships.get(i).setRect(AI.ships.get(i).getX(), AI.ships.get(i).getY());
               canvas.drawRect((AI.ships.get(i)).getHull(), ShipColor);
               ShipBorder.setStyle(style);
               canvas.drawRect((AI.ships.get(i)).getHull(), ShipBorder);*/
            }
            //sets a border along the current chosen ship  

            //look of our selected box 
            if(deploy_phase == false){
               for(int i = 0; i < PC.hitList.size() ;i++) 
                   canvas.drawRect(PC.hitList.get(i), hit); //draw a dark box at every hit shot
               
               for(int i = 0; i < PC.missList.size() ;i++) 
                   canvas.drawRect(PC.missList.get(i), miss); //draw a dark box at every missed shot
               
               for(int i = 0; i < AI.hitList.size() ;i++) 
                   canvas.drawRect(AI.hitList.get(i), hit); //draw a dark box at every hit shot
               
               for(int i = 0; i < AI.missList.size() ;i++) 
                   canvas.drawRect(AI.missList.get(i), miss); //draw a dark box at every missed shot
               
               Paint selected = new Paint();
               selected.setColor(getResources().getColor(R.color.battleship_selected));
               canvas.drawRect(selRect, selected);
            }
    }
    
    @Override
    /**
     * handles user input touch input.
     */
    public boolean onTouchEvent(MotionEvent ev){
		float xTouch, yTouch;
    	if(deploy_phase == false){
    		if (ev.getAction() == MotionEvent.ACTION_DOWN){
              xTouch = 8 * (ev.getX() / gameboardWidth);
              yTouch = 16 *(ev.getY() / gameboardHeight);
              select((int)xTouch, (int)yTouch);
    		}	
    	}
    	else{
          if (ev.getAction() == MotionEvent.ACTION_DOWN){
             xTouch = 8 * (ev.getX() / gameboardWidth);
             yTouch = 16 *(ev.getY() / gameboardHeight);
             PC.ships.get(currSelect).moveTo((int)xTouch, (int)yTouch);
             PC.ships.get(currSelect).setupShipLocation(); 
          }
        }
    	return true;
    }
    
    /**
     * handles broke ass phone inputs
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       if(deploy_phase == false){
           switch (keyCode){
               case KeyEvent.KEYCODE_DPAD_UP:
                    select(selX, selY - 1);
                    break;
               case KeyEvent.KEYCODE_DPAD_DOWN:
                    select(selX, selY + 1);
                    break;
               case KeyEvent.KEYCODE_DPAD_LEFT:
                    select(selX - 1, selY);
                    break;
               case KeyEvent.KEYCODE_DPAD_RIGHT:
                    select(selX + 1, selY);
                    break;
       }
           
   }
       else{ //still placing ships
            switch(keyCode){
                case KeyEvent.KEYCODE_DPAD_UP:
                     (PC.ships.get(currSelect)).moveUp();                 
                     break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                     (PC.ships.get(currSelect)).moveDown();                 
                     break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                     (PC.ships.get(currSelect)).moveLeft();                 
                     break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                     (PC.ships.get(currSelect)).moveRight();                 
                     break;
                case KeyEvent.KEYCODE_ENTER:
                	(PC.ships.get(currSelect)).rotate();
                     break;
                default:
                     return super.onKeyDown(keyCode, event);         
            }
       }
       PC.ships.get(currSelect).setupShipLocation();
       return true;
    }
    
    private void select(int x, int y){
        invalidate(selRect);
        selX = Math.min(Math.max(x, 0), 7);
        selY = Math.min(Math.max(y, 0), 7);
        getRect(selX, selY, selRect);
        invalidate(selRect);
    }

    public void switchShip(){
        invalidate((PC.ships.get(currSelect)).getHull());
        if(currSelect == 4){
           currSelect = 0;
        }
        else{
           currSelect++;
        }
        invalidate((PC.ships.get(currSelect)).getHull());
    }
    
    public void rotator(){
    	PC.ships.get(currSelect).rotate();
    }
    
    /**
     * 
     * @return true if the current ships the player
     * has placed are not overlapping
     */
    public boolean validateShipLocations(){
    	int i, j, k, l;
    	for(i = 0; i < 5; i++){
    		for(j = i + 1; j < 5; j++){
    			for(k = 0; k < PC.ships.get(i).getSize(); k++){
    				for(l = 0; l < PC.ships.get(j).getSize(); l++){
    					if(PC.ships.get(i).shipLocation.get(k).equals(PC.ships.get(j).shipLocation.get(l)))
    						return false;
    				}
    			}
    		}
    	}
    	return true;
    }
    
    public void AIturn(){
    		AI.AIshot();
    }

}

