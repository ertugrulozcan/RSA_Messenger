package com.aero.rsamessenger;

import java.util.List;

import com.aero.json.Json;
import com.aero.rsa.Encoder;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendActivity extends Activity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send);
		
		InitializeComponents();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.send, menu);
		return true;
	}
	
	private void InitializeComponents()
	{
		InitializeButtons();
	}
	
	private void InitializeButtons()
	{
		SetCryptoButtonActionListener();
	}
	
	private void SetCryptoButtonActionListener()
	{
		final Button sendButton = (Button)findViewById(R.id.sendButton);
		final EditText phoneNumber = (EditText)findViewById(R.id.phoneNumber);
		final EditText messageTextField = (EditText)findViewById(R.id.message);
		
		sendButton.setOnClickListener(new OnClickListener()
		{
			// Butona týklandýðýnda çalýþtýrýlacak metod:
			@Override
			public void onClick(View arg0)
			{
				String phoneNo = phoneNumber.getText().toString();
				
				// Mesajin sifrelenmesi
				String message = messageTextField.getText().toString();
				// Dosyadan json stringinin okunmasý
				String jsonString = Json.ReadFromFile("/storage/sdcard/RsaMessengerUserDB.json");
				
				// Kullanýcý listesinin okunmasý
				List<User> userList = Json.DeserializeUserList(jsonString);
				
				boolean send = false;
				
				for(int i = 0; i < userList.size(); i++)
				{
					if(phoneNo.equals(userList.get(i).phoneNumber))
					{
						Encoder encoder = new Encoder(userList.get(i).publicKey);
						String cryptedMessage = encoder.Encrypt(message);
						
						//
						// Mesajýn gönderilmesi
						//
						
						if (phoneNo.length() > 0 && cryptedMessage.length() > 0)
						{
							//SmsManager sms = SmsManager.getDefault();
			                //sms.sendTextMessage(phoneNo, phoneNo, cryptedMessage, null, null);
			                
			                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
			                sendIntent.putExtra("address", phoneNo);
			                sendIntent.putExtra("sms_body", cryptedMessage);
			                sendIntent.setType("vnd.android-dir/mms-sms");
			                startActivity(sendIntent);
			                
							send = true;
							//Toast.makeText(getBaseContext(), "Mesaj " + phoneNo + " numaralý kiþiye iletildi. (" + userList.get(i).publicKey.N().toString().substring(0, 10) + ")", Toast.LENGTH_SHORT).show();
						}
						else
							Toast.makeText(getBaseContext(), "Lütfen her iki alaný da doldurunuz.", Toast.LENGTH_SHORT).show();
					}
				}
				
				if(!send)
				{
					Toast.makeText(getBaseContext(), "Bu numara sistemde kayýtlý bir numara deðil.", Toast.LENGTH_SHORT).show();
				}
			}
			
		});
	}
}
