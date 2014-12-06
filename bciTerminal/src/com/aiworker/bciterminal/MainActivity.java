package com.aiworker.bciterminal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.aiworker.bciterminal.MusicPlayerView;
import com.aiworker.bciterminal.MusicPlayerView.MusicPlayerThread;

import com.neurosky.thinkgear.TGDevice;
import com.neurosky.thinkgear.TGEegPower;

 

public class MainActivity extends Activity implements SurfaceHolder.Callback{
	
	private BluetoothAdapter bluetoothAdapter;
	TGDevice tgDevice;
	private static final boolean RAW_ENABLED = false; // false by default
	
	TextView tv_info;	TextView tv_TimeToSel; 
	TextView tv_consoleBoard; TextView tv_consoleLine;
	private int At = 50;     private int Med = 50;

    private int delta = 0; private int high_alpha = 0; private int high_beta = 0; private int low_alpha = 0;
    private int low_beta = 0; private int low_gamma = 0; private int mid_gamma = 0; private int theta = 0;
    
	TextView tv_Med;    TextView tv_Att;    TextView tv_Vel;    TextView tv_AmM;    
    	/** A handle to the thread that's actually running the animation. */
    private MusicPlayerThread mMusicPlayerThread;   
    	/** A handle to the View in which the game is running. */
    private MusicPlayerView mMusicPlayerView;
    
    String KEY_play_stop_status;  String play_stop_status = "false";
    
    final int ActivityTwoRequestCode = 0;
    Bitmap myBitmap;
    private Map<String, Integer> tickData;
    
    // -- camera 
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    boolean previewing = false;
    LayoutInflater controlInflater = null;
    private boolean flag_camera = true; 
    private int cameraId = 0;
    public String filename = "empty";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // -- tell system to use the layout defined in our XML file
        setContentView(R.layout.activity_main);
        //setContentView(R.layout.activity_main_tablet);
        
        // -- get the between instance stored values (status of music player)
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        if (preferences.getString(KEY_play_stop_status, null) != null){
        	play_stop_status =  preferences.getString(KEY_play_stop_status, null);
        }
        
        mMusicPlayerView = (MusicPlayerView) findViewById(R.id.lunar);
        mMusicPlayerThread = mMusicPlayerView.getThread();

        // -- give the MusicPlayerView a handle to the TextView used for messages
        mMusicPlayerView.setTextView((TextView) findViewById(R.id.text));
		tv_info = (TextView) findViewById(R.id.text);
        // -- give the GlassView a handle to the TextView used for messages
        tv_Att = (TextView) findViewById(R.id.Att_text);
        tv_Med = (TextView) findViewById(R.id.Med_text);       
        tv_Vel = (TextView) findViewById(R.id.Vel_text);
        tv_AmM = (TextView) findViewById(R.id.AmM_text);
        
        tv_TimeToSel = (TextView) findViewById(R.id.TimeToSel);
        tv_consoleBoard = (TextView) findViewById(R.id.console_info);
        tv_consoleLine = (TextView) findViewById(R.id.console_line);
        
				
        /* Checking BT and connecting to the TG device */
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {	// Alert user that Bluetooth is not available
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        } else {
            tgDevice = new TGDevice(bluetoothAdapter, handler);
            //Toast.makeText(this, "Bluetooth is available ir", Toast.LENGTH_LONG).show();
            doStuff();
        }	
         

        // -- camera block        
        	//getWindow().setFormat(PixelFormat.UNKNOWN);
        surfaceView = (SurfaceView)findViewById(R.id.camerapreview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        	//surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
               
		Log.d(getString(R.string.app_name), "ir_d onCreate()"); 
       
      /*  Button buttonTakePicture = (Button)findViewById(R.id.takepicture);
        buttonTakePicture.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				camera.takePicture(myShutterCallback, myPictureCallback_RAW, myPictureCallback_JPG);
			}});*/
                  
	}

	@Override
	public void onStart() {    	       
		super.onStart();   
		Log.d(getString(R.string.app_name), "ir_d onStart()");
	}	      
	@Override
	public void onResume() {
		super.onResume();
		//initializeCamera();
	      
		//mMusicPlayerView.getThread().unpause(); // pause game when Activity pauses
        //mMusicPlayerView.getThread().setRunning(true); //correctly destroy SurfaceHolder, ir   
          
	    Log.d(getString(R.string.app_name), "ir_d onResume()");
	}
    @Override
    public void onPause() {        
        super.onPause();
        
    	mMusicPlayerView.getThread().pause(); // pause game when Activity pauses
        mMusicPlayerView.getThread().setRunning(false); //correctly destroy SurfaceHolder, ir   
               
        // -- store values between instances here  
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);  
        SharedPreferences.Editor editor = preferences.edit();  // Put the values from the UI 
        	//editor.putBoolean(KEY_play_stop_status, true); // value to store  
        editor.putString(KEY_play_stop_status, String.valueOf(mMusicPlayerView.getThread().play_flag));
        	// -- commit to storage 
        editor.commit(); 
           
        // -- prevent several activity to be open/closed activity on home button pressed
        finish();
        
        Log.d(getString(R.string.app_name), "ir_d onPause()");
    }
    @Override
	public void onStop() { 
		super.onStop();
        try {
            if (tgDevice != null) {
                tgDevice.close();
            }

          //  releaseCamera();

        } catch (NullPointerException e) { }
        
        Log.d(getString(R.string.app_name), "ir_d onStop()");
	}
    @Override
	public void onDestroy() {  
    	super.onDestroy();  
       /* try {
            if (tgDevice != null) {
                tgDevice.close();
            }

           // releaseCamera();

        } catch (NullPointerException e) { } */

       // mMusicPlayerView.getThread().pause(); // pause game when Activity pauses
       // mMusicPlayerView.getThread().setRunning(false); //correctly destroy SurfaceHolder, ir   
           
	    Log.d(getString(R.string.app_name), "ir_d onDestroy()");
	}    
   
	
	    /**
	     * Notification that something is about to happen, to give the Activity a
	     * chance to save state.
	     * * @param outState a Bundle into which this Activity should save its state
	     */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	        // just have the View's thread save its state into our Bundle
	        super.onSaveInstanceState(outState);
	       // mGlassThread.saveState(outState);
	}
	   
		// -- tgDevice State
	public void doStuff() {
	   //Toast.makeText(this, "connecting...", Toast.LENGTH_SHORT).show();
	   if (tgDevice.getState() != TGDevice.STATE_CONNECTING && tgDevice.getState() != TGDevice.STATE_CONNECTED) {
	       tgDevice.connect(RAW_ENABLED);
	   }
	}
	
		    
	    // -- save bitmap of screenshot
	public void saveBitmap(Bitmap bitmap) {
	     //   String filePath = Environment.getExternalStorageDirectory() + File.separator + "Pictures/screenshot.png";
	       // File imagePath = new File(filePath);
	        
	        File pictureFileDir = getDir();
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
    	    String date = dateFormat.format(new Date());
    	    String photoFile = "eeg_scr" + date + ".png";

    	    String filePath = pictureFileDir.getPath() + File.separator + photoFile;
    	    File imagePath = new File(filePath);
	        
	        FileOutputStream fos;
	        try {
	            fos = new FileOutputStream(imagePath);
	            bitmap.compress(CompressFormat.PNG, 100, fos);
	            fos.flush();
	            fos.close();
	           // sendMail(filePath);
	        } catch (FileNotFoundException e) {
	            Log.e("GREC", e.getMessage(), e);
	        } catch (IOException e) {
	            Log.e("GREC", e.getMessage(), e);
	        }
	}
	    
	    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	        switch (keyCode) {
	            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
	            case KeyEvent.KEYCODE_HEADSETHOOK:
	                //startService(new Intent(MusicService.ACTION_TOGGLE_PLAYBACK));
	                return true;
	        }
	        return super.onKeyDown(keyCode, event);
	}
	    	    
	    
	    
  // -- Handles messages from TGDevice 
	private final Handler handler = new Handler() {
	   @Override
	   public void handleMessage(Message msg) {               
	                switch (msg.what) {
	                case TGDevice.MSG_STATE_CHANGE:
	                    /*display message according to state change type */
	                    switch (msg.arg1) {
	                    case TGDevice.STATE_IDLE:
	                        break;
	                    case TGDevice.STATE_CONNECTING:
	                    	//mGlassThread.setTGStatus("Connecting...");
	                    	tv_info.setText("Connecting...");
	                    	//releaseCamera();
	                        break;
	                    case TGDevice.STATE_CONNECTED:
	                        tgDevice.start();
	                        tv_info.setText("Connected");
	                        // -- start thread with eeg_launcher
	                        mMusicPlayerThread.doStart(); 	  
	                        mMusicPlayerView.getThread().play_flag = Boolean.valueOf(play_stop_status);                     	        	    		
	                        break;
	                    case TGDevice.STATE_NOT_FOUND:
	                    	tv_info.setText("neurosky mindwave mobile was not found");
	                        break;
	                    case TGDevice.STATE_NOT_PAIRED:
	                    	tv_info.setText("neurosky mindwave mobile not paired !!!!!");
	                        break;
	                    case TGDevice.STATE_DISCONNECTED:
	                    	tv_info.setText("Disconnected");
	                    }

	                    break;
	                case TGDevice.MSG_POOR_SIGNAL:
	                         //int TGState = msg.arg1;
	                    break;
	                case TGDevice.MSG_RAW_DATA:
	                	//raw1 = msg.arg1;
	                    //tv.append("Got raw: " + msg.arg1 + "\n");                  
	                    break;
	                case TGDevice.MSG_HEART_RATE:
	                    //tv.append("Heart rate: " + msg.arg1 + "\n");
	                    break;
	                case TGDevice.MSG_ATTENTION:
	                    // -- First send Attention data to the backend in async way
	                    //APIClient.postData(null, "attention", String.valueOf(msg.arg1), null); // old
	                	APIClient.collectAttention(null, msg.arg1);
	                	//Log.e(getString(R.string.app_name), "camera.takePicture()");  
	                		 	               
	                    At = msg.arg1;         
	                    tv_Att.setText(String.valueOf(At));
	                    mMusicPlayerThread.setAttention(At);
	                    
	                    // -- do appropriate action for music player
	                    	// --play/stop/playnext
	                   /* if (mMusicPlayerView.getThread().play_flag == true)
                    		{ 
	                    	 startService(new Intent(MusicService.ACTION_PLAY)); 
	                    	}
	                    if (mMusicPlayerView.getThread().stop_flag == true)
                			{ 
	                    	 startService(new Intent(MusicService.ACTION_STOP));
                			}
	                    
	                    if (mMusicPlayerView.getThread().next_flag == true)
                			{ 
	                    	 startService(new Intent(MusicService.ACTION_SKIP));
                			 mMusicPlayerView.getThread().next_flag = false;
                			}*/
	                    
	                   /* if (mMusicPlayerView.getThread().back_flag == true)
            				{ 
	                    	 //startService(new Intent(MusicService.ACTION_PAUSE));
	                    	 startService(new Intent(MusicService.ACTION_STOP)); 
	                    	 mMusicPlayerView.getThread().back_flag = false;
            				// onBackPressed();
            				} */
	                    
	                    	// -- camera/twitter/PrtSc
	                    if (mMusicPlayerView.getThread().Picture_flag == true)
        				{ 	                    	
	                    	camera.takePicture(myShutterCallback, myPictureCallback_RAW, myPictureCallback_JPG);	                    	
	                    	mMusicPlayerView.getThread().Picture_flag = false;
        				}
	                    if (mMusicPlayerView.getThread().TwitPicture_flag == true)
        				{ 	                    	
	                    	//camera.takePicture(myShutterCallback, myPictureCallback_RAW, myPictureCallback_JPG);		                    	                    	
	                    	mMusicPlayerView.getThread().TwitPicture_flag = false;
        				}
	                    
	                    if (mMusicPlayerView.getThread().Prtscr_flag == true)
        				{ 	                    	
		                    View v1 = findViewById(android.R.id.content).getRootView() ; 
	                    	v1.setDrawingCacheEnabled(true);
		                    myBitmap = v1.getDrawingCache();
		                    saveBitmap(myBitmap);
	                    	
	                    	mMusicPlayerView.getThread().Prtscr_flag = false;
	                    		// onBackPressed();
        				}
	                    
	                   // if (mMusicPlayerView.getThread().MusicPlayerFlag == true) { camera.stopPreview(); previewing = false;}
	                   // if (mMusicPlayerView.getThread().DnaConsoleFlag == true) {camera.stopPreview(); previewing = false;}
	                   // if (mMusicPlayerView.getThread().MusicPlayerFlag == false) { previewing = true;}
	                   // if (mMusicPlayerView.getThread().DnaConsoleFlag == false) { previewing = true;}
	                    
	                    	// -- display velocity based on accel_alpha [0..2.5]
	                    float vel = mMusicPlayerView.getThread().accel_alpha;
	                    if (vel>=1.25f) 			{tv_Vel.setText("4"); tv_Vel.setTextColor(Color.RED);}
	                    if (vel>=1.0f && vel<1.25f) {tv_Vel.setText("3"); tv_Vel.setTextColor(Color.GREEN);}
	                    if (vel>=0.75f && vel<1.0f) {tv_Vel.setText("2"); tv_Vel.setTextColor(Color.YELLOW);}
	                    if (vel>=0.5f && vel<0.75f) {tv_Vel.setText("1"); tv_Vel.setTextColor(Color.WHITE);}
	                    if (vel<0.5f)				{tv_Vel.setText("0"); tv_Vel.setTextColor(Color.WHITE);}  
	                    
	                    	// -- display time to action selection
	                    tv_TimeToSel.setTextColor(Color.WHITE);
	                    float tts = mMusicPlayerView.getThread().TimeToSelect;
	                    tts = tts*10f;
	                    tts = Math.round(tts);
	                    tts = tts/10f;
	                    //if (tts < 3f && vel<=0 && mMusicPlayerView.getThread().flag_Cursor)
	                    //if (mMusicPlayerView.getThread().action_cancel_flag){tv_TimeToSel.setText("cancel");}
	                    if (tts < 3f &&  mMusicPlayerView.getThread().flag_Cursor) {
	                    	tv_TimeToSel.setTextSize(20); tv_TimeToSel.setText(String.valueOf(Math.round(tts)) ); 
	                    	mMusicPlayerView.getThread().msgBoard = " ";}
	                    else {
	                    	tv_TimeToSel.setTextSize(20); tv_TimeToSel.setText(mMusicPlayerView.getThread().msgBoard);
	                    	}
	                    if (mMusicPlayerView.getThread().action_cancel_flag){
	                    	tv_TimeToSel.setTextSize(20);tv_TimeToSel.setText("cancel");
	                    	mMusicPlayerView.getThread().msgBoard = "";
	                    	}
	                    
	                    tv_consoleBoard.setTextSize(15);tv_consoleBoard.setText(mMusicPlayerView.getThread().consoleBoard);
	                    tv_consoleLine.setTextSize(30); tv_consoleLine.setText(mMusicPlayerView.getThread().consoleLine);
	                   
	                    

	                    /* tv_Att.setText(String.valueOf(At)); // display meditation
	                    	// -- change size and color of Att text view
	                    if (At < 30) {
	                    	//tv_Att.setTextColor(Color.YELLOW);
	                    	tv_Att.setTextSize(20);
	                    } else {
	                        if (At <70) {
	                        	//tv_Att.setTextColor(Color.GREEN);
	                        	tv_Att.setTextSize(30);
	                        } else {
	                        	//tv_Att.setTextColor(Color.RED);
	                        	tv_Att.setTextSize(40);
	                        }                    
	                    } */
 	                    		                    
	                    // --saving data to file
	                    String filename; 
	                    String user_g = "ihar";
	                    Time now = new Time();
	                    now.setToNow();
	                    String date_time = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));                    
	                    filename = "bciTerminal_" + date_time + ".csv";
	                    
	                    writeToExternalStoragePublic(filename, user_g, now, At, Med);
	                    
	                    break;
	                    
	                case TGDevice.MSG_MEDITATION:
	                    //APIClient.postData(null, "meditation", String.valueOf(msg.arg1), null); //old code
	                	APIClient.collectMeditation(null, msg.arg1);

	                	tickData = new HashMap<String, Integer>();
	                    tickData.put("meditation", msg.arg1);
	                    
	                    Med = msg.arg1;
	                    tv_Med.setText(String.valueOf(Med));
	                    mMusicPlayerThread.setMeditation(Med);
	                    
	                    // -- change size and color of Med text view
	                   /* if (Med < 30) {
	                    	//tv_Med.setTextColor(Color.YELLOW);
	                    	//tv_Med.setTextSize(20);
	                    } else {
	                        if (Med <70){
	                        	//tv_Med.setTextColor(Color.GREEN);
	                        	//tv_Med.setTextSize(30);
	                        } else {
	                        	//tv_Med.setTextColor(Color.RED);
	                        	//tv_Med.setTextSize(40);
	                        }                    
	                    }*/
	 
	                    tv_AmM.setText(String.valueOf(At-Med)); // display Att-Med
	                    	// -- change size and color of Att-Med text view                   
	                    if (Math.abs(At-Med) <= 15)	{tv_AmM.setTextSize(25); tv_AmM.setTextColor(Color.GRAY);}
	                    	else if (Math.abs(At-Med) <= 30) {tv_AmM.setTextSize(25); tv_AmM.setTextColor(Color.GRAY); }
	                    
	                    if (At-Med < -45 || At-Med > 45) {tv_AmM.setTextSize(25); tv_AmM.setTextColor(Color.GREEN);}
	                    	else if (At-Med < -30 || At-Med > 30) {tv_AmM.setTextSize(25); tv_AmM.setTextColor(Color.GREEN); }
	                                                           
	                    
	                    break;
	                case TGDevice.MSG_BLINK:
	                    //tv.append("Blink: " + msg.arg1 + "\n");
	                    break;
	                case TGDevice.MSG_RAW_COUNT:
	                    //tv.append("Raw Count: " + msg.arg1 + "\n");
	                    break;
	                case TGDevice.MSG_LOW_BATTERY:
	                    Toast.makeText(getApplicationContext(), "Low battery!", Toast.LENGTH_SHORT).show();
	                    break;
	                case TGDevice.MSG_RAW_MULTI:
	                    //TGRawMulti rawM = (TGRawMulti)msg.obj;
	                    //tv.append("Raw1: " + rawM.ch1 + "\nRaw2: " + rawM.ch2);
	                
	                case TGDevice.MSG_SLEEP_STAGE:
	                	//sleep_stage = msg.arg1;
	                	break;
	                case TGDevice.MSG_EEG_POWER:
	                    TGEegPower eegPower = (TGEegPower) msg.obj;
	                    APIClient.collectEEGPower(null, eegPower);
	                    
	                    delta = eegPower.delta;
	                    high_alpha = eegPower.highAlpha;
	                    high_beta = eegPower.highBeta;
	                    low_alpha = eegPower.lowAlpha;
	                    low_beta = eegPower.lowBeta;
	                    low_gamma = eegPower.lowGamma;
	                    mid_gamma = eegPower.midGamma;
	                    theta = eegPower.theta;
	                    
	                    break;
	                default:
	                    break;
	                }

	            }
	        };
	        
	    // -- Handles messages from Camera 
	        ShutterCallback myShutterCallback = new ShutterCallback(){

	    		@Override
	    		public void onShutter() {
	    			// TODO Auto-generated method stub
	    			
	    		}
	    	};
	    		
	    	PictureCallback myPictureCallback_RAW = new PictureCallback(){

	    		@Override
	    		public void onPictureTaken(byte[] arg0, Camera arg1) {
	    			// TODO Auto-generated method stub
	    			
	    		}
	    	}; 
	    		
	    	PictureCallback myPictureCallback_JPG = new PictureCallback(){

	    	@Override
	    	public void onPictureTaken(byte[] arg0, Camera arg1) {
	    			// TODO Auto-generated method stub
	    		//Bitmap bitmapPicture = BitmapFactory.decodeByteArray(arg0, 0, arg0.length);
	    		
	    		Log.d(getString(R.string.app_name), "ir_d onPictureTaken() --> bitmapPicture");
	    		try {
					camera.setPreviewDisplay(surfaceHolder);
					camera.startPreview();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//previewing = true;
				
				// --- saving picture
				File pictureFileDir = getDir();
	    	    if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
	    	      //Log.d(MakePhotoActivity.DEBUG_TAG, "Can't create directory to save image.");
	    	     // Toast.makeText(context, "Can't create directory to save image.", Toast.LENGTH_LONG).show();
	    	      return;
	    	    }
	    	    
	    	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
	    	    String date = dateFormat.format(new Date());
	    	    String photoFile = "eeg_" + date + ".jpg";

	    	    filename = pictureFileDir.getPath() + File.separator + photoFile;

	    	    File pictureFile = new File(filename);
	    	    try {
		    	      FileOutputStream fos = new FileOutputStream(pictureFile);
		    	      fos.write(arg0);
		    	      fos.close();
		    	     // Toast.makeText(context, "New Image saved:" + photoFile,  Toast.LENGTH_LONG).show();
		    	    } catch (Exception error) {
		    	     // Log.d(MakePhotoActivity.DEBUG_TAG, "File" + filename + "not saved: "  + error.getMessage());
		    	      //Toast.makeText(context, "Image could not be saved.", Toast.LENGTH_LONG).show();
		    	    }
	    	     
	    	   // releaseCamera();
	    	}
	    };

	    public void writeToExternalStoragePublic(String filename,
	    		String user_g_l, Time now_l, int At_l, int Med_l) {
	    	
	        String packageName = this.getPackageName();
	        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
	        		+ "/Android/data/" + packageName + "/files/";

	        String titles = "bciApp;username;time;att;med;"
	        		+ "delta;high_alpha;high_beta;low_alpha;low_beta;low_gamma;mid_gamma;theta;";
	        
	        try {
	               boolean exists = (new File(path)).exists();
	               if (!exists) {
	                    new File(path).mkdirs();
	               }
	                   // -- open output stream
	               File file = new File(path + filename);
	               if(file.exists()) {
	                    	FileOutputStream fOut = new FileOutputStream(path + filename,true);
	                    	// -- write Head and integers as separated ascii's
	                    	//fOut.write((titles.toString() + "\n").getBytes());
	                    	fOut.write(("bciTerminal;").getBytes());
	                    	fOut.write((user_g_l.toString() + ";").getBytes());
	                    	fOut.write((now_l.toString() + ";").getBytes());
	                        fOut.write((Integer.valueOf(At_l).toString() + ";").getBytes());
	                        fOut.write((Integer.valueOf(Med_l).toString() + ";").getBytes());
	                        fOut.write((Double.valueOf(delta).toString() + ";").getBytes());                        
	                        fOut.write((Double.valueOf(high_alpha).toString() + ";").getBytes());
	                        fOut.write((Double.valueOf(high_beta).toString() + ";").getBytes());
	                        fOut.write((Double.valueOf(low_alpha).toString() + ";").getBytes());
	                        fOut.write((Double.valueOf(low_beta).toString() + ";").getBytes());
	                        fOut.write((Double.valueOf(low_gamma).toString() + ";").getBytes());
	                        fOut.write((Double.valueOf(mid_gamma).toString() + ";").getBytes());
	                        fOut.write((Double.valueOf(theta).toString() + ";").getBytes());
	                        fOut.flush();
	                        fOut.close();
	                    }    
	                else {
	                    	// -- write integers as separated ascii's
	                    	FileOutputStream fOut = new FileOutputStream(path + filename,true);
	                    	fOut.write((titles.toString() + "\n").getBytes());
	                        fOut.flush();
	                        fOut.close();
	                    }
	                    
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	          
	    }	  
	    
	        private File getDir() {
	    	    File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
	    	    return new File(sdDir, "eeg_picture");
	        }
	    	
	    	
	    	@Override
	    	public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) {			
	    		//initializeCamera();
	            // -- If your preview can change or rotate, take care of those events here.
	            // -- Make sure to stop the preview before resizing or reformatting it.
	    	
	            if (holder.getSurface() == null){
	            	Log.d(getString(R.string.app_name), "ir_d surfaceChanged --> preview surface does not exist");
	                return;
	            }

	            // -- stop preview before making changes
	            try {
	                camera.stopPreview();
	                Log.d(getString(R.string.app_name), "ir_d surfaceChanged --> camera.stopPreview()  " + camera);
	            } catch (Exception e){ // ignore: tried to stop a non-existent preview
	            	Log.d(getString(R.string.app_name), "ir_d surfaceChanged --> Error camera.stopPreview()  " + camera);
	            }

	            // set preview size and make any resize, rotate or
	            // reformatting changes here

	            // -- start preview with new settings	            
	    		if (camera != null) {
	    			try { 
	    				camera.setPreviewDisplay(holder);
	    				// -- camera.setPreviewDisplay(surfaceHolder);
	    				camera.startPreview();
	                    Log.d(getString(R.string.app_name),"ir_d surfaceChanged --> camera.setPreviewDisplay()  " + camera);
	                } catch (IOException e) 
	                { Log.d(getString(R.string.app_name), "ir_d surfaceChanged --> Error setting camera preview:  " + camera); }
	                	
	    			camera.setDisplayOrientation(90);
	            }
	    		
	    	}

	    	@Override
	    	public void surfaceCreated(SurfaceHolder holder) {
	    		// -- The Surface has been created, now tell the camera where to draw the preview.
	    		initializeCamera();

	    		Log.d(getString(R.string.app_name), "ir_d surfaceCreated()");
	    		
	    	}

	    	@Override
	    	public void surfaceDestroyed(SurfaceHolder holder) {
	    	    // Surface will be destroyed when we return, so stop the preview.
	    	    if (camera != null) {// Call stopPreview() to stop updating the preview surface.
	    	        camera.stopPreview();
	    	    }
	    	    releaseCamera();
	    	    
	    		Log.d(getString(R.string.app_name), "ir_d surfaceDestroyed()");
	    	}

	        private void initializeCamera()
	        { 
	            // -- do we have a camera?
	            if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
	              Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG).show();
	            } else {
	              //cameraId = findFrontFacingCamera();
	              cameraId = 0;
	              if (cameraId < 0) {
	                Toast.makeText(this, "No front facing camera found.",Toast.LENGTH_LONG).show();
	              }
	            }
	            
	            // -- open camera
	            try {
	            	releaseCamera();
	            	camera = Camera.open(cameraId);
	            	Log.d(getString(R.string.app_name), "ir_d initializeCamera() --> Camera.open()  " + camera + "  " + cameraId);
	            } catch (Exception e) {
	                Log.d(getString(R.string.app_name), "ir_d initializeCamera() failed to open Camera");
	                
	            }
	            
	        }

	        private void releaseCamera() {
	            if (camera != null) {
	                camera.stopPreview();
	                camera.release();
	                camera = null;
	                
	                Log.d(getString(R.string.app_name), "ir_d releaseCamera() --> Camera.release() true  " + camera );
	            }
	            
	            Log.d(getString(R.string.app_name), "ir_d releaseCamera() --> Camera.release() false  " + camera );
	            
	        }

	        private int findFrontFacingCamera() {
	            int cameraId = -1;
	            // Search for the front facing camera
	            int numberOfCameras = Camera.getNumberOfCameras();
	            for (int i = 0; i < numberOfCameras; i++) {
	              CameraInfo info = new CameraInfo();
	              Camera.getCameraInfo(i, info);
	              if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
	            	  Log.d(getString(R.string.app_name), "ir_d findFrontFacingCamera() CAMERA_FACING_FRONT found" );
	                cameraId = i;
	                break;
	              }
	            }
	            return cameraId;
	          }
}