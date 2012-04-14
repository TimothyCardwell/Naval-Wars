package tec.battleship;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
 
public class GameActivity extends Activity implements OnClickListener{
        Button buttonRotate, buttonDeploy, buttonNext;
        Grid grid; //gameboard
        FrameLayout frame;
        int difficulty; //easy = 0, 1 = hard
        int AIshipsSunk = 0;
        int playerShipsSunk = 0;
        int turn = 0; //0 for player, 1 for AI
        boolean hit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamesetup);
        
        Bundle diff = getIntent().getExtras();
        difficulty = diff.getInt("difficulty");
        
        //set up buttons
        buttonRotate = (Button)findViewById(R.id.rotate);
        buttonDeploy = (Button)findViewById(R.id.Deploy);
        buttonNext = (Button)findViewById(R.id.next);
        
        buttonRotate.setTag(1);
        buttonDeploy.setTag(1);
        buttonNext.setTag(1);
        
        buttonRotate.setOnClickListener(this);
        buttonDeploy.setOnClickListener(this);
        buttonNext.setOnClickListener(this);
        
        frame = (FrameLayout)findViewById(R.id.main_view1);
        grid = new Grid(this, difficulty);
        frame.addView(grid);
        grid.requestFocus();
        
       
    }
    
        public void onClick(View src) {
    	    int status =(Integer) src.getTag();
                switch(src.getId())
                {
                case R.id.Deploy: //End Deployment / Quit
                	if(status == 1){
                		boolean ready = validateShipLocations(grid);
                		if(ready == true){
                          grid.setDeploy_phase(false);   
                          grid.invalidate();
                          buttonDeploy.setTag(0);
                          buttonDeploy.setText("Quit");
                          buttonRotate.setEnabled(false);
                          buttonNext.setTag(0);
                          buttonNext.setText("Fire!");
                		}
                		else{
                            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            				builder.setTitle("Invalid Ship Placement");
            				builder.setMessage("You need to place your ships so they are not overlapping.");
            				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            			           public void onClick(DialogInterface dialog, int id) {
            			                dialog.cancel();
            			           }
            			    });
            				builder.show();
                		}
                	}
                	else{//status == 0
                		finish();
                	}
                    break;
                
                case R.id.next: //Rotate / Fire
                	if(status == 1) //Still deploying
                       grid.switchShip();
                	
                	if(status == 0){
                		playGame(grid);
                	}
                	//check game state
                    if(AIshipsSunk == 5){
                        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        				builder.setTitle("Game Over");
        				builder.setMessage("You Win!");
        				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        			           public void onClick(DialogInterface dialog, int id) {
        			                GameActivity.this.finish();
        			           }
        			    });
        				builder.show();
                    }
                    
                    if(playerShipsSunk == 5){
                        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        				builder.setTitle("Game Over");
        				builder.setMessage("You Lose!");
        				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        			           public void onClick(DialogInterface dialog, int id) {
        			                GameActivity.this.finish();
        			           }
        			    });
        				builder.show();
                    }
                	
                    break;
                
                case R.id.rotate:
                	    grid.rotator();
                	    break;
                }               
        }//end click
        
        /**
         * 
         * @param grid: the game grid. playGame organizes game flow.
         */
		private void playGame(Grid grid){
			if(grid.alreadyShot(turn)){
				System.out.println("true");
		        AlertDialog.Builder builder=new AlertDialog.Builder(this);
				builder.setMessage("You have already shot at that square!");
				builder.setIcon(android.R.drawable.ic_dialog_alert);
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			    });
				builder.show();
			}
        	if(grid.checkHit(turn)){
    			//grid.colorHitSquare(turn);
    			grid.invalidate();
    			if(grid.checkSunk()){
    				AIshipsSunk++;
    				if(AIshipsSunk != 5){
    					AlertDialog.Builder builder=new AlertDialog.Builder(this);
    					builder.setTitle("Ship Sunk");
    					builder.setMessage("You sunk their " + grid.lastShipHit.name + "!");
    					builder.setIcon(android.R.drawable.ic_dialog_alert);
    					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
    			           public void onClick(DialogInterface dialog, int id) {
    			                dialog.cancel();
    			           }
    					});
    					builder.show();
    				}
    			}
    		}
    		else{
    			grid.colorMissSquare(turn);
    			grid.invalidate();
    		}
        	if(turn == 0){
        		turn = 1;
        		grid.AIturn();
        		if(grid.AIhitlast){
            	//if(grid.checkHit(1)){
            		//grid.colorHitSquare(1);
            		if(grid.checkSunk()){
            			playerShipsSunk++;
            			if(playerShipsSunk != 5){
            				AlertDialog.Builder builder=new AlertDialog.Builder(this);
            				builder.setTitle("Ship Sunk");
            				builder.setMessage("Your " + grid.lastShipHit.name + " has been sunk!");
            				builder.setIcon(android.R.drawable.ic_dialog_alert);
            				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        			           public void onClick(DialogInterface dialog, int id) {
        			              dialog.cancel();
        			           }
            				});
            				builder.show();
            			}
            		}
            		grid.AIhitlast = false;
            	}
            	else
            		grid.colorMissSquare(1);
        		turn = 0;
        	}
        }
        
        private boolean validateShipLocations(Grid grid){
        	return grid.validateShipLocations();
        }
}
