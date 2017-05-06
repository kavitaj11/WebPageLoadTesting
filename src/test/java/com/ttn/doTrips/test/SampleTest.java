package com.ttn.doTrips.test;

import org.testng.annotations.Test;

import com.ttn.doTrips.controller.Controller;

public class SampleTest extends Controller{

	
 
	@Test
	public void teknosa_test1() throws InterruptedException {
  
		driver.get("https://staff.dotrips.com");
		driver.manage().window().maximize();
		Thread.sleep(15000);
	}

	@Test
	public void teknosa_test2() throws InterruptedException {
 
		driver.get("https://staff.dotrips.com/en/forgotPassword");
		driver.manage().window().maximize();
		Thread.sleep(15000);
	}
	
	
}