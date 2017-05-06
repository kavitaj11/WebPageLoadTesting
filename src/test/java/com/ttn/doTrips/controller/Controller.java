package com.ttn.doTrips.controller;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import com.ttn.doTrips.utilities.GlobalVars;
import com.ttn.doTrips.utilities.Keywords;
import com.ttn.doTrips.utilities.Reporter;

import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;

public class Controller {

	public static String driverPath = "/home/ttn/Downloads/";
	public static WebDriver driver;
	public static BrowserMobProxyServer server;
	public static Reporter reporter;
	public static String env = System.getProperty("env");
	public static String ver = System.getProperty("ver");
	
	private String inputFile;

	public static String HARFILE_NAME="PerformanceTestHar.har";
	public static String TEXTFILE_NAME="PerformanceTestHar.txt";
	public static String EXCELFILE_NAME="GA_Excel_Execution.xls";
	
	public void setInputFile(String inputFile) 
	{
		this.inputFile = inputFile;
	}

	
	
	
	
	@BeforeSuite(alwaysRun = true)
	public void beforeSuite() {
		System.out.println("--------------@BeforeSuite--------------");
		Keywords.setURL(env, ver);

		// Report Environment Directory
		try {
			GlobalVars.reportsDirEnv = Reporter.createResultFolderStructure(env);
			System.out.println(">>>>>>>>" + GlobalVars.reportsDirEnv);

			String filPath = GlobalVars.reportsDirEnv + "_Performance_Execution_Report" + ".html";
			reporter = new Reporter(filPath, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@BeforeClass
	public void setup() throws Exception {

		server = new BrowserMobProxyServer();
		server.start();
		int port = server.getPort();
		Proxy proxy = ClientUtil.createSeleniumProxy(server);
		DesiredCapabilities seleniumCapabilities = new DesiredCapabilities();
		seleniumCapabilities.setCapability(CapabilityType.PROXY, proxy);
		driver = new FirefoxDriver(seleniumCapabilities);
		System.out.println("Port started:" + port);

		// Record a HTTP Archive - HAR using BrowserMob Proxy - BMP
		server.newHar("dotrips.har"); // Record a HAR

	}

	@AfterClass
	public void shutdown() {

		// Log the name of the test just run

		try {

			// Get the HAR data
			Har har = server.getHar();
			File harFile = new File("/home/ttn/Workspace/LinkedIn/har/doTrips_test.har");
			har.writeTo(harFile);

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		driver.quit();
		server.stop();
	}
}