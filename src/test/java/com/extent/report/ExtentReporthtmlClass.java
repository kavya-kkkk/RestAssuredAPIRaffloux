package com.extent.report;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.Utiliies.BaseClass;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class ExtentReporthtmlClass extends BaseClass


{
	
	
	public ExtentReports extent;
	public ExtentTest test;
	
	
	
	
	@BeforeSuite 
public void setUp() {
	ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(REPORTPATHLogin);
 	extent = new ExtentReports();
	extent.attachReporter(htmlReporter);
}


	@BeforeMethod
	public void beforeMethod() {
		test = extent.createTest("Raffloux-ReDev Rest API", "API Testing both Positive and negative");
	}
	
	
	
	@AfterTest
	public void afterMethod() {
		extent.flush();
	}



	@AfterSuite
	public void tearDown() {
		extent.flush();
	}
	
	

	

}
