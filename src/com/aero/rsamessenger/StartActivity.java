package com.aero.rsamessenger;

import java.util.List;

import com.aero.json.Json;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;

public class StartActivity extends Activity
{
	private static final String PREFS = "userDataPreferences";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		// Uygulama baslatildiginda oncelikle kullanicinin daha once kayit olup olmadigini kontrol eder.
		// Kayit olmadiysa kayit sayfasina yonlendirilir.
		// Kayit yapilmissa local depodan parametre verileri cekilerek ilgili nesnelere yazilir.
		if (isJoin())
		{
			SharedPreferences userDataPreferences = getSharedPreferences(PREFS, 0);
			String phoneNumber = userDataPreferences.getString("phoneNumber", "NULL");
			
			//
			// Dosyadan json stringinin okunmasý
			String jsonString = Json.ReadFromFile("/storage/sdcard/RsaMessengerUserDB.json");
			
			// Kullanýcý listesinin okunmasý
			List<User> userList = Json.DeserializeUserList(jsonString);
			for(int i = 0; i < userList.size(); i++)
			{
				if(userList.get(i).phoneNumber.equals(phoneNumber))
				{
					UserData.currentUser = userList.get(i);
				}
			}
			
			// Kullanici kayitliysa login ekranina yonlendirilir.
			Intent navigation = new Intent(getApplicationContext(), LoginActivity.class);
			startActivity(navigation);
			finish();
		}
		else
		{
			// Kullanici kayitli degilse kayit ekranina yonlendirilir.
			Intent navigation = new Intent(getApplicationContext(), JoinActivity.class);
			startActivity(navigation);
			finish();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}
	
	private boolean isJoin()
	{
		SharedPreferences userDataPreferences = getSharedPreferences(PREFS, 0);
		String phoneNumber = userDataPreferences.getString("phoneNumber", "NULL");
		
		if (phoneNumber.equals("NULL"))
			return false;
		else
			return true;
	}
	
}
