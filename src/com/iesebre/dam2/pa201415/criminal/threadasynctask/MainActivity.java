package com.iesebre.dam2.pa201415.criminal.threadasynctask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnClickListener {
     //set controls
	 private TextView txtImage ;
	 private ImageView imgView;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//Load controls
		txtImage =(TextView)findViewById(R.id.txtImage);
		imgView = (ImageView)findViewById(R.id.ImgView);
		Button btnGetImage = (Button)findViewById(R.id.btnGetImage);
		Button btnAnotherTask = (Button)findViewById(R.id.btnAnotherTask);
		//Check internet connection
		if(isConnected()){
			txtImage.setText("Connected to Internet");
			//Set Listeners
			btnGetImage.setOnClickListener(this);
			btnAnotherTask.setOnClickListener(this);
		}else{
			txtImage.setText("Check internet connection before use app!");
			
		}
		
	}
	//onClick Buttons
		@Override
		public void onClick(View v) {
			URI uri = null;
			try {
				 uri = new URI("http://defencely.com/blog/wp-content/uploads/2013/06/ways-hackers-hack-your-website-e1371080108770.jpg");
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			switch (v.getId()) {
			   case R.id.btnGetImage:
				   //Instance of AsyncTask class
				   GetImageAsync getImageInstance = new GetImageAsync();
				   getImageInstance.execute(uri);
				break;
			   case R.id.btnAnotherTask:
				   Toast.makeText(this, "Now we are doing another task while we download image",Toast.LENGTH_LONG).show();
			}
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	// check network connection
    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected())
                return true;
            else
                return false;  
    }
    private class GetImageAsync extends AsyncTask<URI, Void, Bitmap> {
    	InputStream instream=null;
    	@Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Show the message in textView
            txtImage.setText("");
            txtImage.setText("Downloading Image ....");
    	}
    	
    	@Override
        protected Bitmap doInBackground(URI... uri) {
    		
    		HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(uri[0]);
    		// Adding Headers .. 
    		// Execute the request
    		HttpResponse response;
    		Bitmap bmp = null;
    		
    		try {
    		    response = httpclient.execute(httpget);
    		if (response.getStatusLine().getStatusCode() == 200) {
    		    // Get hold of the response entity
    		    HttpEntity entity = response.getEntity();
    		    if (entity != null) {
    		     instream = entity.getContent();
    		     //Here we get the input stream and we read the image from it
        		 ByteArrayOutputStream baos = new ByteArrayOutputStream();
        		 int bufferSize = 1024;
        		 byte[] buffer = new byte[bufferSize];
        		 int len = 0;
        		 try {
        		     // instream is content got from httpentity.getContent()
        		     while ((len = instream.read(buffer)) != -1) {
        		     baos.write(buffer, 0, len);
        		     }
        		     baos.close();
        		 } catch (IOException e) {
        		     e.printStackTrace();
        		 }
        		 byte[] b = baos.toByteArray();
        		  bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
    		}
    		
    	}
    		}catch(Exception e){
    			
    		}
    		return bmp;
    	}
    	 @Override
         protected void onPostExecute(Bitmap bmp) {
    		txtImage.setText("");
    		txtImage.setText("Download finished");
    		 imgView.setImageBitmap(bmp); 
         }	
    	
    }
}
