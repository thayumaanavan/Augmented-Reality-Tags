package com.jwetherell.augmented_reality.activity;





import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jwetherell.augmented_reality.R;
import com.jwetherell.augmented_reality.data.ARData;

public class Main extends Activity{
	 Button b;
	 public static EditText t;
	 public static Location l;
	 public static   double lat,lon,alt;
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.main);
		  b=(Button)findViewById(R.id.button1);
		  t=(EditText)findViewById(R.id.editText1);
		  b.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if(t.getText().equals(" "))
					{
						Toast.makeText(getApplicationContext(), "No message",Toast.LENGTH_SHORT).show();
					}
					else
					{
						l = ARData.getCurrentLocation();
				    	lat=l.getLatitude();
				    	lon=l.getLongitude();
				    	alt=l.getAltitude();
					startActivity(new Intent(Main.this,Demo.class));
				      Main.this.finish();
					}
				}
			});
	}
}
