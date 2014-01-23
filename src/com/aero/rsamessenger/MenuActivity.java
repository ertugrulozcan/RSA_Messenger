package com.aero.rsamessenger;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MenuActivity extends Activity
{
	private final Activity menuActivity = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		SetButtons();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	private void SetButtons()
	{
		final ImageButton sendButton = (ImageButton) findViewById(R.id.messageSendButton);
		final ImageButton readButton = (ImageButton) findViewById(R.id.messageReadButton);
		
		sendButton.setOnClickListener(new OnClickListener()
		{
			// Butona týklandýðýnda çalýþtýrýlacak metod:
			@Override
			public void onClick(View arg0)
			{
				String[] types = {"SMS", "E-posta"};
				AlertDialog.Builder builder = new AlertDialog.Builder(menuActivity);
			    builder.setTitle("Mesaj tercihi");
			    builder.setItems(types, new DialogInterface.OnClickListener()
			    {
					@Override
					public void onClick(DialogInterface arg0, int index)
					{
						switch(index)
						{
							case 0:
							{
								Intent intent = new Intent(MenuActivity.this, SendActivity.class);
								startActivity(intent);
								finish();
							}
							break;
							
							case 1:
							{
								Intent intent = new Intent(MenuActivity.this, EmailActivity.class);
								startActivity(intent);
								finish();
							}
							break;
						}
					}
			    });
			    AlertDialog alertDialog = builder.create();
			    alertDialog.show();
			}
		});
		
		readButton.setOnClickListener(new OnClickListener()
		{
			// Butona týklandýðýnda çalýþtýrýlacak metod:
			@Override
			public void onClick(View arg0)
			{
				Intent intent = new Intent(MenuActivity.this, ReadActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
}
