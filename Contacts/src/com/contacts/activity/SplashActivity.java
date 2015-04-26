package com.contacts.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.contacts.db.DbHelper;
 



public class SplashActivity extends Activity {

	 
    @Override
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.splashactivity); 
 
        SharedPreferences sp = getSharedPreferences("contacts_sp", MODE_PRIVATE);
        
        boolean firstStart = sp.getBoolean("FIRST_START", true);
        if(firstStart){
        	DbHelper dbHelper = new DbHelper(this, "Db_Contacts");
        	SQLiteDatabase db = dbHelper.getWritableDatabase();
        	db.close();
        	dbHelper.close();
        	
        	/*IndexOperateHelper ioh = new IndexOperateHelper(this.getApplicationContext().getFilesDir().getAbsolutePath());
        	ioh.addCommonDocument("-1", " ", "-1");
        	ioh.close();*/
        }
        
        Editor editor = sp.edit();
        editor.putBoolean("FIRST_START", false);
        editor.commit();
        
        Intent mainIntent = new Intent(SplashActivity.this,ContactsMainActivity.class); 
        SplashActivity.this.startActivity(mainIntent); 
        SplashActivity.this.finish(); 
    } 

}
