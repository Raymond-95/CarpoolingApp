package com.example.raymond.share.jsonparser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.View;

import com.example.raymond.share.Login;
import com.example.raymond.share.R;

import org.apache.http.cookie.Cookie;

import java.io.File;
import java.util.List;

public class Share {
	
	public static final String Share_KEY = "T6q8XU2Bt7aiT1O1ROktyIwDC8vlCB4a";
	public static final String CAMPAIGN_ID = "1023";
	
	public static final String MODEL = android.os.Build.MODEL;
	public static final String MAKER = android.os.Build.MANUFACTURER;
	public static final String PLATFORM = "android";
	
	public static final String APP_DIR = "/Share/";
	public static final String APP_CAMERA_DIR = "/Share/Share Images/";
	
	
	public static final int RC_LOGIN_EMAIL = 1;
	public static final int RC_SIGN_UP = 2;
	public static final int RC_LOGIN = 101;
    public static final int RC_ADD_PLACE = 103;
	
	public static final int RC_PICK_PROFILE_IMG = 104;
    public static final int RC_ACCOUNT = 105;
    public static final int RC_ANIMATE_SLIDE = 106;
	
	public static final int RC_TWITTER_LOGIN = 3;
	public static final int RC_TWITTER_LOGIN_SUCCESS = 4;
	public static final int RC_TWITTER_LOGIN_FAILURE = 5;
	
	public static final int RC_GPS = 200;
	
	public final static int RC_PLACE_DETAILS = 300;
	
	public static final int RC_CHECK_IN = 400;
	public static final int RC_GOOGLE_PLAY_SERVICE = 401;
	
	public static final int RC_IMAGE_CAPTURE = 20;
	public static final int RC_IMAGE_SELECT = 21;


	public static final int RC_LOGOUT = 777;
    public static final int RC_GooglePlayServices = 888;

    public static final String INTENT_LOGIN = "Share.intent.login";
    public static final String INTENT_QUERY = "Share.intent.query";
    public static final String INTENT_IMAGE_LIST = "Share.intent.imageList";
    public static final String INTENT_IMAGE_POSITION = "Share.intent.imagePosition";
    public static final String  INTENT_CHECK_GPS = "Share.intent.checkGPS";

    public static final String KEY_BITMAP_STORAGE = "Share.key.viewbitmap";

	public static void logoutTest(final Context context){
		
		new ShareRestClient(context).clearCookies();
        Intent intentLogin = new Intent(context, Login.class);
        intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intentLogin);
	}

	public static class AppPreferences {
		
	    public static final String PREF_KEY_LASTUSER = "last_user";
	    public static final String PREF_KEY_SESSION_COOKIES = "session_cookies";
	    private static final String APP_SHARED_PREFS = AppPreferences.class.getSimpleName();
	    private static SharedPreferences _sharedPrefs;
	    private static Editor _prefsEditor;
	    
	   //user info
        public static final String PREF_KEY_USER_ID = "Share.user.id";
        public static final String PREF_KEY_USER_EMAIL = "Share.user.email";
        public static final String PREF_KEY_USER_NAME = "Share.user.name";
        public static final String PREF_KEY_USER_PHONE_NUM = "Share.user.phonenum";
        public static final String PREF_KEY_USER_PROFILE_URL = "Share.user.profileUrl";
        public static final String PREF_KEY_USER_IMAGE_URL = "Share.user.imageUrl";
        public static final String PREF_KEY_USER_TOKEN = "Share.user.token";
        
        public static final String PREF_KEY_UUID = "Share.uuid";
        public static final String PREF_KEY_REGID = "Share.reg_id";

	    public AppPreferences(Context context) {
	    	AppPreferences._sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
            AppPreferences._prefsEditor = _sharedPrefs.edit();
	    }

	    
	    public String getLastUserAcc() {
	        return _sharedPrefs.getString(PREF_KEY_LASTUSER, null);
	    }

	    public void saveLastUserAcc(String acc) {
	        _prefsEditor.putString(PREF_KEY_LASTUSER, acc);
	        _prefsEditor.commit();
	    }
	    
	    public String getSessionCookie() {
	        return _sharedPrefs.getString(PREF_KEY_SESSION_COOKIES, null);
	    }

	    public void setSessionCookie(List<Cookie> cookies) {
	    	for (int i = 0; i < cookies.size(); i++) {
                if(cookies.get(i).getName().contentEquals("PHPSESSID")) {

                   String PHPSESSID = cookies.get(i).getValue();
                   _prefsEditor.putString(PREF_KEY_SESSION_COOKIES, PHPSESSID);
       	           _prefsEditor.commit();
                }
             }
	        
	    }
	   //user info
        public int getUserId() {
            return _sharedPrefs.getInt(PREF_KEY_USER_ID, 0);
        }

        public void saveUserId(int value) {
            _prefsEditor.putInt(PREF_KEY_USER_ID, value);
            _prefsEditor.commit();
        }

        public String getUserEmail() {
            return _sharedPrefs.getString(PREF_KEY_USER_EMAIL, null);
        }

        public void saveUserEmail(String value) {
            _prefsEditor.putString(PREF_KEY_USER_EMAIL, value);
            _prefsEditor.commit();
        }

        public String getUsername() {
            return _sharedPrefs.getString(PREF_KEY_USER_NAME, null);
        }

        public void saveUsername(String value) {
            _prefsEditor.putString(PREF_KEY_USER_NAME, value);
            _prefsEditor.commit();
        }

        public String getUserPhonenum() {
            return _sharedPrefs.getString(PREF_KEY_USER_PHONE_NUM, null);
        }

        public void saveUserPhonenum(String value) {
            _prefsEditor.putString(PREF_KEY_USER_PHONE_NUM, value);
            _prefsEditor.commit();
        }

        public String getUserProfileUrl() {
            return _sharedPrefs.getString(PREF_KEY_USER_PROFILE_URL, null);
        }

        public void saveUserProfileUrl(String value) {
            _prefsEditor.putString(PREF_KEY_USER_PROFILE_URL, value);
            _prefsEditor.commit();
        }

        public String getUserImageUrl() {
            return _sharedPrefs.getString(PREF_KEY_USER_IMAGE_URL, null);
        }

        public void saveUserImageUrl(String value) {
            _prefsEditor.putString(PREF_KEY_USER_IMAGE_URL, value);
            _prefsEditor.commit();
        }

        public String getUserToken() {
            return _sharedPrefs.getString(PREF_KEY_USER_TOKEN, null);
        }

        public void saveUserToken(String value) {
            _prefsEditor.putString(PREF_KEY_USER_TOKEN, value);
            _prefsEditor.commit();
        }
        
        public String getUUID() {
            return _sharedPrefs.getString(PREF_KEY_UUID, "");
        }

        public void saveUUID(String uuid) {
            _prefsEditor.putString(PREF_KEY_UUID, uuid);
            _prefsEditor.commit();
        }
        
        public String getRegID() {
            return _sharedPrefs.getString(PREF_KEY_REGID, "");
        }

        public void saveRegID(String regid) {
            _prefsEditor.putString(PREF_KEY_REGID, regid);
            _prefsEditor.commit();
        }
	}


	public static final class Camera {
		// Standard storage location for digital camera files
		//private static final String CAMERA_DIR = "/dcim/";

		public static File getAlbumStorageDir() {
			return new File (
					Environment.getExternalStorageDirectory()
					+ APP_CAMERA_DIR
			);
		}
	}
	
	public static final class Helpers {

        public static String getPath(Uri uri, Activity activity) {
            String[] projection = { MediaStore.MediaColumns.DATA };
            Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        
    	public static int getDp(Context context, int value){
    		
    		return (int) TypedValue.applyDimension(
    		        TypedValue.COMPLEX_UNIT_DIP,
    		        value, 
    		        context.getResources().getDisplayMetrics() );
    	}

        public static int pxToDp(Context context, int value){
            return (int) (value / context.getResources().getDisplayMetrics().density + 0.5f);
        }

        public static int dpToPx(Context context, int value){
            return (int) (value * context.getResources().getDisplayMetrics().density + 0.5f);
        }

        public static boolean checkInternet(Context ctx) {
            ConnectivityManager connec = (ConnectivityManager) ctx
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            // Check if wifi or mobile network is available or not. If any of them is
            // available or connected then it will return true, otherwise false;
            return wifi.isConnected() || mobile.isConnected();
        }

        public static String styleWebView(Context context, String content){
            return styleWebView(context, content, 0);
        }

        public static String styleWebView(Context context, String content, int margin){
            return styleWebView(context, content, margin, margin);
        }

        public static String styleWebView(Context context, String content, int marginTB, int marginLR ){
            if(content == null){
                content = "";
            }
            if(context == null){
                return "";
            }
            return  "<html>" +
                    "<head><meta name=\"viewport\" content=\"width=device-width, user-scalable=no\" />" +
                    "</head><body>" + content + "</body>" +
                    "</html>" +
                    "<style type=\"text/css\"> " +
                    "@font-face {font-family: 'HelveticaNeue';src: url('file:///android_asset/fonts/helvetica-neue.ttf'); }" +
                    "body  { font-family:\"HelveticaNeue\"; " +
                    "height:auto;" +
                    "margin-left:"+marginLR+"px; " +
                    "margin-right:"+marginLR+"px; " +
                    "margin-top:"+marginTB+"px; " +
                    "margin-bottom:"+marginTB+"px; " +
                    "padding:0; " +
                    "color:"+colorIntToString(context.getResources().getColor(R.color.colorPrimaryDark))+"; }" +
                    "</style>";
        }

        public static String colorIntToString(int color){
            return "#" + Integer.toHexString(color).substring(2);
        }

        public static int getStatusBarHeight(Context context) {
            int result = 0;
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId);
            }
            return result;
        }

        public static void setViewPaddingStatusBar(Context context, View view) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
            view.setPadding(0, getStatusBarHeight(context), 0, 0);
        }

    }

}
