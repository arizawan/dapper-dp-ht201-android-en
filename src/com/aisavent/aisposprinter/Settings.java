package com.aisavent.aisposprinter;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import btmanager.*;

public class Settings extends Activity implements OnClickListener{

	protected static final String TAG = "Settings";
	
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;
	public Button btn_src_connect;
	public BluetoothAdapter mBluetoothAdapter;
	BluetoothDevice mBluetoothDevice;
	public static String deviceMacAddress = "";
	BroadcastReceiver broadcastReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Pos.APP_Init(Settings.this);
		
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		setContentView(R.layout.settings);
		btn_src_connect = (Button) findViewById(R.id.btn_src_connect);
		btn_src_connect.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_src_connect){
			
			if (mBluetoothAdapter == null) { 
				finish();
			    return;
			}
			if (!mBluetoothAdapter.isEnabled()) {
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
			else
			{
				Intent connectIntent = new Intent(Settings.this, DeviceList.class);
                startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
			}
			
		}
	}
	
	 public void onActivityResult(int mRequestCode, int mResultCode, Intent mDataIntent) 
	    {
	        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

	        switch (mRequestCode) 
	        {
	            case REQUEST_CONNECT_DEVICE:
	                if (mResultCode == Activity.RESULT_OK) 
	                {
	                    Bundle mExtra = mDataIntent.getExtras();
	                    String mDeviceAddress = mExtra.getString("DeviceAddress");
	                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
	                    mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(mDeviceAddress);
	                    
	                   Settings.deviceMacAddress =  mBluetoothDevice.getAddress();
	                  
	                   connectPrinter();
	                 }
	                break;

	            case REQUEST_ENABLE_BT:
	                if (mResultCode == Activity.RESULT_OK) 
	                {
	                    Intent connectIntent = new Intent(Settings.this, DeviceList.class);
	                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
	                } 
	                else 
	                {
	                    Toast.makeText(Settings.this, "Message", Toast.LENGTH_SHORT).show();
	                }
	                break;
	        }
	    }
	 
	 public void connectPrinter(){
		 
		 String mac = Settings.deviceMacAddress ;
		 Pos.POS_Open(mac);
		 broadcastReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					
		    		String action = intent.getAction();
					//BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					
					if (action.equals(ConnectThread.ACTION_CONNECTED)) {
						
						PrintDemo.isConnected = true;
						Toast.makeText(Settings.this, "CONNECTED", Toast.LENGTH_SHORT).show();
						unregisterReceiver(broadcastReceiver);
					} 
					else if (action.equals(ConnectThread.ACTION_DISCONNECTED)) {
						
						Toast.makeText(Settings.this, "DISCONNECTED", Toast.LENGTH_SHORT).show();
					}
					else if (action.equals(ConnectThread.ACTION_STARTCONNECTING)) {
						
						Toast.makeText(Settings.this, "START CONNECTING...", Toast.LENGTH_SHORT).show();
					}
					
				}

			};
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(ConnectThread.ACTION_DISCONNECTED);
			intentFilter.addAction(ConnectThread.ACTION_CONNECTED); 
			intentFilter.addAction(ConnectThread.ACTION_STARTCONNECTING);
			registerReceiver(broadcastReceiver, intentFilter);	
	 }
	
	 @Override
	protected void onPause() {
		super.onPause();
	}
	 
    @Override
    protected void onDestroy() {
    	
    	//if(broadcastReceiver.isOrderedBroadcast())
    		//unregisterReceiver(broadcastReceiver);
    	
    	super.onDestroy();
    	
    }
	
}
