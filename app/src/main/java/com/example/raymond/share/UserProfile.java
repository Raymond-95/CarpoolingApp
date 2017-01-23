package com.example.raymond.share;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.raymond.share.jsonparser.ShareApi;
import com.example.raymond.share.jsonparser.ShareJSON;
import com.example.raymond.share.model.User;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    private Toolbar toolbar;
    private static User user;
    private static String getPhonenum;
    private static Button phoneBtn;
    private static  Button send;
    private static String TAG = "com.example.raymond.share.UserProfile";
    private static RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Profile");

        layout = (RelativeLayout) findViewById(R.id.top);
        send = (Button) findViewById(R.id.send);
        layout.removeView(send);

        String pass_id = getIntent().getStringExtra("id");
        int id = Integer.parseInt(pass_id);
        getUser(id);
    }

    public void getUser(int id) {

        ShareApi.init(getApplicationContext())
                .getUser(id)
                .call(new ShareApi.CustomJsonResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject response, ShareJSON meta) {

                        user = new User(meta.getResult());

                        TextView nameView = (TextView)findViewById(R.id.name);
                        nameView.setText(user.getName());

                        Button emailBtn = (Button) findViewById(R.id.email);
                        emailBtn.setText(user.getEmail());

                        getPhonenum = user.getPhonenum();
                        User me = new User().getUserAccount(getApplicationContext());
                        phoneBtn = (Button) findViewById(R.id.phone);
                        boolean result = verifyUser(user.getId());
                        if (me.getId() == user.getId())
                        {
                            popUpWindow();
                        }
                        else{

                            if (result) {
                                layout.addView(send);
                                send.setText("SEND FRIEND REQUEST");
                                send.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                });
                            }
                            else{
                                RelativeLayout layout = (RelativeLayout) findViewById(R.id.top);
                                layout.removeView(send);
                            }
                        }
                        phoneBtn.setText(setPhoneFormat(user.getPhonenum()));

                        ImageView background = (ImageView) findViewById(R.id.background);
                        new DownloadImage(background).execute(user.getImageUrl());
                        background.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);

                        CircleImageView profilePic = (CircleImageView)findViewById(R.id.profileImage);
                        profilePic.setBorderWidth(4);
                        profilePic.setBorderColor(getResources().getColor(R.color.white));
                        new DownloadImage (profilePic).execute(user.getImageUrl());

                        Button fbProfileBtn = (Button) findViewById(R.id.fbProfile);
                        fbProfileBtn.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                                String facebookUrl = user.getProfileUrl();
                                facebookIntent.setData(Uri.parse(facebookUrl));
                                startActivity(facebookIntent);
                            }
                        });

                    }

                    @Override
                    public void onFailure(Throwable e, JSONObject response, ShareJSON meta) {

                    }

                });
    }

    //set phone number in "(xxx) xxx-xxxx
    public String setPhoneFormat(String phonenumber){
        PhoneNumberUtil pnu = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber pn = null;
        try {
            pn = pnu.parse(phonenumber, "US");
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        String phonenum = pnu.format(pn, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);

        return phonenum;
    }

    private static  AlertDialog dialog;

    //update user phone number
    public void popUpWindow() {

        phoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(UserProfile.this);
                View mView = getLayoutInflater().inflate(R.layout.change_phonenum, null);
                final EditText changePhonenum = (EditText) mView.findViewById(R.id.changePhonenum);
                changePhonenum.setText(user.getPhonenum());

                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (changePhonenum.getText().toString().trim().length() < 10) {
                            changePhonenum.setError("The phone number format is wrong!");
                        }
                        else{
                            user.setPhonenum(changePhonenum.getText().toString());
                            updateUser(
                                    user.getEmail(),
                                    user.getName(),
                                    user.getPhonenum(),
                                    user.getProfileUrl(),
                                    user.getImageUrl()
                            );
                            dialog.dismiss();
                        }
                    }
                });

                mBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                mBuilder.setView(mView);
                dialog = mBuilder.create();
                dialog.show();
            }
        });
    }

    private void updateUser(String email, String name, String phonenum, String profileUrl, String imageUrl){

        ShareApi.init(this)
                .updateUser(
                        email,
                        name,
                        phonenum,
                        profileUrl,
                        imageUrl

                ).call(new ShareApi.DialogResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject response, ShareJSON meta) {
                        Log.e(TAG, "Phone number is updated");
                        getAccount();
                    }

                    @Override
                    public void onFailure(Throwable e, JSONObject response, ShareJSON meta) {
                    }

                })
                .keepProgressDialog()
                .getProgressDialog();
    }

    public void getAccount() {
        ShareApi.init(this)
                .getAccount().call(
                new ShareApi.DialogResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject response, ShareJSON meta) {
                        User userInfo = new User(response);
                        userInfo.saveUserAccount(UserProfile.this);
                        setResult(RESULT_OK);

                        finish();
                        startActivity(getIntent());

                    }

                    @Override
                    public void onFailure(Throwable e, JSONObject response, ShareJSON meta) {

                    }
                }
        );
    }

    private static boolean result;
    private boolean verifyUser(int id){

        ShareApi.init(this)
                .verifyUser(
                        id
                ).call(new ShareApi.DialogResponseHandler() {

            @Override
            public void onSuccess(JSONObject response, ShareJSON meta) {

                String object = null;
                try {
                    object = response.getString("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (object.equals("true"))
                    result = true;
                else if (object.equals("false"))
                    result = false;
            }

            @Override
            public void onFailure(Throwable e, JSONObject response, ShareJSON meta) {
            }
        });

        return result;
    }
}
