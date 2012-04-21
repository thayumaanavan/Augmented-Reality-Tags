package com.vikky.augmented_reality.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vikky.augmented_reality.R;

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
						LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

						LocationListener mlocListener = new Location();


						mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
						//l = ARData.getCurrentLocation();
				    	//lat=l.getLatitude();
				    	//lon=l.getLongitude();
				    	//alt=l.getAltitude();
					startActivity(new Intent(Main.this,Demo.class));
				      Main.this.finish();
					}
				}
			});
	}
	 
	 public class Location implements LocationListener {
			
			public void onLocationChanged(android.location.Location loc){
				lat=loc.getLatitude();
				lon=loc.getLongitude();
				
				//String Text = "My current location is: " +"Latitud ="  + loc.getLatitude() +"Longitud = " + loc.getLongitude();
				
				//Toast.makeText( getApplicationContext(),Text,Toast.LENGTH_SHORT).show();

			}


			@Override

			public void onProviderDisabled(String provider){
			Toast.makeText( getApplicationContext(),"Gps Disabled",Toast.LENGTH_SHORT ).show();

			}


			@Override

			public void onProviderEnabled(String provider){

			Toast.makeText( getApplicationContext(),"Gps Enabled",Toast.LENGTH_SHORT).show();

			}


			@Override

			public void onStatusChanged(String provider, int status, Bundle extras)

			{


			}


			
			
		}

}
