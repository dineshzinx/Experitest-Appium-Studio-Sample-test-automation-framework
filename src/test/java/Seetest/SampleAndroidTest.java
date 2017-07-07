package Seetest;

import java.io.IOException;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import com.seetest.androidpages.SampleAndroidPage;
import com.seetest.driverInstance.SeetestCommandBase;


public class SampleAndroidTest {
	
private com.seetest.driverInstance.SeetestCommandBase caller;
 
    @BeforeSuite
	public void init(){
    caller = new SeetestCommandBase("EriBank");
	}
	
	@Test
	public void test1() throws IOException{

        caller.verifyElementPresent(SampleAndroidPage.usernameTextField);
		caller.type(SampleAndroidPage.usernameTextField, "company");
		caller.verifyElementPresent(SampleAndroidPage.passwordTextField);
		caller.type(SampleAndroidPage.passwordTextField, "company");
		caller.verifyElementPresent(SampleAndroidPage.login);
		caller.click(SampleAndroidPage.login);
		
	}
	
	@Test
	public void test2() throws IOException{
		  caller.verifyElementPresent(SampleAndroidPage.test);
            
	}
}
