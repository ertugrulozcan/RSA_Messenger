package com.aero.rsamessenger;

import java.util.ArrayList;
import java.util.List;

import com.aero.json.Json;
import com.aero.rsa.RSA;
import com.aero.rsa.RsaKey.*;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class JoinActivity extends Activity
{
	Activity joinActivity;
	
	String phoneNumber;
	String emailAdress;
	String password;
	
	private static final String PREFS = "userDataPreferences";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		this.joinActivity = this;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join);
		
		final Button joinButton = (Button)findViewById(R.id.joinButton);
		final EditText phoneNumberTextBox = (EditText)findViewById(R.id.phoneNumberTextBox);
		final EditText emailTextBox = (EditText)findViewById(R.id.emailTextBox);
		final EditText passwordTextBox = (EditText)findViewById(R.id.passwordTextBox);
		
		joinButton.setOnClickListener(new OnClickListener()
		{
			// Butona týklandýðýnda çalýþtýrýlacak metod:
			@Override
			public void onClick(View arg0)
			{
				phoneNumber = phoneNumberTextBox.getText().toString();
				emailAdress = emailTextBox.getText().toString();
				password = passwordTextBox.getText().toString();
				
				// Kullanici icin yeni RSA parametrelerinin uretilmesi
				RSA rsa = new RSA();
				PublicKey publicKey = rsa.getPublicKey();
				PrivateKey privateKey = rsa.getPrivateKey();
				
				// Kullanici nesnesinin olusturulmasi
				User user = new User(phoneNumber, emailAdress, publicKey, privateKey);
				
				// Kullanýcýnýn veritabanýna kaydý
				//SaveOnDatabase(user);
				
				// Kullanýcý verilerinin json dosyasýna kaydý
				SaveOnJsonFile(user);
				
				// Kulanýcý verilerinin local storage uzerine kaydedilmesi
				SharedPreferences userDataPreferences = getSharedPreferences(PREFS, 0);
				SharedPreferences.Editor editor = userDataPreferences.edit();
				editor.putString("phoneNumber", phoneNumber);
				editor.putString("password", password);
				editor.commit();
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.join, menu);
		return true;
	}
	
	@SuppressWarnings("unused")
	private void SaveOnDatabase(User user)
	{
		// Kullanicinin veritabanina kaydettirilmesi
		DatabaseConnector dbConnector = new DatabaseConnector();
		boolean success = false;
		success = dbConnector.JoinUser(user);
		
		if (success)
		{
			// Ýslem basarili!
			AlertDialog.Builder builder = new AlertDialog.Builder(joinActivity);
			builder.setMessage("Kayýt tamamlandý.");
			
			// Tamam butonunun eklenmesi
			builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int id)
				{
					//
				}
			});
			
			AlertDialog alertDialog = builder.create();
			alertDialog.show();
		}
		else
		{
			// Ýslem basarisiz!
			// Hata mesajinin goruntulenmesi
			AlertDialog.Builder builder = new AlertDialog.Builder(joinActivity);
			builder.setMessage(dbConnector.errorMessage);
			
			// Tamam butonunun eklenmesi
			builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int id)
				{
					
				}
			});
			
			AlertDialog alertDialog = builder.create();
			alertDialog.show();
		}
	}
	
	private void SaveOnJsonFile(User user)
	{
		// Dosyadan json stringinin okunmasý
		String jsonString = Json.ReadFromFile("/storage/sdcard/RsaMessengerUserDB.json");
		
		// Kullanýcý listesinin okunmasý
		List<User> userList = Json.DeserializeUserList(jsonString);
		if(userList == null)
		{
			userList = new ArrayList<User>();
		}
		else
		{
			userList.add(user);
			// Yeni dizinin kaydedilmesi
			jsonString = Json.SerializeUserList(userList);
			
			Json.WriteToFileFile(jsonString, "/storage/sdcard/RsaMessengerUserDB.json", false);
		}

	}
	
}
