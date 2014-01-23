package com.aero.json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.aero.rsa.RsaKey.PublicKey;
import com.aero.rsamessenger.User;

public abstract class Json
{
	public static String SerializeUserList(List<User> userList)
	{
		// Serialization iþlemi:
		// Nesne >>> String Dönüþümü
		
		JSONArray array = new JSONArray();
		JSONObject arrayObject = new JSONObject();
		
		try
		{
			for(int i = 0; i < userList.size(); i++)
			{
				JSONObject object = new JSONObject();
				object.put("phoneNumber", userList.get(i).phoneNumber);
				object.put("email", userList.get(i).emailAdress);
				object.put("publicKeyN", userList.get(i).publicKey.N().toString());
				object.put("publicKeyE", userList.get(i).publicKey.E().toString());
				
				array.put(object);
			}
			
			// Serializator
			arrayObject.put("Users", array);
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return arrayObject.toString();
	}
	
	public static List<User> DeserializeUserList(String jsonString)
	{
		// Deserialization iþlemi:
		// String >>> Nesne Dönüþümü
		
		// Json nesnesi ve dönüþtürme iþlemi
		List<User> userList = new ArrayList<User>();
		try
		{
			JSONObject json = new JSONObject(jsonString);
			JSONArray jArray = json.getJSONArray("Users");
			for(int i = 0; i < jArray.length(); i++)
			{
				String phoneNumber, email, publicKeyN, publicKeyE;
				phoneNumber = jArray.getJSONObject(i).getString("phoneNumber");
				email = jArray.getJSONObject(i).getString("email");
				publicKeyN = jArray.getJSONObject(i).getString("publicKeyN");
				publicKeyE = jArray.getJSONObject(i).getString("publicKeyE");
				
				userList.add(new User(phoneNumber, email, new PublicKey(new BigInteger(publicKeyN), new BigInteger(publicKeyE))));
			}
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return userList;
	}
	
	public static String ReadFromFile(String path)
	{
		String text = "";
		File file = new File(path);
		BufferedReader br = null;
		
		try
		{
			String line;
			
			br = new BufferedReader(new FileReader(file));
			
			while ((line = br.readLine()) != null)
			{
				text += line;
			}
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (br != null)
					br.close();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
		
		return text;
	}
	
	public static void WriteToFileFile(String content, String path, boolean appendMode)
	{
		BufferedWriter bW = null;
		try
        {
            File file = new File(path);

            bW = new BufferedWriter(new FileWriter(file, appendMode));
            bW.write(content);
            bW.newLine();
            bW.flush();
            
        }
        catch(IOException e)
        {
             e.printStackTrace();
        }
		finally
		{
			try
			{
				bW.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
