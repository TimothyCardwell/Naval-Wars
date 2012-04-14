package tec.battleship;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class NavalWarsActivity extends Activity {
    /** Called when the activity is first created. */
	int difficulty = 1;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    
        Button newGame = (Button) findViewById(R.id.new_game);
        Button multiplayer = (Button) findViewById(R.id.multiplayer);
        Button options = (Button) findViewById(R.id.options);
        Button quit = (Button) findViewById(R.id.quit);
    
        newGame.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
                //Starting a new Intent
                Intent newGame = new Intent(getApplicationContext(), GameActivity.class);
                newGame.putExtra("difficulty", difficulty);
                startActivity(newGame);
			}
        	
        });
        
        multiplayer.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
                //Starting a new Intent
                Intent multiplayer = new Intent(getApplicationContext(), MultiplayerActivity.class);
                startActivity(multiplayer);	
			}   	
        });
        
        options.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
                //Starting a new Intent
                Intent options = new Intent(getApplicationContext(), OptionsActivity.class);
                startActivityForResult(options, 1);		//ForResult, 1 after options	
			}
        	
        });
        
        quit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				finish();
			}
        	
        });
    
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==1){
        	Bundle dataz = data.getExtras();
            difficulty = dataz.getInt("returnedData");
        }
    }
}