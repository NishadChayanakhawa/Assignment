package RegressionTest;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.Parameters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import Commons.HelperClass;
import PageRepository.HomePage;
import PageRepository.SearchResultPage;
import org.testng.Assert;
import java.util.List;
import java.util.ArrayList;
import org.json.JSONObject;
import java.util.Map;
import java.util.HashMap;
public class MobilePriceCheck {
	WebDriver objDriver;
	HomePage objHomePage;
	SearchResultPage objSearchResultPage;
	int iTestCount;
	String strResultLocation;
	String strScreenshotLocation;
	
	@BeforeTest(alwaysRun=true)
	@Parameters({"TestDataPath","ResultLocation","ScreenshotLocation"})
	public void clearOldResults(String strTestDataPath,String strResultLocation,String strScreenshotLocation) {
		this.strResultLocation=strResultLocation;			//set Result location from xml parameter
		this.strScreenshotLocation=strScreenshotLocation;	//set Screenshot location from xml parameter
		HelperClass.strTestDataPath=strTestDataPath;		//set Test Data location from xml parameter
		HelperClass.clearDirectory(strResultLocation);		//clear files from Result location
		HelperClass.clearDirectory(strScreenshotLocation);	//clear files from Screenshot location
		iTestCount=0;										//set test count to 0
	}
	
	@BeforeMethod(alwaysRun=true)
	@Parameters({"URL"})
	public void loadFlipkartPage(String strURL) {
		objDriver=HelperClass.getDriver();					//Get WebDriver
		objHomePage=new HomePage(objDriver,10);				//Instantiate HomePage
		objHomePage.loadHomePage(strURL);					//load url specified in xml parameter
		iTestCount++;										//increment test count
	}
	
	@Test(dataProvider="SearchParameters",
			dataProviderClass=TestData.TestDataRepository.class)
	public void getMobilePriceDetails
	(String strStringToSearch,String strStringToMatch,String strRAM,String strMinimumPrice,String strMaximumPrice,
			ITestContext objTestContext) {
		
		//Set WebDriver attribute.....to be used for screenshot by listener
		objTestContext.setAttribute("WebDriver",objDriver);
		//Set Screenshot anf Result full paths and set as attributes to TestContext
		String strScreenshotFullPath=strScreenshotLocation + "/Test#" + iTestCount + ".png";
		String strResultFullPath=strResultLocation + "/Test#" + iTestCount;
		objTestContext.setAttribute("ScreenshotFullPath",strScreenshotFullPath);
		objTestContext.setAttribute("ResultFullPath",strResultFullPath);
		//Get parameter map
		Map<String,String> objParameterMap=HelperClass.getParameterMap
				(strStringToSearch, strStringToMatch, strRAM, strMinimumPrice, strMaximumPrice);
		List<Map<String,String>> objResultsMap=null;
		//initialize JSON Object
		JSONObject objResult=new JSONObject();
		try {
			//Load home page
			Assert.assertTrue(objHomePage.hasHomePageLoaded(),"Home Page was not loaded");
			
			//Search using auto search functionality
			Boolean searchSuccess=objHomePage.searchByAutoSelect(strStringToSearch,strStringToMatch);
			Assert.assertTrue(searchSuccess, "Search by auto selection failed");
			
			//load Search Result Page
			objSearchResultPage=new SearchResultPage(objDriver,10);
			Boolean searchResultsLoaded=objSearchResultPage.hasSearchResultPageLoaded();
			Assert.assertTrue(searchResultsLoaded, "Search results not loaded");
			
			//Select RAM
			Boolean RAMSelected=objSearchResultPage.selectRAM(strRAM);
			Assert.assertTrue(RAMSelected, "RAM Selection failed");
			
			//Select minimum price range
			Boolean minimumPriceSelected=objSearchResultPage.selectMinimumRange(strMinimumPrice);
			Assert.assertTrue(minimumPriceSelected, "Minimum Price selection failed");
			//Select maximum price range
			Boolean maximumPriceSelected=objSearchResultPage.selectMaximumRange(strMaximumPrice);
			Assert.assertTrue(maximumPriceSelected, "Maximum Price selection failed");
			
			//Get list of mobile names and prices
			List<WebElement> objMobileNames=objSearchResultPage.getMobileNameElements();
			List<WebElement> objMobilePrices=objSearchResultPage.getMobilePriceElements();
			//Get Map of results
			objResultsMap=HelperClass.getResultMap(objMobileNames,objMobilePrices);
		}
		catch(AssertionError e) {
			//In case of error, create map that has error message and StackTrace
			System.out.println(e.getMessage());
			Map<String,String> objErrorMap=new HashMap<String,String> ();
			objErrorMap.put("Error Message",e.getMessage());
			objErrorMap.put("StackTrace",e.getStackTrace().toString());
			objResultsMap=new ArrayList<Map<String,String>>();
			objResultsMap.add(objErrorMap);
			Assert.assertTrue(false);
		}
		finally {
			//Put parameters and results in JSON object
			objResult.put("Parameters", objParameterMap);
			objResult.put("Results", objResultsMap);
			//set JSON object as an attribute to test context......to be used by listener
			objTestContext.setAttribute("ResultJSON",objResult.toString(4));
		}
	}
	
	@AfterMethod
	public void closeBrowser() {
		//close browser
		objDriver.close();
	}
}
