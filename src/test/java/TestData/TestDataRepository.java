package TestData;
import java.lang.reflect.Method;
import org.testng.annotations.DataProvider;
import Commons.HelperClass;
public class TestDataRepository {
	@DataProvider(name="SearchParameters")
	public static Object[][] getData(Method M) {
		Object[][] objTestData=HelperClass.getTestData(M.getName());
		return objTestData;
	}
}
