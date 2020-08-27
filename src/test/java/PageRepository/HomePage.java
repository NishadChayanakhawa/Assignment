package PageRepository;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.TimeoutException;

import java.util.List;
import java.util.Properties;
import Commons.HelperClass;

/***************************************************************************************************
 * Class Name: PageRepository.HomePage
 * Description: Home Page repository
 * Methods:
 * 		loadHomePage() - loads home page
 * 		hasHomePageLoaded() - returns true when Home page has loaded
 * 		searchByAutoSelect() - Searches and selects using auto select
 * 		getMatchedAutoSelectLink() - gets matched clickable link
 * Changelog:
 * 	-v1.0 Base code with base functionalities
 * *************************************************************************************************
 */
public class HomePage {
	WebDriver objDriver;
	WebDriverWait objWait;
	Properties objObjectRepository;
	WebElement objSearchTextBox;
	final String strLoginPopupCloseXPath;
	final String strSearchTextBoxXPath;
	final String strAutoSelectOptionsXPath;
	final String strAutoSelectLinksXPath;
	
	public HomePage(WebDriver objDriver,int iWaitTime) {
		this.objDriver=objDriver;
		this.objWait=new WebDriverWait(this.objDriver,iWaitTime);
		objObjectRepository=HelperClass.getObjectRepository();
		strLoginPopupCloseXPath=objObjectRepository.getProperty("LoginPopup.Close");
		strSearchTextBoxXPath=objObjectRepository.getProperty("SearchTextBox");
		strAutoSelectOptionsXPath=objObjectRepository.getProperty("AutoSelectOptions");
		strAutoSelectLinksXPath=objObjectRepository.getProperty("AutoSelectLinks");
	}
	
	/***********************************************************************************************
	 * Method Name: loadHomePage()
	 * Description: Loads home page with strURL as URL
	 * Parameters: String strURL
	 * Returns: Nothing
	 * Throws: Nothing
	 * *********************************************************************************************
	 */
	public void loadHomePage(String strURL) {
		objDriver.get(strURL);	//load page from strURL
		//In case login popup is displayed, close the same
		try {
			WebElement objPopupCloseButton=
					objWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(strLoginPopupCloseXPath)));
			objPopupCloseButton.click();
		}
		catch (TimeoutException e) {
			//Do Nothing if no popup is found
		}
	}
	
	/***********************************************************************************************
	 * Method Name: hasHomePageLoaded()
	 * Description: Returns true if home page is loaded
	 * Parameters: Nothing
	 * Returns: Boolean(true if page is loaded)
	 * Throws: Nothing
	 * *********************************************************************************************
	 */
	public Boolean hasHomePageLoaded() {
		Boolean homePageLoaded;
		try {
			//look for search box and set page loaded to true of the same is found
			objSearchTextBox=
					objWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(strSearchTextBoxXPath)));
			homePageLoaded=true;
		}
		catch (TimeoutException e) {
			//On timeout set page loaded to false
			homePageLoaded=false;
		}
		return homePageLoaded;
	}
	
	/***********************************************************************************************
	 * Method Name: searchByAutoSelect()
	 * Description: Enters strToSearch in search text box. In auto selection, looks for item that
	 * has text strToSelect and clicks on the same
	 * Parameters: 
	 * 	String strToSearch - String to search
	 * 	String strToSelect - String looked up in auto select options
	 * Returns: Boolean(true if search is successful)
	 * Throws: Nothing
	 * *********************************************************************************************
	 */
	public Boolean searchByAutoSelect(String strToSearch,String strToSelect) {
		Boolean searchSuccessful;
		//look for search text box and enter strToSearch 
		objSearchTextBox=
				objDriver.findElement(By.xpath(strSearchTextBoxXPath));
		objSearchTextBox.sendKeys(strToSearch);
		//get element from auto select pane that has text  strToSelect
		WebElement objMatchedAutoSelectLink=getMatchedAutoSelectLink(strToSelect);
		if (objMatchedAutoSelectLink==null) {
			searchSuccessful=false;		//object being null indicates auto select had no match
		}
		else {
			objMatchedAutoSelectLink.click();	//click on auto select link
			searchSuccessful=true;
		}
		return searchSuccessful;
	}
	
	/***********************************************************************************************
	 * Method Name: getMatchedAutoSelectLink()
	 * Description: From auto select pane, returns link element that has text strToSelect
	 * Parameters: 
	 * 	String strToSelect - holds string to look for in auto select
	 * Returns: WebElement as matched Link element
	 * Throws: Nothing
	 * *********************************************************************************************
	 */
	private WebElement getMatchedAutoSelectLink(String strToSelect) {
		WebElement objMatchedAutoSelectLink=null;
		List<WebElement> objAutoSelectOptions;
		List<WebElement> objAutoSelectLinks;
		try {
			//Get text elements from auto select pane
			objAutoSelectOptions=
				objWait.until
				(ExpectedConditions.visibilityOfAllElementsLocatedBy(
						By.xpath(strAutoSelectOptionsXPath)));
			//Get corresponding link elements
			objAutoSelectLinks=
					objWait.until
					(ExpectedConditions.visibilityOfAllElementsLocatedBy
							(By.xpath(strAutoSelectLinksXPath)));
		}
		catch (TimeoutException e) {
			//if no elements are located return null
			return objMatchedAutoSelectLink;
		}
		for (int i=0;i<objAutoSelectOptions.size();i++) {				//for each element
			String strAutoItem=objAutoSelectOptions.get(i).getText();		//get text
			Boolean doesTextMatch=strAutoItem.contains(strToSelect);
			if (doesTextMatch) {											//check if text contains strToSelect
				objMatchedAutoSelectLink=objAutoSelectLinks.get(i);			//return corresponding link
				break;
			}
		}
		return objMatchedAutoSelectLink;
	}
	
}
