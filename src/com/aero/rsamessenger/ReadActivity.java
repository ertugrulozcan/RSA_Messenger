package com.aero.rsamessenger;

import com.aero.rsa.Decoder;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ReadActivity extends Activity
{
	final Context context = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read);

		SetButtons();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.read, menu);
		return true;
	}
	
	private void SetButtons()
	{
		final Button decryptoButton = (Button) findViewById(R.id.decryptoButton);
		final EditText messageArea = (EditText) findViewById(R.id.messageArea);
		
		decryptoButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				String cryptedMessage = messageArea.getText().toString();
				
				try
				{
					Decoder decoder = new Decoder(UserData.currentUser.privateKey);
					String message = decoder.Decrypt(cryptedMessage);
					
					messageArea.setText(message);
				}
				catch(Exception ex)
				{
					AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);

                    dlgAlert.setMessage("Hatalý, bozuk veya eksik mesaj hatasý. Bu mesaj çözümlenemez." + " (" + ex.getMessage() + ")");
                    dlgAlert.setTitle("Hata");
                    dlgAlert.setPositiveButton("Tamam", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();

                    dlgAlert.setPositiveButton("Tamam", new DialogInterface.OnClickListener()
                    {
                    	public void onClick(DialogInterface dialog, int which)
                    	{
                    		messageArea.setText("");
                    	}
                    });
				}
			}
			
		});
	}
}
