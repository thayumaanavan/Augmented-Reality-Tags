package com.vikky.augmented_reality.activity;


import com.vikky.augmented_reality.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Splashscreen extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        ImageView i=(ImageView)findViewById(R.id.imageView1);
        i.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Splashscreen.this,Main.class));
		        Splashscreen.this.finish();
			}
		});
        
    }

}
