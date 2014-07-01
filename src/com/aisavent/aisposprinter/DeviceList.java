package com.aisavent.aisposprinter;



import java.util.Set;

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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DeviceList extends Activity{
	    protected static final String TAG = "DeviceList";
	    private BluetoothAdapter mBluetoothAdapter;
	    private ArrayAdapter<String> mDevicesArrayAdapter;
	   
	    BluetoothDevice mBluetoothDevice;
	    BroadcastReceiver mReceiver;
	    
	    @Override
	    protected void onCreate(Bundle mSavedInstanceState) 
	    {
	        super.onCreate(mSavedInstanceState);
	        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	        setContentView(R.layout.device_list);

	        setResult(Activity.RESULT_CANCELED);
	        mDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

	        ListView mDeviceListView = (ListView) findViewById(R.id.devices);
	        mDeviceListView.setAdapter(mDevicesArrayAdapter);
	        mDeviceListView.setOnItemClickListener(mDeviceClickListener);

			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			
			Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter.getBondedDevices();

	        if (mPairedDevices.size() > 0) 
	        {
	            findViewById(R.id.title_devices).setVisibility(View.VISIBLE);
	            for (BluetoothDevice mDevice : mPairedDevices) 
	            {
	            	mDevicesArrayAdapter.add(mDevice.getName() + "\n" + mDevice.getAddress());
	            }
	        } 
	        
			mBluetoothAdapter.startDiscovery(); 
			
             mReceiver = new BroadcastReceiver() {
            		
            	public void onReceive(Context context, Intent intent) {
            	    String action = intent.getAction();
            	
            	    if (BluetoothDevice.ACTION_FOUND.equals(action)) 
            	    {
            	        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            	        Log.e(TAG, "Name : "+device.getName() + "\n MAC : " + device.getAddress());
            	        
            	        mDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            	    }
            	  }
            	};

            	IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND); 
            	registerReceiver(mReceiver, filter);
			
			
	    }

	    @Override
	    protected void onDestroy() 
	    {
	        super.onDestroy();
	        if (mBluetoothAdapter != null) 
	        {
	            mBluetoothAdapter.cancelDiscovery();
	            
	            this.unregisterReceiver(mReceiver);
	        }
	    }
	    
	    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() 
	    {
	        public void onItemClick(AdapterView<?> mAdapterView, View mView, int mPosition, long mLong) 
	        {
	            mBluetoothAdapter.cancelDiscovery();
	            String mDeviceInfo = ((TextView) mView).getText().toString();
	            String mDeviceAddress = mDeviceInfo.substring(mDeviceInfo.length() - 17);
	            Log.v(TAG, "Device_Address " + mDeviceAddress);

	            Bundle mBundle = new Bundle();
	            mBundle.putString("DeviceAddress", mDeviceAddress);
	            Intent mBackIntent = new Intent();
	            mBackIntent.putExtras(mBundle);
	            setResult(Activity.RESULT_OK, mBackIntent);
	            finish();
	        }
	    };


}
