package CustomOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.WebDriver;

/***************************************************************************************************
 * Class Name: CustomOptions.WaitForSelectedValue
 * Description: Custom Expected Condition that waits for Select object to have certain value 
 * This is created to handle AJAX wait after selecting price range.
 * Changelog:
 * 	-v1.0 Base code with base functionalities
 * *************************************************************************************************
 */
public class WaitForSelectedValue implements ExpectedCondition<Boolean> {
	Select objSelect;
	String strSelectedValue;
	
	public WaitForSelectedValue(Select objSelect,String strSelectedValue) {
		this.objSelect=objSelect;
		this.strSelectedValue=strSelectedValue;
	}
	
	public Boolean apply(WebDriver objDriver) {
		Boolean isValuePresent;
		String strSelectedText=this.objSelect.getFirstSelectedOption().getText();
		if (strSelectedText.equals(this.strSelectedValue)) {
			isValuePresent=true;
		}
		else {
			isValuePresent=false;
		}
		return isValuePresent;
	}
}
