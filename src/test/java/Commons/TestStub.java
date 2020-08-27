package Commons;
import Commons.HelperClass;
import org.openqa.selenium.WebDriver;
import java.util.Properties;
import PageRepository.HomePage;
import PageRepository.SearchResultPage;
public class TestStub {
	public static void main(String[] args) {
		/*
		WebDriver objDriver=HelperClass.getDriver();
		HomePage objHomePage=new HomePage(objDriver,10);
		objHomePage.loadHomePage("https://www.flipkart.com/");
		System.out.println(objHomePage.hasHomePageLoaded());
		System.out.println(objHomePage.searchByAutoSelect("samsung","samsung mobiles"));
		
		SearchResultPage objResultPage=new SearchResultPage(objDriver,10);
		System.out.println(objResultPage.hasSearchResultPageLoaded());
		System.out.println(objResultPage.selectRAM("3 GB"));
		System.out.println(objResultPage.selectMinimumRange("₹2000"));
		System.out.println(objResultPage.selectMaximumRange("₹10000"));
		*/
		Object[][] objData=HelperClass.getTestData("getMobilePriceDetails");
		for (Object[] row: objData) {
			for (Object cell: row) {
				System.out.print("["+cell+"]");
			}
			System.out.println();
		}
		//objDriver.close();
	}
}
