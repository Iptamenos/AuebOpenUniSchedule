package auebunischedule;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
//import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;


public class AUEBDownloader implements ScheduleDownloader {

//	@SuppressWarnings("resource")
	@Override
	public JSONArray downloadSchedule() {
		JSONArray jsonArray = null;
		
        try {
            URL urlObj = new URL("http://schedule.aueb.gr/mobile/index.php");
            HttpURLConnection urlConnection = (HttpURLConnection) urlObj.openConnection();
            
            int statusCode = urlConnection.getResponseCode();
            System.out.println("statusCode: " + statusCode);

            // If downloaded successfully
            if (statusCode == 200) {
                InputStream content = urlConnection.getInputStream();
                
                // convert InputStream to String
                String myString = IOUtils.toString(content, StandardCharsets.UTF_8);
                // System.out.println(myString);

                if (myString.equals("Could not connect to database")) {
                	System.err.println(myString + "!");
                	System.exit(0);
                }
                
                // ALTERNATIVE
//                Scanner s = new Scanner(content).useDelimiter("\\A");
//                String myString = s.hasNext() ? s.next() : "";
                
                jsonArray = new JSONArray(myString);
                
            } else {
            	// Do something else in case of error
            	System.err.println("HTTP error!");
            	System.exit(0);
            }
        } catch (IOException e) {
        	e.printStackTrace();
        } catch (JSONException e) {
        	e.printStackTrace();
        }
        return jsonArray;
	}

	@Override
	public boolean checkForExistingUpdate() {
		// TODO 
		return false;
	}

}
