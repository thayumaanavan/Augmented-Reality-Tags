package com.vikky.augmented_reality.activity;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.vikky.augmented_reality.R;
import com.vikky.augmented_reality.data.ARData;
import com.vikky.augmented_reality.data.LocalDataSource;
import com.vikky.augmented_reality.data.NetworkDataSource;
import com.vikky.augmented_reality.data.TwitterDataSource;
import com.vikky.augmented_reality.data.WikipediaDataSource;
import com.vikky.augmented_reality.ui.Marker;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;


public class Demo extends AugmentedReality {
    private static final String TAG = "Demo";
    private static final String locale = Locale.getDefault().getLanguage();
    private static final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(1);
    private static final ThreadPoolExecutor exeService = new ThreadPoolExecutor(1, 1, 20, TimeUnit.SECONDS, queue);
	private static final Map<String,NetworkDataSource> sources = new ConcurrentHashMap<String,NetworkDataSource>();    

    
	/**	 * {@inheritDoc}
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Local
        LocalDataSource localData = new LocalDataSource(this.getResources());
        ARData.addMarkers(localData.getMarkers());
        
        Toast t1 = Toast.makeText(getApplicationContext(),Main.t.getText().toString()+Main.lat+","+Main.lon , Toast.LENGTH_SHORT);
        t1.setGravity(Gravity.CENTER, 0, 0);
        t1.show();
        //Network
        NetworkDataSource twitter = new TwitterDataSource(this.getResources());
        sources.put("twitter",twitter);
        NetworkDataSource wikipedia = new WikipediaDataSource(this.getResources());
        sources.put("wiki",wikipedia);
      //  NetworkDataSource buzz = new BuzzDataSource(this.getResources());
      //  sources.put("buzz",buzz);
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
    public void onStart() {
        super.onStart();
        
        Location last = ARData.getCurrentLocation();
        updateData(last.getLatitude(),last.getLongitude(),last.getAltitude());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(TAG, "onOptionsItemSelected() item="+item);
        switch (item.getItemId()) {
            case R.id.showRadar:
                showRadar = !showRadar;
                item.setTitle(((showRadar)? "Hide" : "Show")+" Radar");
                break;
            case R.id.showZoomBar:
                showZoomBar = !showZoomBar;
                item.setTitle(((showZoomBar)? "Hide" : "Show")+" Zoom Bar");
                zoomLayout.setVisibility((showZoomBar)?LinearLayout.VISIBLE:LinearLayout.GONE);
                break;
            case R.id.exit:
                finish();
                break;
        }
        return true;
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);
        
        updateData(location.getLatitude(),location.getLongitude(),location.getAltitude());
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void markerTouched(Marker marker) {
        Toast t = Toast.makeText(getApplicationContext(), marker.getName(), Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
	}

    /**
     * {@inheritDoc}
     */
    @Override
	protected void updateDataOnZoom() {
	    super.updateDataOnZoom();
        Location last = ARData.getCurrentLocation();
        updateData(last.getLatitude(),last.getLongitude(),last.getAltitude());
	}
    
    private void updateData(final double lat, final double lon, final double alt) {
        try {
            exeService.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        for (NetworkDataSource source : sources.values())
                            download(source, lat, lon, alt);
                    }
                }
            );
        } catch (RejectedExecutionException rej) {
            Log.w(TAG, "Not running new download Runnable, queue is full.");
        } catch (Exception e) {
            Log.e(TAG, "Exception running download Runnable.",e);
        }
    }
    
    private static boolean download(NetworkDataSource source, double lat, double lon, double alt) {
		if (source==null) return false;
		
		String url = null;
		try {
			url = source.createRequestURL(lat, lon, alt, ARData.getRadius(), locale);    	
		} catch (NullPointerException e) {
			return false;
		}
    	
		List<Marker> markers = null;
		try {
			markers = source.parse(url);
		} catch (NullPointerException e) {
			return false;
		}

    	ARData.addMarkers(markers);
    	return true;
    }
}
