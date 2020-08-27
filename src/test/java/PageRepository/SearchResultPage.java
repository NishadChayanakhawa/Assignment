package PageRepository;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.NoSuchElementException;
import java.util.Properties;
import Commons.HelperClass;
import CustomOptions.WaitForSelectedValue;
import java.util.List;

/***************************************************************************************************
 * Class Name: PageRepository.SearchResultPage
 * Description: Search result Page repository
 * Methods:
 * 		hasSearchResultPageLoaded() - returns true if search page is loaded
 * 		selectRAM() - selects RAM
 * 		selectMinimumRange() - select minimum price range
 * 		selectMaximumRange() - select maximum price range
 * 		getMobileNameElements() - Returns elements that have mobile names
 * 		getMobilePriceElements() - Returns elements that have mobile prices
 * Changelog:
 * 	-v1.0 Base code with base functionalities
 * *************************************************************************************************
 */
public class SearchResultPage {
	WebDriver objDriver;
	WebDriverWait objWait;
	Properties objObjectRepository;
	final String strRAMXPath;
	final String strRAMAjaxWait;
	final String strPriceRangeXPath;
	final String strSearchResultDetailXPath;
	final String strMobileNamesXPath;
	final String strMobilePricesXPath;
	
	public SearchResultPage(WebDriver objDriver,int iWaitTime) {
		this.objDriver=objDriver;
		this.objWait=new WebDriverWait(objDriver,iWaitTime);
		this.objObjectRepository=HelperClass.getObjectRepository();
		strRAMXPath=objObjectRepository.getProperty("RAM");
		strRAMAjaxWait=objObjectRepository.getProperty("RAM.AJAXWait");
		strPriceRangeXPath=objObjectRepository.getProperty("PriceRange");
		strSearchResultDetailXPath=objObjectRepository.getProperty("SearchResultDetail");
		strMobileNamesXPath=objObjectRepository.getProperty("MobileNames");
		strMobilePricesXPath=objObjectRepository.getProperty("MobilePrices");
	}
	
	/***********************************************************************************************
	 * Method Name: hasSearchResultPageLoaded()
	 * Description: Returns true if search page is loaded
	 * Parameters: None
	 * Returns: Boolean(true if page is loaded)
	 * Throws: Nothing
	 * *********************************************************************************************
	 */
	public Boolean hasSearchResultPageLoaded() {
		Boolean SearchResultPageLoaded;
		try {
			//Check if search results are loaded and visible. Set return value to true once complete.
			objWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(strSearchResultDetailXPath)));
			SearchResultPageLoaded=true;
		}
		catch (TimeoutException e) {
			//set return value as false if results are not visible
			SearchResultPageLoaded=false;		
		}
		return SearchResultPageLoaded;
	}
	
	/***********************************************************************************************
	 * Method Name: selectRAM()
	 * Description: Selects RAM checkbox
	 * Parameters: strRAMSize - holds RAM value to select
	 * Returns: Boolean(true if RAM is selected)
	 * Throws: Nothing
	 * *********************************************************************************************
	 */
	public Boolean selectRAM(String strRAMSize) {
		Boolean ramSelected;
		WebElement objRAM;
		//get xpath that corresponds to RAM value provided
		String strCustomRAMXPath=strRAMXPath.replace("<RAM_SIZE>", strRAMSize);
		
		try {
			//locate the element
			objRAM=objDriver.findElement(By.xpath(strCustomRAMXPath));
		}
		catch (NoSuchElementException e) {
			//return false if element is not found
			return false;
		}
		//click on elemet to select the checkbox
		objRAM.click();
		try {
			//wait until 'Clear All' is visible which indicates AJAX call has concluded
			objWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(strRAMAjaxWait)));
			ramSelected=true;
		}
		catch (TimeoutException e) {
			//return false if AJAX call fails
			ramSelected=false;
		}
		return ramSelected;
	}
	
	/***********************************************************************************************
	 * Method Name: selectMinimumRange()
	 * Description: Selects minimum range value
	 * Parameters: strVisibleTextToSelect - holds minimum price range to select
	 * Returns: Boolean(true if range is selected)
	 * Throws: Nothing
	 * *********************************************************************************************
	 */
	public Boolean selectMinimumRange(String strVisibleTextToSelect) {
		Boolean minimumRangeSelected;
		//Grab minimum price range element and select value
		Select objMinimum=new Select(objDriver.findElements(By.xpath(strPriceRangeXPath)).get(0));
		objMinimum.selectByVisibleText(strVisibleTextToSelect);
		try {
			//wait until value gets selected post AJAX call
			objWait.until(new WaitForSelectedValue(objMinimum,strVisibleTextToSelect));
			minimumRangeSelected=true;
		}
		catch (TimeoutException e) {
			//return false if AJAX fails
			minimumRangeSelected=false;
		}
		return minimumRangeSelected;
	}
	
	/***********************************************************************************************
	 * Method Name: selectMaximumRange()
	 * Description: Selects maximum range value
	 * Parameters: strVisibleTextToSelect - holds minimum price range to select
	 * Returns: Boolean(true if range is selected)
	 * Throws: Nothing
	 * *********************************************************************************************
	 */
	public Boolean selectMaximumRange(String strVisibleTextToSelect) {
		Boolean maximumRangeSelected;
		//grab maximum price range element and select value
		Select objMaximum=new Select(objDriver.findElements(By.xpath(strPriceRangeXPath)).get(1));
		objMaximum.selectByVisibleText(strVisibleTextToSelect);
		try {
			//wait for value to be selected post AJAX call
			objWait.until(new WaitForSelectedValue(objMaximum,strVisibleTextToSelect));
			maximumRangeSelected=true;
		}
		catch (TimeoutException e) {
			//return false if AJAX call fails
			maximumRangeSelected=false;
		}
		return maximumRangeSelected;
	}
	
	public List<WebElement> getMobileNameElements() {
		//find Mobile name elements and return the same
		List<WebElement> objMobileNames=
				objDriver.findElements(By.xpath(strMobileNamesXPath));
		return objMobileNames;
	}
	
	public List<WebElement> getMobilePriceElements() {
		//find Mobile price elements and return the same
		List<WebElement> objMobilePrices=
				objDriver.findElements(By.xpath(strMobilePricesXPath));
		return objMobilePrices;
	}
	
}
