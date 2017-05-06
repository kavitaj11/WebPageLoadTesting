package com.ttn.doTrips.utilities;

import java.io.FileReader;
import java.util.Properties;


public class Keywords{
	

	public static void setURL(String env, String ver) {
		
		
		Properties properties = new Properties();
		String fileName = "application-" + env + ".properties";
		try {
			FileReader reader = new FileReader(fileName);
			properties.load(reader);
			
			GlobalVars.AUTH_URL = properties.getProperty("AUTH_URL");
			GlobalVars.BASE_URL = properties.getProperty("BASE_URL");
			GlobalVars.LOGIN_EMAIL = properties.getProperty("LOGIN_EMAIL");
			GlobalVars.LOGIN_SIGNUP_PASSWORD_123456 = properties.getProperty("LOGIN_SIGNUP_PASSWORD_123456");
				
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}