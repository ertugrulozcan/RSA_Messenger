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

public class EmailActivity extends Activity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_email);
		
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
		final EditText emailAdress = (EditText)findViewById(R.id.emailAdress);
		final EditText messageTextField = (EditText)findViewById(R.id.message);
		
		sendButton.setOnClickListener(new OnClickListener()
		{
			// Butona t�kland���nda �al��t�r�lacak metod:
			@Override
			public void onClick(View arg0)
			{
				String email = emailAdress.getText().toString();
				
				// Mesajin sifrelenmesi
				String message = messageTextField.getText().toString();
				// Dosyadan json stringinin okunmas�
				String jsonString = Json.ReadFromFile("/storage/sdcard/RsaMessengerUserDB.json");
				
				// Kullan�c� listesinin okunmas�
				List<User> userList = Json.DeserializeUserList(jsonString);
				
				boolean send = false;
				
				for(int i = 0; i < userList.size(); i++)
				{
					if(email.equals(userList.get(i).emailAdress))
					{
						Encoder encoder = new Encoder(userList.get(i).publicKey);
						String cryptedMessage = encoder.Encrypt(message);
						
						//
						// Mesaj�n g�nderilmesi
						//
						// Intent olu�turulmas�
						Intent emailIntent = new Intent(Intent.ACTION_SEND);
						// G�nderilecek adresler
						emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAdress.getText().toString()});		  
						// Konu
						emailIntent.putExtra(Intent.EXTRA_SUBJECT, "RSA Test");
						// G�vde
						emailIntent.putExtra(Intent.EXTRA_TEXT, cryptedMessage);
						// ��erik tipi
						emailIntent.setType("message/rfc822");
						// Activitynin �a��r�lmas�
						startActivity(Intent.createChooser(emailIntent, "E-posta istemcisi se�imi :"));
						
						Toast.makeText(getApplicationContext(), "E-posta istemcisine y�nlendiriliyor...", Toast.LENGTH_LONG).show();
						
						send = true;
					}
				}
				
				if(!send)
				{
					Toast.makeText(getBaseContext(), "Bu adres sistemde kay�tl� bir adres de�il.", Toast.LENGTH_SHORT).show();
				}
			}
			
		});
	}
}
