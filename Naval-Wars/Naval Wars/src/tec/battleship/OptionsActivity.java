package tec.battleship;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class OptionsActivity extends Activity implements OnItemSelectedListener {
	int diff = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_screen);
        
        Button newGame = (Button) findViewById(R.id.options_return);
        
        Spinner spinner = (Spinner) findViewById(R.id.diff);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.diff_array, 
        		android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    
        newGame.setOnClickListener(new OnClickListener(){

		@Override
			public void onClick(View arg0) {
				Intent intent = getIntent();
			    intent.putExtra("returnedData", diff);
			    setResult(RESULT_OK, intent);
			    System.out.println(diff);
			    finish();
			}
    	
        });
    }
    

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		if(pos == 0)
			diff = 0;
		else
			diff = 1;
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

}