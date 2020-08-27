package CustomOptions;
//import packages for listener
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.openqa.selenium.OutputType;
//import package for screenshot
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
//import packages for file handling
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

/***************************************************************************************************
 * Class Name: CustomOptions.TestResultListener
 * Description: Provides Listeners for taking screenshots and saving test results
 * Members:
 * 		None
 * Methods:
 * 		onTestSuccess() - executes when test is successful
 * 		onTestFailure() - executes when test has failed
 * 		takeScreenshot() - takes screenshot
 * 		writeResults() - writes test result
 * Changelog:
 * 	-v1.0 Base code with base functionalities
 * *************************************************************************************************
 */
public class TestResultListener implements ITestListener{

	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub
		
	}
	
	/***********************************************************************************************
	 * Method Name: onTestSuccess()
	 * Description: takes screenshot and saves result on test pass
	 * Parameters:
	 * 	ITestResult result - passed automatically by testng
	 * Returns: Nothing
	 * Throws: Nothing
	 * *********************************************************************************************
	 */
	public void onTestSuccess(ITestResult result) {
		ITestContext objTestContext=result.getTestContext();	//get test context
		takeScreenshot(objTestContext);							//call method to take screenshot
		writeResults(objTestContext);							//call method to save result
	}
	
	/***********************************************************************************************
	 * Method Name: onTestFailure()
	 * Description: takes screenshot and saves result on test failure
	 * Parameters:
	 * 	ITestResult result - passed automatically by testng
	 * Returns: Nothing
	 * Throws: Nothing
	 * *********************************************************************************************
	 */
	public void onTestFailure(ITestResult result) {
		ITestContext objTestContext=result.getTestContext();
		takeScreenshot(objTestContext);
		writeResults(objTestContext);
	}

	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		
	}

	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub		
	}
	
	/***********************************************************************************************
	 * Method Name: takeScreenshot()
	 * Description: takes screenshot
	 * Parameters:
	 * 	ITestContext objTestContext
	 * Returns: Nothing
	 * Throws: Nothing
	 * *********************************************************************************************
	 */
	private void takeScreenshot(ITestContext objTestContext) {
		//Grab WebDriver and cast as TakesScreenshot
		//Also, grab screenshot full path attribute from test context
		WebDriver objDriver=(WebDriver)objTestContext.getAttribute("WebDriver");
		String strScreenshotFullPath=(String)objTestContext.getAttribute("ScreenshotFullPath");		
		//take screenshot
		File objScreenshotFile=((TakesScreenshot)objDriver).getScreenshotAs(OutputType.FILE);
		try {
			//save at location grabbed earlier
			FileUtils.copyFile(objScreenshotFile, new File(strScreenshotFullPath));
		}
		catch(IOException e) {
			//To Be implemented
		}
	}
	
	/***********************************************************************************************
	 * Method Name: writeResults()
	 * Description: writes test result
	 * Parameters:
	 * 	ITestContext objTestContext
	 * Returns: Nothing
	 * Throws: Nothing
	 * *********************************************************************************************
	 */
	private void writeResults(ITestContext objTestContext) {
		//grab test result full path and Result in JSON format
		String strResultFullPath=(String)objTestContext.getAttribute("ResultFullPath");
		String strResultToBeSaved=(String)objTestContext.getAttribute("ResultJSON");
		try {
			//write JSON result at location grabbed earlier
			FileWriter objWriter=new FileWriter(strResultFullPath);
			objWriter.write(strResultToBeSaved);
			objWriter.close();
		}
		catch(IOException e) {
			//To Be implemented
		}
	}
	
}
