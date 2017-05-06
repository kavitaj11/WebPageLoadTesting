package com.ttn.doTrips.utilities;

import java.io.File;
import java.io.IOException;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.ExtentTestInterruptedException;
import com.ttn.doTrips.controller.Controller;

public class Reporter {

	public ExtentReports extent;
	public ExtentTest test;
	public ExtentTestInterruptedException testexception;

	public ExtentReports getExtent() {
		return extent;
	}

	public ExtentTest getTest() {
		return test;
	}

	public void setExtent(ExtentReports extent) {
		this.extent = extent;
	}

	public void setTest(ExtentTest test) {
		this.test = test;
	}

	public Reporter(String filePath,boolean replaceExisting) throws Exception {
		extent = new ExtentReports(filePath, replaceExisting);
		extent.loadConfig(new File(GlobalVars.EXTENT_CONFIG_FILE));
		extent.addSystemInfo("Environment",Controller.env);
	}

	public void startTest(String testName, String description) {
		test = extent.startTest(testName, description);
	}

	/*public void assignCategory(String env, String ver) {
		test.assignCategory("Sanity  :: " + env + " :: API VERSION - "+ ver);
	}*/
	
	public void assignCategory(String env, String ver) {
		test.assignCategory(env + ver);
	}
	
	public void endTest() {
		extent.endTest(test);
		extent.flush();
	}

	public void close() {
		extent.close();
	}

	/*public static void main(String[] args) {
		System.out.println(FileSystemView.getFileSystemView().getHomeDirectory());
		System.out.println(System.getProperty("java.io.tmpdir"));
		System.out.println(System.getProperty("user.dir"));
	}*/
	public static String createResultFolderStructure(String environment) throws Exception {
		/**
		 * assign rootFolder based on OS (Windows/Linux)
		 */
		GlobalVars.REPORT_ROOT_FOLDER = System.getProperty("user.dir");

		String reportsRootFolder = GlobalVars.REPORT_ROOT_FOLDER;
		/*for(File drive:File.listRoots()){
			if(drive.getPath().contains("c")){
				reportsRootFolder = drive.getPath();
				break;
			}
		}*/
		//File reportsDir = new File(new File(System.getProperty("user.dir")).getParent(), "FameReports");
		File reportsParentDir = new File(reportsRootFolder, GlobalVars.REPORT_FOLDER_ROOT_NAME);

		//Remove REPORT_FOLDER_ROOT_NAME
		if(!reportsParentDir.exists()){
			System.out.println("Directory does not exist.");
		}else{
			try {
				DeleteDir(reportsParentDir);
			} catch (IOException e) {
				throw new Exception("Unable to Delete dir " + reportsParentDir);
			}
		}

		return CreateDir(reportsParentDir);
	}

	public static String CreateDir(File reportsParentDir) {
		/**
		 * Create parent dir and then sub-folder $ENV
		 */
		System.out.println("Directory does not exist : " + reportsParentDir );
		File reportsDir = null;
		if(reportsParentDir.mkdirs()){
			
			//reportsDir = new File(reportsParentDir);

			//reportsDir.mkdirs();
			//System.out.println("Creating directory : " + reportsDir.getAbsolutePath());
		}
		return reportsParentDir.getAbsolutePath();
	}

	public static String setupReports(String reportsDir, String environmentName) {
		File reportTemp = new File(reportsDir, environmentName);
		reportTemp.mkdirs();
		return reportTemp.getAbsolutePath();
	}

	private static void DeleteDir(File file) throws IOException{
		if(file.isDirectory()){

			//directory is empty, then delete it
			if(file.list().length==0){

				file.delete();
				System.out.println("Deleting Directory : " 
						+ file.getAbsolutePath());

			}else{

				//list all the directory contents
				String files[] = file.list();

				for (String temp : files) {
					//construct the file structure
					File fileDelete = new File(file, temp);

					//recursive delete
					DeleteDir(fileDelete);
				}

				//check the directory again, if empty then delete it
				if(file.list().length==0){
					file.delete();
					System.out.println("Directory is deleted : " 
							+ file.getAbsolutePath());
				}
			}

		}else{
			//if file, then delete it
			file.delete();
			//			System.out.println("File is deleted : " + file.getAbsolutePath());
		}
	}
}
