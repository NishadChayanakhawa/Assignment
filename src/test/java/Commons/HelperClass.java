package Commons;
//import selenium packages
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
//import java utilities
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
//import java io packages for file handling
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
//import apache poi packages for EXCEL handling
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
/***************************************************************************************************
 * Class Name: Commons.Helper Class
 * Description: Provides static methods and static elements to be used by testing stubs.
 * Members:
 * 		strChromeDriverPath - holds relative path to chrome driver
 * 		strObjectRepositoryPath - holds path to Object repository properties file
 * 		strTestDataPath - holds path to TestData
 * Methods:
 * 		getDriver() - Returns WebDriver by instantiating chrome driver
 * 		getObjectRepository() - Returns Object repository as Properties object
 * 		getResultMap() - Returns Map of Mobile name and prices
 * 		getTestData() - Returns test data
 * 		getTestDataFromSheet() - Returns test data from sheet(Private method)
 * 		appendArray() - Returns a new array with additional object appended to original array
 * 		clearDirectory() - Clears Directory
 * 		getParameterMap() - Returns Parameter values in Map format
 * Changelog:
 * 	-v1.0 Base code with base functionalities
 * *************************************************************************************************
 */
public class HelperClass {
	//strChromeDriverPath holds path to Chrome Driver and 
	//strObjectRepositoryPath holds path to Object Repository
	final static String strChromeDriverPath="./src/test/resources/Driver/chromedriver.exe";
	final static String strObjectRepositoryPath=
			"./src/test/resources/TestData/ObjectRepository.properties";
	//strTestDataPath holds path to test data. This member is public and is popuated dynamically
	public static String strTestDataPath;
	
	/***********************************************************************************************
	 * Method Name: getDriver()
	 * Description: Returns ChromeDriver as WebDriver.
	 * Parameters: None
	 * Returns: WebDriver objDriver
	 * Throws: Nothing
	 * *********************************************************************************************
	 */
	public static WebDriver getDriver() {
		//Set "webdriver.chrome.driver" system property
		System.setProperty("webdriver.chrome.driver",strChromeDriverPath);
		//Initialize Chrome Driver and return the same.
		WebDriver objDriver=new ChromeDriver();
		return objDriver;
	}
	
	/***********************************************************************************************
	 * Method Name: getObjectRepository()
	 * Description: Returns Object repository as Properties Object.
	 * Parameters: None
	 * Returns: Properties objObjectRepository
	 * Throws: Nothing
	 * *********************************************************************************************
	 */
	public static Properties getObjectRepository() {
		Properties objObjectRepository=new Properties();				//Initialize Properties
		File objObjectRepositoryFile=new File(strObjectRepositoryPath);	//Load Properties file
		try {
			FileInputStream objObjectRepositoryInputStream=
					new FileInputStream(objObjectRepositoryFile);		//Create stream of file
			objObjectRepository.load(objObjectRepositoryInputStream);	//Load properties
			objObjectRepositoryInputStream.close();						//close the stream
		}
		catch (IOException e) {
			// To be implemented										//Not needed currently
		}
		return objObjectRepository;										//return the properties obj
	}
	
	/***********************************************************************************************
	 * Method Name: getResultMap()
	 * Description: Returns Result map.
	 * Parameters:
	 * 	List<WebElement> objMobileNames - Holds list of objects that have Mobile Names
	 * 	List<WebElement> objMobilePrices - Holds list of objects that have Mobile Prices
	 * Returns: List<Map<String,String>> objResultMaps
	 * for e.g.-
	 * 		[{{MobileName: SamsungMobile1},{Price: 10000}},......]
	 * Throws: Nothing
	 * *********************************************************************************************
	 */
	public static List<Map<String,String>> getResultMap
		(List<WebElement> objMobileNames,List<WebElement> objMobilePrices) {
		//Initialize list of map to be returned
		List<Map<String,String>> objResultMaps=new ArrayList<Map<String,String>>();
		for (int i=0;i<objMobileNames.size();i++) {						//For each element
			Map<String,String> objResultMap=new HashMap<String,String>();	//Create a map
			String strMobileName=objMobileNames.get(i).getText();			//Grab mobile name
			String strMobilePrice=objMobilePrices.get(i).getText();			//Grab mobile price
			objResultMap.put("MobilePrice", strMobilePrice);				//add MobilePrice to map
			objResultMap.put("MobileName", strMobileName);					//add MobileName to map
			objResultMaps.add(objResultMap);								//add Map to list
		}
		return objResultMaps;	//Return the list of maps
	}
	
	/***********************************************************************************************
	 * Method Name: getTestData()
	 * Description: Returns Test data.
	 * Parameters:
	 * 	String strSheetName - holds name of the sheet which has test data
	 * Returns: Object[][] objTestData
	 * Throws: Nothing
	 * *********************************************************************************************
	 */
	public static Object[][] getTestData(String strSheetName) {
		Object[][] objTestData=new Object[0][0];		//Initialize return object
		File objTestDataFile=null;
		FileInputStream objTestDataFileInputStream=null;
		Workbook objTestDataWorkbook=null;
		Sheet objTestDataWorksheet=null;
		try {
			//Open test data file at strTestDataPath and assign to apache poi XSSF workbook
			objTestDataFile=new File(strTestDataPath);
			objTestDataFileInputStream=new FileInputStream(objTestDataFile);
			objTestDataWorkbook=new XSSFWorkbook(objTestDataFileInputStream);
			//Set Missing row policy to return null cells as blank.
			//This is to handle blank cell values as part of valid test data
			objTestDataWorkbook.setMissingCellPolicy(Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
			objTestDataWorksheet=objTestDataWorkbook.getSheet(strSheetName);
			//Pass Sheet object and get test data from private function
			objTestData=getTestDataFromSheet(objTestDataWorksheet);
			//Close workbook and stream
			objTestDataWorkbook.close();
			objTestDataFileInputStream.close();
		}
		catch(IOException e) {
			return null;		//in case of IOException, return null. testng will handle this
		}
		return objTestData;
	}
	
	/***********************************************************************************************
	 * Method Name: getTestDataFromSheet()
	 * Description: Returns Test data.
	 * Parameters:
	 * 	Sheet objTestDataSheet - holds Sheet object(apache poi) that has test data
	 * Returns: Object[][] objTestData
	 * Throws: Nothing
	 * *********************************************************************************************
	 */
	private static Object[][] getTestDataFromSheet(Sheet objTestDataSheet) {
		Object[][] objTestData=new Object[0][0];	//Initialize return array with 0x0 size.
		Object[] objTestDataRow=null;
		//Row lookup starts at 1 as first row is header
		for (int iRow=1;iRow<=objTestDataSheet.getLastRowNum();iRow++) {	//For each excel row
			Row objRow=objTestDataSheet.getRow(iRow);
			objTestDataRow=new Object[0];						//Initialize data row with 0 size
			for (int iCell=0;iCell<objRow.getLastCellNum();iCell++) {		//for each cell
				//Get cell object
				Cell objCell=objRow.getCell(iCell);
				//set initial value as blank
				String strCellValue="";
				if (!(objCell==null || objCell.getCellType()==CellType.BLANK)) {
					//get valid cell value if exists
					strCellValue=objCell.getStringCellValue();
				}
				//append new value to Row array
				objTestDataRow=appendArray(objTestDataRow,strCellValue);
			}
			//append entire row to test data 2D array
			objTestData=appendArray(objTestData,objTestDataRow);
		}
		return objTestData;	//return test data
	}
	
	/***********************************************************************************************
	 * Method Name: appendArray()
	 * Description: Appends another object to original array and returns new array
	 * Parameters:
	 * 	Object[] originalArray - Original array
	 * 	Object itemToAppend - object to append
	 * Returns: Object[] appendedArray
	 * Throws: Nothing
	 * *********************************************************************************************
	 */
	private static Object[] appendArray(Object[] originalArray,Object itemToAppend) {
		//create copy of array with length +1 from original
		Object[] appendedArray=Arrays.copyOf(originalArray, originalArray.length+1);
		//Add object as last array item
		appendedArray[appendedArray.length-1]=itemToAppend;
		//return new array
		return appendedArray;
	}
	
	/***********************************************************************************************
	 * Method Name: appendArray()
	 * Description: Appends another object to original array and returns new array
	 * Parameters:
	 * 	Object[][] originalArray - Original array
	 * 	Object[] itemToAppend - object to append
	 * Returns: Object[] appendedArray
	 * Throws: Nothing
	 * *********************************************************************************************
	 */
	private static Object[][] appendArray(Object[][] originalArray,Object[] itemToAppend) {
		//create copy of array with length +1 from original
		Object[][] appendedArray=Arrays.copyOf(originalArray, originalArray.length+1);
		//Add object as last array item
		appendedArray[appendedArray.length-1]=itemToAppend;
		//return new array
		return appendedArray;
	}
	
	/***********************************************************************************************
	 * Method Name: clearDirectory()
	 * Description: Removes all files from directory
	 * Parameters:
	 * 	String strDirectory - holds relative path to directory
	 * Returns: Nothing
	 * Throws: Nothing
	 * *********************************************************************************************
	 */
	public static void clearDirectory(String strDirectory) {
		File objResultDirectory=new File(strDirectory);			//Fetch directory as file
		for (File objOldFiles:objResultDirectory.listFiles()) {	//for each file in directory
			objOldFiles.delete();									//delete file
		}
	}
	
	/***********************************************************************************************
	 * Method Name: getParameterMap()
	 * Description: Returns map of parameters
	 * Parameters:
	 * 	String strStringToSearch - maps to "Search String"
	 * 	String strStringToMatch  - maps to "Match String"
	 * 	String strRAM 			 - maps to "RAM"
	 * 	String strMinimumPrice 	 - maps to "Minimum Price"
	 * 	String strMaximumPrice	 - maps to "Maximum Price"
	 * Returns: Map<String,String> objParameterMap
	 * Throws: Nothing
	 * *********************************************************************************************
	 */
	public static Map<String,String> getParameterMap
	(String strStringToSearch,String strStringToMatch,String strRAM,String strMinimumPrice,String strMaximumPrice) {
		Map<String,String> objParameterMap=new HashMap<String,String>();
		objParameterMap.put("Search String", strStringToSearch);
		objParameterMap.put("Match String", strStringToMatch);
		objParameterMap.put("RAM", strRAM);
		objParameterMap.put("Minimum Price", strMinimumPrice);
		objParameterMap.put("Maximum Price", strMaximumPrice);
		return objParameterMap;
	}
}
