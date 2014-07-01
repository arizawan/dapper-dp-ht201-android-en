package com.aisavent.aisposprinter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import btmanager.*;

public class MainActivity extends Activity implements OnClickListener{
	
	public Button btn_prnt_dmo,btn_setting,btn_exit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Pos.APP_Init(MainActivity.this);
		setContentView(R.layout.main);
		
		btn_prnt_dmo = (Button) findViewById(R.id.btn_prnt_dmo);
		btn_setting = (Button) findViewById(R.id.btn_setting);
		btn_exit = (Button) findViewById(R.id.btn_exit);
		
		btn_prnt_dmo.setOnClickListener(this);
		btn_setting.setOnClickListener(this);
		btn_exit.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		
		if(v.getId() == R.id.btn_prnt_dmo){
			Intent intent = new Intent(MainActivity.this, PrintDemo.class);
			startActivity(intent);
		}
		else if(v.getId() == R.id.btn_setting){
			
			Intent intent = new Intent(MainActivity.this, Settings.class);
			startActivity(intent);
		}
		else if(v.getId() == R.id.btn_exit){
			this.finish();
	    	Intent exit0 = new Intent(Intent.ACTION_MAIN);
	        exit0.addCategory(Intent.CATEGORY_HOME);
	        exit0.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        exit0.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        startActivity(exit0);
		}
	}
	
	@Override
	protected void onDestroy() {
		Pos.POS_Close();
		Pos.APP_UnInit();
		super.onDestroy();
		
	}
	
}
