package com.aisavent.aisposprinter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import btmanager.*;

public class PrintDemo extends Activity implements OnClickListener{

	public static String TAG = "PrintDemo";
	public static boolean isConnected = false;
	
	public Button btn_demo_receipt,btn_qr_print,btn_txt_print;
	public EditText editTxtQr,editTxtPlain;
	BroadcastReceiver broadcastReceiver;
	public static int nDarkness, nFontSize, nTextAlign, nScaleTimesWidth,
	nScaleTimesHeight, nFontStyle, nLineHeight = 32, nRightSpace;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Pos.APP_Init(PrintDemo.this);
		setContentView(R.layout.demo_print);
		
		btn_demo_receipt = (Button) findViewById(R.id.btn_demo_receipt);
		btn_qr_print = (Button) findViewById(R.id.btn_qr_print);
		btn_txt_print = (Button) findViewById(R.id.btn_txt_print);
		
		editTxtQr = (EditText) findViewById(R.id.editTxtQr);
		editTxtPlain = (EditText) findViewById(R.id.editTxtPlain);
		
		
		btn_demo_receipt.setOnClickListener(this);
		btn_qr_print.setOnClickListener(this);
		btn_txt_print.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		
		if(v.getId() == R.id.btn_demo_receipt){
			Log.e(TAG, "onClick btn_demo_receipt MAC: "+Settings.deviceMacAddress);
			printDemoReceipt();	
		}
		else if(v.getId() == R.id.btn_qr_print){
			printQrCode();
		}
		else if(v.getId() == R.id.btn_txt_print){
			printPlainText();
		}
	}
	
	public void printPlainText(){
		
      String tmp = editTxtPlain.getText().toString();
		if ((tmp != null) && (!"".equals(tmp))) {
			Pos.POS_S_TextOut(tmp, 0, PrintDemo.nScaleTimesWidth,
					PrintDemo.nScaleTimesHeight, PrintDemo.nFontSize,
					PrintDemo.nFontStyle);
			Pos.POS_FeedLine();
			Pos.POS_FeedLine();
			Pos.POS_FeedLine();
		}
		else{
			
			Toast.makeText(PrintDemo.this, "Enter Plain Text", Toast.LENGTH_SHORT).show();
		}
	}
	public void printQrCode(){
		String tmp = editTxtQr.getText().toString();
		if ((tmp != null) && (!"".equals(tmp))) {
			Pos.POS_S_SetQRcode(tmp, 6, 4);
			Pos.POS_FeedLine();
		}
		else{
			
			Toast.makeText(PrintDemo.this, "Enter Qr Code Text", Toast.LENGTH_SHORT).show();
		}
	}
	public void printDemoReceipt(){
		
		Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logowhiteb);
		Pos.POS_PrintPicture(mBitmap, 325, 0);
		Pos.POS_FeedLine();
		
		String tmp = "";
		Pos.POS_S_TextOut(tmp, 0, PrintDemo.nScaleTimesWidth,
				PrintDemo.nScaleTimesHeight, PrintDemo.nFontSize,
				PrintDemo.nFontStyle);
		Pos.POS_FeedLine();
		Pos.POS_FeedLine();
		
		
		String sOrderDetails = "1. Spring Roll (480 Tk)\n";
		       sOrderDetails += "  with : Spring Roll\n";
		       sOrderDetails += "2. Fried Wonthon (240 Tk)\n";
		       sOrderDetails += "   with : No Topping\n";
		       sOrderDetails += "-------------------------------\n";
		       sOrderDetails += "Sub Total                 720 Tk\n";
		       sOrderDetails +="Restaurant Service Charge   0 Tk\n";
		       sOrderDetails +="VAT(15%)                  108 Tk\n";
		       sOrderDetails +="Delivery Fee               50 Tk\n";
		       sOrderDetails += "-------------------------------\n";
		       sOrderDetails +="Total                     878 Tk\n";
		       sOrderDetails += "-------------------------------\n";
		       sOrderDetails += "\n";
		       sOrderDetails += "\n";
		       sOrderDetails += "------For Delivery Person------\n";
		       
		Pos.POS_S_TextOut(sOrderDetails, 0, PrintDemo.nScaleTimesWidth,
					PrintDemo.nScaleTimesHeight, PrintDemo.nFontSize,
					PrintDemo.nFontStyle);
		Pos.POS_FeedLine();
		
		Pos.POS_S_SetQRcode("hungrynaki.com", 6, 4);
		Pos.POS_FeedLine();
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}

}
