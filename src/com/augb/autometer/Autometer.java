package com.augb.autometer;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Autometer extends Activity implements GpsNotificationListener{
	double distanceValue, fareValue;
	long waitingtimeValue;

	Handler handler = new Handler();
	
    private TextView waitingTimeView, DistanceView, fareView;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Typeface myTypeface = Typeface.createFromAsset(this.getAssets(),"DS-DIGIB.TTF");
        waitingTimeView = (TextView) findViewById(R.id.WaitingTime);
        waitingTimeView.setTypeface(myTypeface);
        DistanceView = (TextView) findViewById(R.id.Distance);
        DistanceView.setTypeface(myTypeface);
        fareView = (TextView) findViewById(R.id.Fare);
        fareView.setTypeface(myTypeface);
        ToggleButton VacantButton = (ToggleButton) findViewById(R.id.VacantHiredButton);
        VacantButton.setOnClickListener( new View.OnClickListener(){
        	
        	public void onClick(View v)
        	{//do the toggle thing
        		GpsLocationManager locManager = GpsLocationManager.GetGPSLocationManger();
        		locManager.start( Autometer.this, Autometer.this);
        	}
        }
        );
        Button stopButton = (Button) findViewById(R.id.StopButton);
        stopButton.setOnClickListener(new View.OnClickListener (){
        	public void onClick(View v)
        	{
                ToggleButton VacantButton = (ToggleButton) findViewById(R.id.VacantHiredButton);
                VacantButton.setChecked(false);
                //TODO Stop capturing data.
        
        	}
        });
        init();
    }
    
    public void init()
    {
    	distanceValue =  fareValue = 0.0;
    	waitingtimeValue = 0;
    	waitingTimeView.setText("--.--");
    	DistanceView.setText("---.-");
    	fareView.setText("---.--");
    	
    }
    @Override
    public void onUpdate(final GPSLocation loc) {
    	handler.post(new Runnable() {
    		@Override
    		public void run() {
    			// TODO Auto-generated method stub
    	    	distanceValue = loc.getTotalDistance();
    	    	waitingtimeValue = loc.getTotalWaitingDT();//in millisecs
    	    	waitingTimeView.setText(String.format("%d:%d",waitingtimeValue/(1000*60), waitingtimeValue/(1000)));
    	    	DistanceView.setText(String.format("%.2f", distanceValue/1000));
    	    	//set fareValue
    	    	fareView.setText(String.format("%.2f", fareValue));    	    	
	
    		}
    	});
    	
    }
}