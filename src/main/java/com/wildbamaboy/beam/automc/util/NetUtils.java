package com.wildbamaboy.beam.automc.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class NetUtils 
{
	public static JSONArray jsonArrayGET(String url)
	{
		int responseCode = -1;
		
		try
		{
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.addRequestProperty("Content-Type", "application/json");
			connection.setRequestMethod("GET");

			responseCode = connection.getResponseCode();

			if (responseCode == 200) //200 OK
			{
				//Read the response string and return it as a JSON object.
				InputStream in = connection.getInputStream();
				Scanner inScanner = new Scanner(in);
				inScanner.useDelimiter("\\Z");

				String responseString = inScanner.next();

				in.close();
				inScanner.close();

				return new JSONArray(responseString);
			}
		}

		catch (IOException e)
		{
			return new JSONArray().put(0, new JSONObject().put("error", "Unable to connect to Beam authentication service."));
		}

		return new JSONArray().put(0, new JSONObject().put("error", responseCode));
	}
}
