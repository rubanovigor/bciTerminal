package com.aiworker.bciterminal;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;


/**
* GlassView - main game screen
*/
class MusicPlayerView extends SurfaceView implements SurfaceHolder.Callback {
class MusicPlayerThread extends Thread {
	float RR; float ZZ;
	String GameMode="b"; String msgBoard=""; String consoleBoard=""; String consoleLine="";
	int EEG_launcher_N = 3; int delta = 360/EEG_launcher_N; 
	int ClusterLeftX = -30; int ClusterCenterX = 0; int ClusterRightX = 30; int ClusterDelta = 0; 
	String flag; 
	boolean play_flag = false; boolean stop_flag = false; boolean back_flag = false;
	boolean next_flag = false; 	boolean Prtscr_flag = false; boolean Picture_flag = false;
	boolean TwitPicture_flag = false;

	// -- eeg launcher by default
	//boolean EegLauncherFlag = true;	boolean MusicPlayerFlag = false;
	//boolean DnaConsoleFlag = false; boolean CameraFlag = false;
	// -- camera by default
	//boolean EegLauncherFlag = false;	boolean MusicPlayerFlag = false;
	//boolean DnaConsoleFlag = false; boolean CameraFlag = true;
	// -- terminal by default
	boolean EegLauncherFlag = false;	boolean MusicPlayerFlag = false;
	boolean DnaConsoleFlag = true; boolean CameraFlag = false;
	// -- music by default
	//boolean EegLauncherFlag = false;	boolean MusicPlayerFlag = true;
	//boolean DnaConsoleFlag = false; boolean CameraFlag = false;
	
	boolean action_cancel_flag = false;
    int At = 50; int Med = 50;   int ApM = 100;    int AmM = 0; 
    int console_length = 4;
    String[] console_str = new String[] {"-","-","-","-"}; //maximum rotation angle
    String console_str_action; 
    
    CharSequence TGStatus;
    float alpha = 0;  float alpha1_1=0;
    float scale_obj = 0.7f; float scale_obj_rot = 0.7f; float scale_obj_console = 0.5f;
  //  float CircleRadius = 1f; //280f;   // -- for android phone
    float CircleRadius = 100f;		// -- for google glass
    float accel_alpha = 0f;
    double elapsed = 0;   
    float curr_alpha_obj1 = 0; float curr_alpha_obj2 = 0; float curr_alpha_obj3 = 0; float curr_alpha_obj4 = 0;
    float curr_alpha_obj5 = 0; float curr_alpha_obj6 = 0; float curr_alpha_obj7 = 0; float curr_alpha_obj8 = 0;
    int[] obj1_center; int sel_action_i; float CursorX; float CursorY;
    boolean flag_Cursor = false;  int CursorI = 0; int CursorJ=1;
    float CursorX_delta = 1f; float CursorY_delta = 1f;      float TimeToSelect = 3f;
    boolean flag_submit = false;   
    int GenBinLet1 = -1;        int GenBinLet2 = -1;  String GenLetSucc = ""; 
    
    // -- declare objects for EEG Launcher
    private SkyBody LauncherMusicPlayer; private SkyBody LauncherDnaConsole; 
    	//private SkyBody LauncherMusicPlayerD; private SkyBody LauncherDnaConsoleD;
    private SkyBody LauncherCamera; //private SkyBody LauncherGoogle;
    	//private SkyBody LauncherCameraD; private SkyBody LauncherGoogleD;
    private SkyBody SelectedIcon; private SkyBody LeftBoarderIcon; private SkyBody RigthBoarderIcon;
    
    
    // -- declare objects for Music Player
    //private SkyBody Object1_1;  private SkyBody Object1_2;  private SkyBody Object1_3; 
    private SkyBody mp_Play;// private SkyBody mp_PlayD; 
    private SkyBody mp_Stop;//private SkyBody mp_StopD;
    private SkyBody mp_Next; //private SkyBody mp_NextD;
   // private SkyBody mp_Vol; private SkyBody mp_VolD;
   // private SkyBody mp_Back; private SkyBody mp_BackD;
   // private SkyBody IconPlay; //private SkyBody IconStop; private SkyBody IconSkip;
    //private SkyBody IconBlack; private SkyBody IconVol; 
    
    // -- declare objects for DNA Console
    private SkyBody DnaConsole_T; private SkyBody DnaConsole_C; private SkyBody DnaConsole_G; private SkyBody DnaConsole_A;
  //  private SkyBody DnaConsole_rigth;  private SkyBody DnaConsole_Back;   private SkyBody DnaConsole_left; private SkyBody DnaConsole_Cancel; 
    
   // private SkyBody ObjectA; private SkyBody ObjectC; private SkyBody ObjectT; private SkyBody ObjectG;
    //private SkyBody ObjectSubmit;
   // private SkyBody ObjectCursor; private SkyBody ObjectCursorDel; 
    //private SkyBody ObjectClear; 
    
    // -- declare objects for EEG Launcher
    private SkyBody CameraPicture;  private SkyBody CameraPrtScr;
   // private SkyBody CameraVideo; 
    	
    private float StarR; private float CursorR;  private float R_Gr_sphere_C;  private float R_Gr_sphere_S; 
    //float Sl1_0; float Sl1_1; float Sl1_2; float Sl1_3; float Sl1_4; float Sl1_5;
    float Cx_lb_l1; float Cx_lt_l1; float Cx_rt_l1; float Cx_rb_l1;
    float Cy_lb_l1; float Cy_lt_l1; float Cy_rt_l1; float Cy_rb_l1;
    float S2P_dist_rt; float S2P_dist_lt; float S2P_dist_rb; float S2P_dist_lb; 
    float P2C_dist;     float S2C_dist;
    float S2P_scale_lt;  float S2P_scale_rt; float S2P_scale_rb;  float S2P_scale_lb;
    float S2P_scale_lt_l2;
    float Scale_Gr_sphere_C = 1;
    
    int Level1_flag;         int Level2_flag;         int Level3_flag;
    double P_S1l1_rr; double P_S2l1_rr; 
    double P_S1l2_rr; double P_S2l2_rr;
    double P_S1l3_rr; double P_S2l3_rr;
    
    	// -- Complex px py
    float pX_C = 0; float pY_C = 0;
    float Z_re = 0; float Z_im = 0; float Z_re_sq=0; float Z_im_sq=0;float Z_re_f=0; float Z_im_f=0;
    
           
    int V_coeff = 4; // Velocity V=At/V_coeff*/
    int ScreenFlag = 1; //control game room(screen)
    
    /* background block */
    private Drawable BackGr_Image; private Drawable BackGr_Image1; private Drawable BackGr_Image2;
    private float BackGr_ImageScaleMax = 0.7f;
    private float BackGr_ImageScale = BackGr_ImageScaleMax;
    private int BackGr_ImageScalePi = 1; //  Pi/BackGr_ImageScalePi
              
    /** X/Y of lander center. */
    double pX;        double pY;
            
    //================================        
    /* State-tracking constants */
    public static final int STATE_LOSE = 1;       public static final int STATE_PAUSE = 2;
    public static final int STATE_READY = 3;      public static final int STATE_RUNNING = 4;
    public static final int STATE_WIN = 5;

    /** UI constants (i.e. the speed & fuel bars) */
   // private static final String KEY_X = "pX";      private static final String KEY_Y = "pY";
    //private static final String KEY_EEG = "EegLauncherFlag"; 
    //private static final String KEY_DNA = "DnaConsoleFlag";
    //private static final String KEY_MP = "MusicPlayerFlag";

    /** Current height/width of the surface/canvas. @see #setSurfaceSize        */
    private int BackGr_H = 1;        private int BackGr_W = 1;

    /** Message handler used by thread to interact with TextView */
    private Handler mHandler;

    /** Used to figure out elapsed time between frames */
    private long mLastTime;

    /** The state of the game. One of READY, RUNNING, PAUSE, LOSE, or WIN */
    private int mMode;

    /** Indicate whether the surface has been created & is ready to draw */
    private boolean mRun = false;

    /** Handle to the surface manager object we interact with */
    private SurfaceHolder mSurfaceHolder;

    public MusicPlayerThread(SurfaceHolder surfaceHolder, Context context, Handler handler) {
        // get handles to some important objects
        mSurfaceHolder = surfaceHolder;   mHandler = handler;      mContext = context;

        // .setSecure(true);
          
        Resources res = context.getResources();  
        // -- setup objects for EEG Launcher
        LauncherMusicPlayer = new SkyBody(res.getDrawable(R.drawable.icon_musicplayer), scale_obj);
        LauncherCamera = new SkyBody(res.getDrawable(R.drawable.icon_cam), scale_obj);
        LauncherDnaConsole = new SkyBody(res.getDrawable(R.drawable.icon_console), scale_obj);
        //LauncherGoogle = new SkyBody(res.getDrawable(R.drawable.icon_google), scale_obj);
        //LauncherMusicPlayerD = new SkyBody(res.getDrawable(R.drawable.icon_musicplayer), scale_obj);
        //LauncherCameraD = new SkyBody(res.getDrawable(R.drawable.icon_cam), scale_obj);
        //LauncherDnaConsoleD = new SkyBody(res.getDrawable(R.drawable.icon_console), scale_obj);
        //LauncherGoogleD = new SkyBody(res.getDrawable(R.drawable.icon_google), scale_obj);
        
        SelectedIcon = new SkyBody(res.getDrawable(R.drawable.icon_selected_icon), scale_obj);
        LeftBoarderIcon = new SkyBody(res.getDrawable(R.drawable.icon_boarder), scale_obj);
        RigthBoarderIcon = new SkyBody(res.getDrawable(R.drawable.icon_boarder), scale_obj);

                   
        // -- setup objects for MusicPlayer
        mp_Stop = new SkyBody(res.getDrawable(R.drawable.icon_stop_white), scale_obj); // image,scale
        mp_Play = new SkyBody(res.getDrawable(R.drawable.icon_play_white), scale_obj); 
        mp_Next = new SkyBody(res.getDrawable(R.drawable.icon_next_white), scale_obj); 
       // mp_StopD = new SkyBody(res.getDrawable(R.drawable.icon_stop), scale_obj); 
        //mp_PlayD = new SkyBody(res.getDrawable(R.drawable.icon_play), scale_obj);
        //mp_NextD = new SkyBody(res.getDrawable(R.drawable.icon_next), scale_obj);            
        //mp_Vol = new SkyBody(res.getDrawable(R.drawable.icon_volume_plus), scale_obj);
        //mp_VolD = new SkyBody(res.getDrawable(R.drawable.icon_volume_plus), scale_obj);
        //mp_Back = new SkyBody(res.getDrawable(R.drawable.icon_inf), scale_obj);
        //mp_BackD = new SkyBody(res.getDrawable(R.drawable.icon_inf), scale_obj);
          
      //  IconPlay = new SkyBody(res.getDrawable(R.drawable.icon_play), scale_obj); 
       // IconStop = new SkyBody(res.getDrawable(R.drawable.icon_stop), scale_obj);  
       // IconSkip = new SkyBody(res.getDrawable(R.drawable.icon_next), scale_obj);
       // IconBlack = new SkyBody(res.getDrawable(R.drawable.icon_black), scale_obj);
       // IconVol = new SkyBody(res.getDrawable(R.drawable.icon_volume_plus), scale_obj);
        
        // -- setup objects for DNA Console
        DnaConsole_T = new SkyBody(res.getDrawable(R.drawable.icon_t), scale_obj); // image,scale
      //  DnaConsole_rigth = new SkyBody(res.getDrawable(R.drawable.icon_rigth), scale_obj); // image,scale
        DnaConsole_C = new SkyBody(res.getDrawable(R.drawable.icon_c), scale_obj); // image,scale
       // DnaConsole_Back = new SkyBody(res.getDrawable(R.drawable.icon_inf), scale_obj); // image,scale
        DnaConsole_G = new SkyBody(res.getDrawable(R.drawable.icon_g), scale_obj);
        //DnaConsole_left = new SkyBody(res.getDrawable(R.drawable.icon_left), scale_obj);
        //DnaConsole_Cancel = new SkyBody(res.getDrawable(R.drawable.icon_submit1), scale_obj);
        DnaConsole_A = new SkyBody(res.getDrawable(R.drawable.icon_a), scale_obj);
          
     //   ObjectA = new SkyBody(res.getDrawable(R.drawable.icon_a), scale_obj_console); // image,scale
      //  ObjectC = new SkyBody(res.getDrawable(R.drawable.icon_c), scale_obj_console); // image,scale
      //  ObjectT = new SkyBody(res.getDrawable(R.drawable.icon_t), scale_obj_console); // image,scale
      //  ObjectG = new SkyBody(res.getDrawable(R.drawable.icon_g), scale_obj_console); // image,scale
          
       // ObjectSubmit = new SkyBody(res.getDrawable(R.drawable.black), scale_obj_console); // image,scale
        //ObjectCursor = new SkyBody(res.getDrawable(R.drawable.p_amber_1px), 5f); // image,scale
       // ObjectCursor = new SkyBody(res.getDrawable(R.drawable.icon_cursor), scale_obj_console*0.5f);
       // ObjectCursorDel = new SkyBody(res.getDrawable(R.drawable.p_black_10px), 0.8f); // image,scale
       // ObjectClear = new SkyBody(res.getDrawable(R.drawable.icon_black), scale_obj_console); // image,scale 
          
        // -- setup objects for Camera layout
        CameraPicture = new SkyBody(res.getDrawable(R.drawable.icon_picture), scale_obj);
       // CameraVideo = new SkyBody(res.getDrawable(R.drawable.icon_video), scale_obj);
        CameraPrtScr = new SkyBody(res.getDrawable(R.drawable.icon_printscreen), scale_obj);
        
          
        StarR = mp_Stop.getImageWidth()/2; // all stars has the same Radius
        StarR = StarR * scale_obj;  // adopt star size to screan using scale
        CursorR = CameraPicture.getImageWidth()/2f;           
        
        
        // cache handles to our key drawables
        BackGr_Image = context.getResources().getDrawable(R.drawable.blacksquare);
       
    }

    /** Starts the game, setting parameters for the current difficulty.  */
    public void doStart() {
        synchronized (mSurfaceHolder) {
            Level1_flag = 0;  Level2_flag = 0;  Level3_flag = 0; 
                                           
            // shuttle1
            // pick a convenient initial location for the lander sprite
            /* ===set random initial shuttle X coordinate=== */
            pY = BackGr_H/2;   
            pX = BackGr_W/2;
            CursorX = (float) pX - CursorR;
            CursorY_delta = BackGr_ImageScale*BackGr_W/2;
            CircleRadius = BackGr_ImageScale*BackGr_W / 2.7f;
            CursorX_delta = StarR*2f;
          //==========SkyBody===========
            //Sl1_0 = 0;  /*<-->*/ Sl1_1 = Cx_lb_l1 - StarR;  /*<-->*/ Sl1_2 = Cx_lb_l1 + StarR;
	                //Level1 - setup initial stars position/center coordinates and scale
            	// 1 left-bottom
	            Cx_lb_l1 = (float)(pX - CircleRadius) ;   // 720 -> 360
	            Cy_lb_l1 = (float)pY;  // 1280 -> 160(1/8); 320(1/4); 640(1/2); 
	           // mp_Stop.setCenterCoordinates(Cx_lb_l1, Cy_lb_l1);    mp_Stop.setScale(scale_obj);	                
	                
	            	// 2 right-top
	            Cx_rt_l1 = (float)(pX + CircleRadius);   // 720 -> 360
            Cy_rt_l1 = (float)pY;  // 1280 -> 160(1/8); 320(1/4); 640(1/2); 
           // mp_StopD.setCenterCoordinates(Cx_rt_l1, Cy_rt_l1); mp_StopD.setScale(scale_obj); mp_StopD.setAlpha(0f);
            
            	// 3 left-top
	            Cx_lt_l1 = (float)pX;   // 720 -> 360
	            Cy_lt_l1 = (float)(pY - CircleRadius);  // 1280 -> 160(1/8); 320(1/4); 640(1/2); 
            mp_Play.setCenterCoordinates(Cx_lt_l1, Cy_lt_l1); mp_Play.setScale(scale_obj); mp_Play.setAlpha(0f);	                
	                
	            	// 4 right-bottom
	            Cx_rb_l1 = (float)pX;   // 720 -> 360
            Cy_rb_l1 = (float)(pY + CircleRadius);  // 1280 -> 160(1/8); 320(1/4); 640(1/2); 
            
            float iconXc = (float)(pX - CircleRadius - 1.5f*StarR);  float iconYc = (float)(pY - CircleRadius);
           // IconPlay.setCenterCoordinates(iconXc, iconYc); // IconStop.setCenterCoordinates(iconXc, iconYc); 
            //IconSkip.setCenterCoordinates(iconXc, iconYc); 
            //IconBlack.setCenterCoordinates(iconXc, iconYc);
            //IconVol.setCenterCoordinates(iconXc, iconYc);
            
            //ObjectG.setCenterCoordinates(CursorX, (float)pY*2f); ObjectG.setScale(scale_obj_console);
            //mp_Next.setCenterCoordinates(Cx_rb_l1, Cy_rb_l1); mp_Next.setScale(scale_obj); mp_Next.setAlpha(0f);
            
           // ObjectCursor.setCenterCoordinates((float)(4*pX), (float)pY );
           // ObjectCursorDel.setCenterCoordinates((float)(4*pX), (float)pY );
            
            mLastTime = System.currentTimeMillis() + 100;
            setState(STATE_RUNNING);
        }
    }

    /** Pauses the physics update & animation.   */
    public void pause() {
        synchronized (mSurfaceHolder) {
            if (mMode == STATE_RUNNING) setState(STATE_PAUSE);
        }
    }

    /**
     * Restores game state from the indicated Bundle. Typically called when
     * the Activity is being restored after having been previously
     * destroyed.
     *
     * @param savedState Bundle containing the game state
     */
    public synchronized void restoreState(Bundle savedState) {
        synchronized (mSurfaceHolder) {
            setState(STATE_PAUSE);
           // mRotating = 0;
          /*  pX = savedState.getDouble(KEY_X);
            pY = savedState.getDouble(KEY_Y);
            EegLauncherFlag = savedState.getBoolean(KEY_EEG);
            DnaConsoleFlag = savedState.getBoolean(KEY_DNA);
            MusicPlayerFlag = savedState.getBoolean(KEY_MP);*/
        }
    }

    @Override
    public void run() {
        while (mRun) {
            Canvas c = null;
            try {
                c = mSurfaceHolder.lockCanvas(null);
                synchronized (mSurfaceHolder) {
                    if (mMode == STATE_RUNNING) updatePhysics();
                    doDraw(c);
                }
            } finally {
                // do this in a finally so that if an exception is thrown
                // during the above, we don't leave the Surface in an
                // inconsistent state
                if (c != null) {
                    mSurfaceHolder.unlockCanvasAndPost(c);
                    
                }
            }
        }
    }

    /**
     * Dump game state to the provided Bundle. Typically called when the
     * Activity is being suspended.
     *
     * @return Bundle with this view's state
     */
    public Bundle saveState(Bundle map) {
        synchronized (mSurfaceHolder) {
            if (map != null) {
            /*    map.putDouble(KEY_X, Double.valueOf(pX));
                map.putDouble(KEY_Y, Double.valueOf(pY));
                map.putBoolean(KEY_EEG, Boolean.valueOf(EegLauncherFlag));
                map.putBoolean(KEY_DNA, Boolean.valueOf(DnaConsoleFlag));
                map.putBoolean(KEY_MP , Boolean.valueOf(MusicPlayerFlag));*/
            }
        }
        return map;
    }
    
    /** Set current Attention.   */
    public void setAttention(int Attention) {
        synchronized (mSurfaceHolder) {
            At = Attention;
        }
    }
    
    /** Set current Meditation.  */
    public void setMeditation(int Meditation) {
        synchronized (mSurfaceHolder) {
            Med = Meditation;
        }
    }

    /** Set current Genome Letter in binary format   */
    public void setGenome(int G1, int G2, String GL) {
        synchronized (mSurfaceHolder) {
        	GenBinLet1 = G1;
        	GenBinLet2 = G2;
        	GenLetSucc = GL;
        }
    }
    
    /** Set current Meditation.  */
    public void setGameMode(String GM) {
        synchronized (mSurfaceHolder) {
            GameMode = GM;
            //flag = String.valueOf(GameMode);
        }
    }
    
    /** Set TG status.  */
    public void setTGStatus(CharSequence TGStatus_l) {
        synchronized (mSurfaceHolder) {
        	TGStatus = TGStatus_l;
        }
    }

    /**
     * Used to signal the thread whether it should be running or not.
     * Passing true allows the thread to run; passing false will shut it
     * down if it's already running. Calling start() after this was most
     * recently called with false will result in an immediate shutdown.
     *
     * @param b true to run, false to shut down
     */
    public void setRunning(boolean b) {
        mRun = b;
    }

    /**
     * Sets the game mode. That is, whether we are running, paused etc.
     *
     * @see #setState(int, CharSequence)
     * @param mode one of the STATE_* constants
     */
    public void setState(int mode) {
        synchronized (mSurfaceHolder) {
            setState(mode, null);
        }
    }

    /**
     * Sets the game mode. That is, whether we are running, paused, in the
     * failure state, in the victory state, etc.
     *
     * @param mode one of the STATE_* constants
     * @param message string to add to screen or null
     */
    public void setState(int mode, CharSequence message) {
        /*
         * This method optionally can cause a text message to be displayed
         * to the user when the mode changes. Since the View that actually
         * renders that text is part of the main View hierarchy and not
         * owned by this thread, we can't touch the state of that View.
         * Instead we use a Message + Handler to relay commands to the main
         * thread, which updates the user-text View.
         */
        synchronized (mSurfaceHolder) {           	
            mMode = mode;
            CharSequence str ="";
            if (mMode == STATE_RUNNING) {
            	str =  "";
            	//+ "CursorI = "+ delta + "  \n "
            	//+ "CursorI = "+ String.valueOf(CursorI) + "  \n ";
            	
            } else {
                Resources res = mContext.getResources();      str = "";
                
                if (message != null) {str = message + "\n" + str; }
              }
                Message msg = mHandler.obtainMessage();
                Bundle b = new Bundle();
                b.putString("text", str.toString());
                b.putInt("viz", View.VISIBLE);
                msg.setData(b);
                mHandler.sendMessage(msg);
            //}
        }
    }

    /* Callback invoked when the surface dimensions change. */
    public void setSurfaceSize(int width, int height) {
        // synchronized to make sure these all change atomically
        synchronized (mSurfaceHolder) {
            BackGr_W = width;
            BackGr_H = height;

            // don't forget to resize the background image
           // mBackgroundImage = Bitmap.createScaledBitmap(mBackgroundImage, width, height, true);
        }
    }

    /** Resumes from a pause.    */
    public void unpause() {
        // Move the real time clock up to now
        synchronized (mSurfaceHolder) {
            mLastTime = System.currentTimeMillis() + 100;
        }
        setState(STATE_RUNNING);
    }


    /**
     * Draws the shuttle, space bodies and background to the provided Canvas.
     */
    private void doDraw(Canvas canvas) {
        // Draw the background image. Operations on the Canvas accumulate so this is like clearing the screen.
                 
        //moving one image on background
        canvas.save();
        canvas.scale(BackGr_ImageScale,BackGr_ImageScale, (float)BackGr_W/2 , (float)BackGr_H/2); // scale
        BackGr_Image.setBounds(0, (int)(BackGr_H/2f - BackGr_W/2), BackGr_W, (int)(BackGr_H/2f + BackGr_W/2));
        BackGr_Image.draw(canvas);        
        canvas.restore();   
                
        LeftBoarderIcon.drawTo(canvas);  RigthBoarderIcon.drawTo(canvas); 
        
        if (EegLauncherFlag == true){
        	LauncherMusicPlayer.drawTo(canvas); LauncherDnaConsole.drawTo(canvas); 
        	LauncherCamera.drawTo(canvas); //LauncherGoogle.drawTo(canvas);
        //	LauncherMusicPlayerD.drawTo(canvas); LauncherDnaConsoleD.drawTo(canvas); LauncherCameraD.drawTo(canvas); LauncherGoogleD.drawTo(canvas);
        }
        	
        if (MusicPlayerFlag == true){
        	if (play_flag == false){ mp_Play.drawTo(canvas); }
        	if (play_flag == true) { mp_Stop.drawTo(canvas); }
	            mp_Next.drawTo(canvas); //mp_NextD.drawTo(canvas);
	           // mp_Vol.drawTo(canvas);  mp_VolD.drawTo(canvas);  mp_Back.drawTo(canvas); mp_BackD.drawTo(canvas);
	            
        }
     
        if (DnaConsoleFlag == true){
	            DnaConsole_T.drawTo(canvas); //DnaConsole_rigth.drawTo(canvas); 
	            DnaConsole_C.drawTo(canvas); //DnaConsole_Back.drawTo(canvas);
	            DnaConsole_G.drawTo(canvas); //DnaConsole_left.drawTo(canvas); 
	            DnaConsole_A.drawTo(canvas); // DnaConsole_Cancel.drawTo(canvas);
	         
	            //ObjectCursor.drawTo(canvas);
	                        
        }
        
        if (CameraFlag == true){
        	CameraPrtScr.drawTo(canvas); CameraPicture.drawTo(canvas); 
        	//CameraVideo.drawTo(canvas); 
        }
        
        SelectedIcon.drawTo(canvas);             
        
        
}

    /**
     * Figures the objects state (x, y, ...) based on the passage of
     * realtime. Does not invalidate(). Called at the start of draw().
     * Detects the end-of-game and sets the UI to the next state.
     */
    private void updatePhysics() {
        long now = System.currentTimeMillis();

        // -- Do nothing if mLastTime is in the future.
        // -- This allows the game-start to delay the start of the physics by 100ms or whatever.
        if (mLastTime > now) return;
        // -- double elapsed = (now - mLastTime) / 1000.0;
        //elapsed = (now - mLastTime) / 1000.0;
        elapsed = (now - mLastTime) / 1000.0;
        
        /* -- =========== -- */
        ApM = At+Med;                       
        AmM = At-Med;
        	// 1s: 10 * 0.015 = 0.15 
       /* if (accel_alpha>=2.5f) {accel_alpha = 2.5f; scale_obj = scale_obj_rot; }  // -- limit rotational speed
        if (accel_alpha>=2f && accel_alpha<=2.5f) {scale_obj = scale_obj_rot; }
        if (accel_alpha>=1.5f && accel_alpha<2.0f) {scale_obj = scale_obj_rot; }
        if (accel_alpha>=1f && accel_alpha<1.5f) {scale_obj = scale_obj_rot; }          
        if (accel_alpha<=0f){ accel_alpha = 0f; scale_obj = scale_obj_rot;  }*/
        
        // -- limit rotational speed
        if (accel_alpha>=1.5f) {accel_alpha = 1.5f; scale_obj = scale_obj_rot; }          
        if (accel_alpha<=0f)	 {accel_alpha = 0f; scale_obj = scale_obj_rot;  }
        
        	// -- time to action selection
       /* if (accel_alpha<=0f && CursorI != 0){ TimeToSelect = TimeToSelect - 0.015f; action_cancel_flag = false;}
        	else {
        		 if (TimeToSelect>0f && TimeToSelect<=2f && accel_alpha>-5f && accel_alpha<=0.75f && accel_alpha!=0f)
        		// if (TimeToSelect>0f && TimeToSelect<=2f && accel_alpha>0f && accel_alpha<=0.75f )
        			{ action_cancel_flag = true;}	
        		  TimeToSelect = 3f;
        		 }*/
        // -- only for glass
        if (accel_alpha<=0f && CursorI != 0){ TimeToSelect = TimeToSelect - 0.03f; action_cancel_flag = false;}
      	else {
      		 if (TimeToSelect>0f && TimeToSelect<=2f && accel_alpha>-5f && accel_alpha<=0.75f && accel_alpha!=0f)
      		// if (TimeToSelect>0f && TimeToSelect<=2f && accel_alpha>0f && accel_alpha<=0.75f )
      			{ action_cancel_flag = true;}	
      		  TimeToSelect = 3f;
      		 }
        
        if (TimeToSelect == 3f && accel_alpha>0.75f){ action_cancel_flag = false;}	
       
        if (TimeToSelect<=0f){ TimeToSelect = 0f;}
        
        //ClusterLeftX = -30; ClusterCenterX = 0; ClusterRightX = 30; ClusterDelta = 0;
        	// -- update accel_alpha and alpha
        if (At-Med >= ClusterLeftX-ClusterDelta &&
        	At-Med <= ClusterRightX+ClusterDelta &&
        	accel_alpha > 0) 
        				{ accel_alpha = accel_alpha - 0.35f*(float)elapsed ; alpha = alpha + accel_alpha; }
        else {
            if (At-Med > ClusterRightX+ClusterDelta )
            			{ accel_alpha = accel_alpha + 0.35f*(float)elapsed ; alpha = alpha + accel_alpha; } 
            else {
            	if (At-Med < ClusterLeftX-ClusterDelta )
            			{ accel_alpha = accel_alpha + 0.35f*(float)elapsed ; alpha = alpha + accel_alpha; }
            }                    
        }       
        if (alpha >=360) {alpha = alpha-360; }
            // -- reset flag if acceleration increase above 1 
        if (accel_alpha>1f && CursorI<=console_length) {flag_Cursor=true; }
        	
        if (CursorI == 0 && accel_alpha>0.5f) {CursorI = 1; }                    
        
        SelectedIcon.setScale(0f); 
        
        if (EegLauncherFlag == true) {EEG_launcher_N = 3; }
        if (MusicPlayerFlag == true) {EEG_launcher_N = 2; }
        if (DnaConsoleFlag == true)  {EEG_launcher_N = 4; }
        if (CameraFlag == true) 	 {EEG_launcher_N = 2; }
        delta = 360/EEG_launcher_N;
        curr_alpha_obj1 = alpha; curr_alpha_obj2 = alpha + delta; 
        curr_alpha_obj3 = alpha + 2*delta; curr_alpha_obj4 = alpha + 3*delta;

        
        if (curr_alpha_obj1 > 360) {curr_alpha_obj1=curr_alpha_obj1 - 360;}
        if (curr_alpha_obj2 > 360) {curr_alpha_obj2=curr_alpha_obj2 - 360;}
        if (curr_alpha_obj3 > 360) {curr_alpha_obj3=curr_alpha_obj3 - 360;}
        if (curr_alpha_obj4 > 360) {curr_alpha_obj4=curr_alpha_obj4 - 360;}
        
        LeftBoarderIcon.updatePhysics(delta/2, 360 - delta/2, CircleRadius, pX, pY);
        RigthBoarderIcon.updatePhysics(-delta/2, delta/2, CircleRadius, pX, pY);
        
        /* -- ======EEG Launcher===== -- */
        if (EegLauncherFlag == true){
        	consoleBoard = " ";          
	            float[] alpha_rot = new float[] {0.0f, 0.0f, 0.8f, 0.8f, 1f, 0.9f };  Random rs1 =new Random();
	            LauncherMusicPlayer.updatePhysics(alpha_rot[rs1.nextInt(2)], curr_alpha_obj2, CircleRadius, pX, pY);
	            LauncherMusicPlayer.setScale(scale_obj);          
	            LauncherDnaConsole.updatePhysics(alpha_rot[rs1.nextInt(1)], curr_alpha_obj1, CircleRadius, pX, pY);
	            LauncherDnaConsole.setScale(scale_obj);     
	            LauncherCamera.updatePhysics(alpha_rot[rs1.nextInt(2)], curr_alpha_obj3, CircleRadius, pX, pY);  
	            LauncherCamera.setScale(scale_obj); 
	            
	            // -- set coordinates of Icon SelectedIcon  
	            if (accel_alpha <= 0 && flag_Cursor == true && TimeToSelect<3){ 
	            	SelectedIcon.setScale(scale_obj*1.4f);
	            	
	            	// -- Console
	            	if (curr_alpha_obj1 > 360 - delta/2  ||  curr_alpha_obj1 <= delta/2){ 
	            		SelectedIcon.updatePhysics(alpha_rot[rs1.nextInt(2)], curr_alpha_obj1, CircleRadius, pX, pY); 
	            	}            		            	
	            	// -- MusicPlayer
	            	if (curr_alpha_obj2 > 360 - delta/2  ||  curr_alpha_obj2 <= delta/2){
	            		SelectedIcon.updatePhysics(alpha_rot[rs1.nextInt(2)], curr_alpha_obj2, CircleRadius, pX, pY); 
	            	}           	            	
	            	// -- Camera
	            	if (curr_alpha_obj3 > 360 - delta/2  ||  curr_alpha_obj3 <= delta/2){
	            		SelectedIcon.updatePhysics(alpha_rot[rs1.nextInt(2)], curr_alpha_obj3, CircleRadius, pX, pY); 
		           	}                   	
	
	            }
	                             
	            // -- manage what was selected  
	            if (accel_alpha <= 0 && flag_Cursor == true && TimeToSelect<=0){ 
	            	
	            	// -- Console
	            	if (curr_alpha_obj1 > 360 - delta/2  ||  curr_alpha_obj1 <= delta/2){ 
	            		EegLauncherFlag = false; DnaConsoleFlag = true;		
	            		msgBoard = "console";
	            	} 
	            		            	
	            	// -- MusicPlayer
	            	if (curr_alpha_obj2 > 360 - delta/2  ||  curr_alpha_obj2 <= delta/2){
	            		MusicPlayerFlag = true; EegLauncherFlag = false;
	            		msgBoard = "music";
	            	}	            	
	            	
	            	// -- Camera
	            	if (curr_alpha_obj3 > 360 - delta/2  ||  curr_alpha_obj3 <= delta/2){
	            		CameraFlag = true; EegLauncherFlag = false;
	            		msgBoard = "camera";
		           	}         
	            	                	
	            	flag_Cursor = false;     
	            	
	            	// -- for testin only
	            	//TwitPicture_flag = true;
	            	//Prtscr_flag = true;
	            	//MusicPlayerFlag = false; EegLauncherFlag = false; DnaConsoleFlag = true;	
	            	//CameraFlag = true; EegLauncherFlag = false;
	            }
        }
        /* -- ======END EEG Launcher===== -- */

        
        /* -- ======MusicPlayer===== -- */
        if (MusicPlayerFlag == true){
        	consoleBoard = " "; 
	            float[] alpha_rot = new float[] {0.0f, 0.0f, 0.8f, 0.8f, 1f, 0.9f };  Random rs1 =new Random();
	            if (play_flag == true){
	            	mp_Stop.updatePhysics(alpha_rot[rs1.nextInt(2)], curr_alpha_obj1, CircleRadius, pX, pY); 
	            	mp_Stop.setScale(scale_obj);          
	            }
	            if (play_flag == false){
	            	mp_Play.updatePhysics(alpha_rot[rs1.nextInt(1)], curr_alpha_obj1, CircleRadius, pX, pY); 
	            	mp_Play.setScale(scale_obj);     
	            }
	            mp_Next.updatePhysics(alpha_rot[rs1.nextInt(2)], curr_alpha_obj2, CircleRadius, pX, pY); 
	            mp_Next.setScale(scale_obj);                       
           
	            // -- set coordinates of Icon SelectedIcon  
	            if (accel_alpha <= 0 && flag_Cursor == true && TimeToSelect<3){ 
	            	SelectedIcon.setScale(scale_obj*1.4f);
	            	
	            	// -- play / stop
	            	if (curr_alpha_obj1 > 360 - delta/2  ||  curr_alpha_obj1 <= delta/2){ 
	            		SelectedIcon.updatePhysics(alpha_rot[rs1.nextInt(2)], curr_alpha_obj1, CircleRadius, pX, pY); 
	            	}            		            	
	            	// -- play next
	            	if (curr_alpha_obj2 > 360 - delta/2  ||  curr_alpha_obj2 <= delta/2){
	            		SelectedIcon.updatePhysics(alpha_rot[rs1.nextInt(2)], curr_alpha_obj2, CircleRadius, pX, pY); 
	            	}           	            	                  	
	
	            }
	                             
	            // -- manage what was selected  
	            if (accel_alpha <= 0 && flag_Cursor == true && TimeToSelect<=0){            	
       	
	            	// -- play / stop
	            	if (curr_alpha_obj1 > 360 - delta/2  ||  curr_alpha_obj1 <= delta/2){ 
	            		if (play_flag == false)	{ play_flag = true; stop_flag = false; sel_action_i = 1; msgBoard = "play";}
	            		else          			{ play_flag = false; stop_flag = true; sel_action_i = 2; msgBoard = "stop";}	            		
	            	} 
	            		            	
	            	// -- play next
	            	if (curr_alpha_obj2 > 360 - delta/2  ||  curr_alpha_obj2 <= delta/2){
	            		next_flag = true; play_flag = true; stop_flag = false; sel_action_i = 3; 	msgBoard = "play next";
	            	}	            	
	            	                  	            			            	
	            	flag_Cursor = false;            	
	
        	//	MusicPlayerFlag = false; EegLauncherFlag = true;
        		MusicPlayerFlag = true;
	            }
        }
        /* -- ======END MusicPlayer===== -- */          
              
        

        /* -- ======DNA Console===== -- */
        if (DnaConsoleFlag == true){
        	consoleBoard = " "; 
	            float[] alpha_rot = new float[] {0.0f, 0.0f, 0.8f, 0.8f, 1f, 0.9f }; Random rs1 =new Random();
	            DnaConsole_T.updatePhysics(alpha_rot[rs1.nextInt(2)], curr_alpha_obj3, CircleRadius, pX, pY);       
	            DnaConsole_C.updatePhysics(alpha_rot[rs1.nextInt(2)], curr_alpha_obj2, CircleRadius, pX, pY);    
	            DnaConsole_G.updatePhysics(alpha_rot[rs1.nextInt(1)], curr_alpha_obj4, CircleRadius, pX, pY);          
	            DnaConsole_A.updatePhysics(alpha_rot[rs1.nextInt(1)], curr_alpha_obj1, CircleRadius, pX, pY);
	            	            
	           /* DnaConsole_T.setScale(scale_obj); DnaConsole_rigth.setScale(scale_obj);
	            DnaConsole_C.setScale(scale_obj); DnaConsole_Back.setScale(scale_obj); 		
	            DnaConsole_G.setScale(scale_obj); DnaConsole_left.setScale(scale_obj);
	            DnaConsole_Cancel.setScale(scale_obj); DnaConsole_A.setScale(scale_obj);*/
	            
	            // -- set coordinates of Icon SelectedIcon  
	            if (accel_alpha <= 0 && flag_Cursor == true && TimeToSelect<3){ 
	            	SelectedIcon.setScale(scale_obj*1.4f);
	            	
	            	// -- A
	            	if (curr_alpha_obj1 > 360 - delta/2  ||  curr_alpha_obj1 <= delta/2){ 
	            		SelectedIcon.updatePhysics(alpha_rot[rs1.nextInt(2)], curr_alpha_obj1, CircleRadius, pX, pY); 
	            	}            		            	
	            	// -- C
	            	if (curr_alpha_obj2 > 360 - delta/2  ||  curr_alpha_obj2 <= delta/2){
	            		SelectedIcon.updatePhysics(alpha_rot[rs1.nextInt(2)], curr_alpha_obj2, CircleRadius, pX, pY); 
	            	}           	            	
	            	// -- T
	            	if (curr_alpha_obj3 > 360 - delta/2  ||  curr_alpha_obj3 <= delta/2){
	            		SelectedIcon.updatePhysics(alpha_rot[rs1.nextInt(2)], curr_alpha_obj3, CircleRadius, pX, pY); 
		           	} 
	            	// -- G
	            	if (curr_alpha_obj4 > 360 - delta/2  ||  curr_alpha_obj4 <= delta/2){
	            		SelectedIcon.updatePhysics(alpha_rot[rs1.nextInt(2)], curr_alpha_obj4, CircleRadius, pX, pY); 
		           	} 
	
	            }
	            
	            // -- manage what was selected 
	            CursorX_delta = CursorR; //float scale_k = 1; 
	            CursorY = (float) (pY - CircleRadius/2f + CursorR/2f);
	            	             
	            if (accel_alpha <= 0 && flag_Cursor == true && TimeToSelect<=0){ 
	            	//ObjectCursorDel.updateDNA(CursorX, pY-CursorY_delta-2*CursorR);  // -- delete old cursor position
	            	
	            	// -- A
	            	if (curr_alpha_obj1 > 360 - delta/2  ||  curr_alpha_obj1 <= delta/2){ 
	            		//EegLauncherFlag = false; DnaConsoleFlag = true;		
	            		if(CursorI!=console_length){
		            		sel_action_i = 4; flag_Cursor = false; //ObjectA.updateDNA(CursorX, CursorY);
		            		console_str[CursorI-1] = "A"; 
		            		CursorX=CursorX+CursorX_delta; CursorI=CursorI+1;
		            	}
	            		msgBoard = "A"; 
	            	} 
	            		            	
	            	// -- C
	            	if (curr_alpha_obj2 > 360 - delta/2  ||  curr_alpha_obj2 <= delta/2){
	            		if(CursorI!=console_length){
		            		sel_action_i = 3; flag_Cursor = false; //ObjectC.updateDNA(CursorX, CursorY);
		            		console_str[CursorI-1] = "C";
		            		CursorX=CursorX+CursorX_delta; CursorI=CursorI+1;
	            		}
	            		ClearConsole();
	            		/*msgBoard = "C";
	            		console_str[0] = "-"; console_str[1] = "-"; console_str[2] = "-"; console_str[3] = "-";
	            		CursorI = 1;*/
	            	}	            	
	            	
	            	// -- T
	            	if (curr_alpha_obj3 > 360 - delta/2  ||  curr_alpha_obj3 <= delta/2){
	            		if(CursorI!=console_length){
		            		sel_action_i = 1;  flag_Cursor = false;// ObjectT.updateDNA(CursorX, CursorY);
		            		console_str[CursorI-1] = "T";
		            		CursorX=CursorX+CursorX_delta; CursorI=CursorI+1;
	            		}
	            		msgBoard = "T";
		           	}  
	            	
	            	// -- G
	            	if (curr_alpha_obj4 > 360 - delta/2  ||  curr_alpha_obj4 <= delta/2){
	            		if(CursorI!=console_length){
		            		sel_action_i = 2; flag_Cursor = false; //ObjectG.updateDNA(CursorX, CursorY);
		            		console_str[CursorI-1] = "G";
		            		CursorX=CursorX+CursorX_delta; CursorI=CursorI+1;
	            		}
	            		msgBoard = "G";
		           	}  
	            	
	            	ConsoleWhatComand();
	            	
        		if(CursorI == 4){
        			console_str_action = console_str[0] + console_str[1] + console_str[2];
	             		console_str[0] = "-"; console_str[1] = "-"; console_str[2] = "-"; console_str[3] = "-";
	             		
        			msgBoard = console_str_action;
        			//EegLauncherFlag = true; DnaConsoleFlag = false; 
        			CursorI = 1;
        		}
	            	  
        		consoleLine = console_str[0] + "  " + console_str[1] + "  " + console_str[3];
        		
	            	flag_Cursor = false;      
	            	//MusicPlayerFlag = true; EegLauncherFlag = false; DnaConsoleFlag = false;	
	            }
	            	
        }
        /* -- ======END DNA Console===== -- */
        
        /* -- ======Camera===== -- */
        if (CameraFlag == true){          
	            float[] alpha_rot = new float[] {0.0f, 0.0f, 0.8f, 0.8f, 1f, 0.9f };  Random rs1 =new Random();
	            CameraPicture.updatePhysics(alpha_rot[rs1.nextInt(2)], curr_alpha_obj1, CircleRadius, pX, pY); CameraPicture.setScale(scale_obj);             
	            CameraPrtScr.updatePhysics(alpha_rot[rs1.nextInt(2)], curr_alpha_obj2, CircleRadius, pX, pY); CameraPrtScr.setScale(scale_obj); 
	         // CameraVideo.updatePhysics(alpha_rot[rs1.nextInt(1)], curr_alpha_obj3, CircleRadius, pX, pY);  CameraVideo.setScale(scale_obj);  
	            
	            // -- set coordinates of Icon SelectedIcon  
	            if (accel_alpha <= 0 && flag_Cursor == true && TimeToSelect<3){ 
	            	SelectedIcon.setScale(scale_obj*1.4f);
	            	
	            	// -- Picture
	            	if (curr_alpha_obj1 > 360 - delta/2  ||  curr_alpha_obj1 <= delta/2){ 
	            		SelectedIcon.updatePhysics(alpha_rot[rs1.nextInt(2)], curr_alpha_obj1, CircleRadius, pX, pY); 
	            	}            		            	
    	            	
	            	// -- PrintScreen
	            	if (curr_alpha_obj2 > 360 - delta/2  ||  curr_alpha_obj2 <= delta/2){
	            		SelectedIcon.updatePhysics(alpha_rot[rs1.nextInt(2)], curr_alpha_obj2, CircleRadius, pX, pY); 
		           	}         
	            	
	            	// -- video
	            	//if (curr_alpha_obj3 > 360 - delta/2  ||  curr_alpha_obj3 <= delta/2){
	            	//	SelectedIcon.updatePhysics(alpha_rot[rs1.nextInt(2)], curr_alpha_obj3, CircleRadius, pX, pY); 
	            	//}   
	
	            }
	                             
	            // -- manage what was selected  
	            if (accel_alpha <= 0 && flag_Cursor == true && TimeToSelect<=0){ 
	            	// double elapsed_cam = 0.0;
	            	// msgBoard = String.valueOf(elapsed_cam);
	            	// -- picture
	            	if (curr_alpha_obj1 > 360 - delta/2  ||  curr_alpha_obj1 <= delta/2){ 	            			
	            		/*msgBoard = "focus camera";
	            		do {
	            			msgBoard = String.valueOf(elapsed_cam);
	            			elapsed_cam = (now - mLastTime) / 1000.0;
	            		} while (elapsed_cam<0.03);*/
	            		Picture_flag = true;
	            		msgBoard = "picture was taken";
	            	} 
	            		            	
	            	// -- video//twitter
	            //	if (curr_alpha_obj3 > 360 - delta/2  ||  curr_alpha_obj3 <= delta/2){
	            //		TwitPicture_flag = true;
	            //		msgBoard = "sharing in dev";
	            //	}	            	
	            	
	            	// -- PrtScr
	            	if (curr_alpha_obj2 > 360 - delta/2  ||  curr_alpha_obj2 <= delta/2){
	            		Prtscr_flag = true;
	            		msgBoard = "PrtSc";
		           	}                     	   
	            	
	            	flag_Cursor = false;     
	            	
	            	//EegLauncherFlag = true; CameraFlag = false;	
	            	CameraFlag = true;	
	
	            	// -- for testing only
	            	//Picture_flag = true;
	            }
        }
        /* -- ======END Camera===== -- */

        
        setState(STATE_RUNNING);  //set game status and update and print message
        
        mLastTime = now;
        
    }


    private void ConsoleWhatComand() {
    	// --first letter A
    	//if (console_str[0] == "A" || console_str[0] == "C" || console_str[0] == "T" || console_str[0] == "G")
    	if (console_str[0] == "A")
    		{ 	consoleBoard = "A: BCI settings \n AAA-AAT/ATA-ATT \n central cluster +10/-10"; 
    	     	
    			if (console_str[1] == "A")	{
    	     		consoleBoard = "AAA/AAT central cluster +10\nAAG/ATG ClusterDelta = 0"; 
    	     		if (console_str[2] == "A") {ClearConsole(); ClusterDelta = 5;
    	     			consoleBoard = "AAA submitted\n central cluster +10"; }
    	     		if (console_str[2] == "T") {ClearConsole(); ClusterDelta = 5;
    	     			consoleBoard = "AAT submitted\n central cluster +10"; }
    	     		if (console_str[2] == "C") { consoleBoard = "AAC: clear"; }
    	     		if (console_str[2] == "G") {ClearConsole(); ClusterDelta = 0;
    	     			consoleBoard = "AAG: submitted\nClusterDelta = 0"; }
    	     		}
    	     	
    	     	if (console_str[1] == "T")	{ 
    	     		consoleBoard = "ATA/ATT central cluster -10\nAAG/ATG ClusterDelta = 0"; 
    	     		if (console_str[2] == "A"){ClearConsole();ClusterDelta = -5;
    	     			consoleBoard = "ATA submitted\n central cluster -10";}
	     			if (console_str[2] == "T"){ClearConsole(); ClusterDelta = -5;
	     				consoleBoard = "ATT submitted\n central cluster -10"; }
	     			if (console_str[2] == "C")	{ consoleBoard = "ATC: clear"; }
	    	     		if (console_str[2] == "G")	{ClearConsole(); ClusterDelta = 0;
	    	     			consoleBoard = "ATG: submitted\nClusterDelta = 0"; }
	    	     		}
    	     	
    	     	if (console_str[1] == "C")	{ consoleBoard = "AC: clear"; }

    			if (console_str[1] == "G")	{
    	     		consoleBoard = "AG*: zoom out"; 
    	     		if (console_str[2] == "A") {consoleBoard = "AGA: zoom out";
    	     				EegLauncherFlag = true; DnaConsoleFlag = false;
    	     			}
    	     		if (console_str[2] == "T") { consoleBoard = "AGT: zoom out";
    	     				EegLauncherFlag = true; DnaConsoleFlag = false;
    	     			}
    	     		if (console_str[2] == "C") { consoleBoard = "AGC: clear"; 
    	     			}
    	     		if (console_str[2] == "G") { consoleBoard = "AGA: zoom out";
    	     				EegLauncherFlag = true; DnaConsoleFlag = false;
    	     			}
    	     		}
    		}
    	
    	// --first letter C
    	//if (console_str[0] == "C" || console_str[1] == "C" || console_str[2] == "C"){} //realized in updatePhysics clearConsole()
    	

    	// --first letter T
    	if (console_str[0] == "T")
		{ 	consoleBoard = "T**: zoom out"; 
	     	
			if (console_str[1] == "A")	{
	     		consoleBoard = "TA*: zoom out"; 
	     		EegLauncherFlag = true; DnaConsoleFlag = false; // -- zoom out
	     		ClearAndHideConsole();
	     		/*if (console_str[2] == "A") {consoleBoard = "TAA: zoom out";
	     				EegLauncherFlag = true; DnaConsoleFlag = false;
	     			}
	     		if (console_str[2] == "T") { consoleBoard = "TAT: zoom out";
	     				EegLauncherFlag = true; DnaConsoleFlag = false;	
	     			}
	     		if (console_str[2] == "C") { consoleBoard = "TAC: clear"; 
	     			}
	     		if (console_str[2] == "G") { consoleBoard = "TAA: zoom out";
	     				EegLauncherFlag = true; DnaConsoleFlag = false;
	     			}*/
	     		}
	     	
			if (console_str[1] == "T")	{
	     		consoleBoard = "TT*: zoom out"; 
	     		EegLauncherFlag = true; DnaConsoleFlag = false; // -- zoom out
	     		ClearAndHideConsole();
	     		/*if (console_str[2] == "A") {consoleBoard = "TTA: zoom out";
	     				EegLauncherFlag = true; DnaConsoleFlag = false;	
	     			}
	     		if (console_str[2] == "T") { consoleBoard = "TTT: zoom out";
	     				EegLauncherFlag = true; DnaConsoleFlag = false;
	     			}
	     		if (console_str[2] == "C") { consoleBoard = "TTC: clear"; 
	     			}
	     		if (console_str[2] == "G") { consoleBoard = "TTA: zoom out";
	     				EegLauncherFlag = true; DnaConsoleFlag = false;
	     			}*/
	     		}
	     	
	     	if (console_str[1] == "C")	{ consoleBoard = "TC: clear"; 
	     		}

			if (console_str[1] == "G")	{
	     		consoleBoard = "TG*: zoom out"; 
	     		EegLauncherFlag = true; DnaConsoleFlag = false; // -- zoom out
	     		ClearAndHideConsole();
	     		/*if (console_str[2] == "A") {consoleBoard = "TGA: zoom out";
	     				EegLauncherFlag = true; DnaConsoleFlag = false;
	     			}
	     		if (console_str[2] == "T") { consoleBoard = "TGT: zoom out";
	     				EegLauncherFlag = true; DnaConsoleFlag = false;
	     			}
	     		if (console_str[2] == "C") { consoleBoard = "TGC: clear"; 
	     			}
	     		if (console_str[2] == "G") { consoleBoard = "TGA: zoom out";
	     				EegLauncherFlag = true; DnaConsoleFlag = false;
	     			}*/
	     		}
			}
    	
    	
    	// --first letter G
    	if (console_str[0] == "G")
		{ 	consoleBoard = "G**: zoom out"; 
	     	
			if (console_str[1] == "A")	{
	     		consoleBoard = "GA*: zoom out"; 
	     		EegLauncherFlag = true; DnaConsoleFlag = false; // -- zoom out
	     		ClearAndHideConsole();
	     		/*if (console_str[2] == "A") {consoleBoard = "GAA: zoom out";
	     				EegLauncherFlag = true; DnaConsoleFlag = false;
	     			}
	     		if (console_str[2] == "T") { consoleBoard = "GAT: zoom out";
	     				EegLauncherFlag = true; DnaConsoleFlag = false;	
	     			}
	     		if (console_str[2] == "C") { consoleBoard = "GAC: clear"; 
	     			}
	     		if (console_str[2] == "G") { consoleBoard = "GAA: zoom out";
	     				EegLauncherFlag = true; DnaConsoleFlag = false;
	     			}*/
	     		}
	     	
			if (console_str[1] == "T")	{
	     		consoleBoard = "GT*: zoom out"; 
	     		EegLauncherFlag = true; DnaConsoleFlag = false; // -- zoom out
	     		ClearAndHideConsole();
	     		/*if (console_str[2] == "A") {consoleBoard = "GTA: zoom out";
	     				EegLauncherFlag = true; DnaConsoleFlag = false;	
	     			}
	     		if (console_str[2] == "T") { consoleBoard = "GTT: zoom out";
	     				EegLauncherFlag = true; DnaConsoleFlag = false;
	     			}
	     		if (console_str[2] == "C") { consoleBoard = "GTC: clear"; 
	     			}
	     		if (console_str[2] == "G") { consoleBoard = "GTA: zoom out";
	     				EegLauncherFlag = true; DnaConsoleFlag = false;
	     			}*/
	     		}
	     	
	     	if (console_str[1] == "C")	{ consoleBoard = "GC: clear"; 
	     		}

			if (console_str[1] == "G")	{
	     		consoleBoard = "GG*: zoom out"; 
	     		EegLauncherFlag = true; DnaConsoleFlag = false; // -- zoom out
	     		ClearAndHideConsole();
	     		/*if (console_str[2] == "A") {consoleBoard = "GGA: zoom out";
	     				EegLauncherFlag = true; DnaConsoleFlag = false;
	     			}
	     		if (console_str[2] == "T") { consoleBoard = "GGT: zoom out";
	     				EegLauncherFlag = true; DnaConsoleFlag = false;
	     			}
	     		if (console_str[2] == "C") { consoleBoard = "GGC: clear"; 
	     			}
	     		if (console_str[2] == "G") { consoleBoard = "GGA: zoom out";
	     				EegLauncherFlag = true; DnaConsoleFlag = false;
	     			}*/
	     		}
			}

    	
    } 
    
    private void ClearConsole() {
		msgBoard = "C";
		console_str[0] = "-"; console_str[1] = "-"; console_str[2] = "-"; console_str[3] = "-";
		CursorI = 1; consoleBoard = "C - clear";
    }
    private void ClearAndHideConsole() {
		msgBoard = " ";
		console_str[0] = " "; console_str[1] = " "; console_str[2] = " "; console_str[3] = " ";
		CursorI = 1; consoleBoard = "zoom out";
    }
}

/** Handle to the application context, used to e.g. fetch Drawables. */
private Context mContext;

/** Pointer to the text view to display "Paused.." etc. */
private TextView mStatusText;

/** The thread that actually draws the animation */
private MusicPlayerThread thread;

public MusicPlayerView(Context context, AttributeSet attrs) {
    super(context, attrs);

    // register our interest in hearing about changes to our surface
    SurfaceHolder holder = getHolder();
    holder.addCallback(this);

    // create thread only; it's started in surfaceCreated()
    thread = new MusicPlayerThread(holder, context, new Handler() {
        @Override
        public void handleMessage(Message m) {
            mStatusText.setVisibility(m.getData().getInt("viz"));
            mStatusText.setText(m.getData().getString("text"));
        }
    });
    

    setFocusable(true); // make sure we get key events
}

/**
 * Fetches the animation thread corresponding to this MusicPlayerView.
 *
 * @return the animation thread
 */
public MusicPlayerThread getThread() {
    return thread;
}

/**
 * Standard window-focus override. Notice focus lost so we can pause on
 * focus lost. e.g. user switches to take a call.
 */
@Override
public void onWindowFocusChanged(boolean hasWindowFocus) {
    if (!hasWindowFocus) thread.pause();
}

/**     * Installs a pointer to the text view used for messages.     */
public void setTextView(TextView textView) {
    mStatusText = textView;
}

/* Callback invoked when the surface dimensions change. */
public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    thread.setSurfaceSize(width, height);
}

/*
 * Callback invoked when the Surface has been created and is ready to be
 * used.
 */
public void surfaceCreated(SurfaceHolder holder) {
    // start the thread here so that we don't busy-wait in run()
    // waiting for the surface to be created
    if(thread.getState() == Thread.State.TERMINATED)
    {
    //MusicPlayerThread state TERMINATED..make new...under CheckCreateThread
    	thread = new MusicPlayerThread(holder, mContext, new Handler());
    } 
    
    thread.setRunning(true);
    thread.start();
}

/*
 * Callback invoked when the Surface has been destroyed and must no longer
 * be touched. WARNING: after this method returns, the Surface/Canvas must
 * never be touched again!
 */
public void surfaceDestroyed(SurfaceHolder holder) {
    // we have to tell thread to shut down & wait for it to finish, or else
    // it might touch the Surface after we return and explode
    boolean retry = true;
    
    
    thread.setRunning(false);
    while (retry) {
        try {
            thread.join();
            retry = false;
        } catch (InterruptedException e) {
        }
    }
}
}
