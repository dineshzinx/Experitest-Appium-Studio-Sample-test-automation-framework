package Seetest;

import java.io.IOException;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import com.seetest.iospages.SampleiOSPage;
import com.seetest.driverInstance.SeetestCommandBase;


public class SampleiOSTest {
	
	private com.seetest.driverInstance.SeetestCommandBase caller;
	 
	 @BeforeSuite
		public void init(){
	    caller = new SeetestCommandBase("EriBank");
		}
		
		@Test
		public void test1() throws IOException{

	        caller.verifyElementPresent(SampleiOSPage.usernameTextField);
			caller.type(SampleiOSPage.usernameTextField, "company");
			caller.verifyElementPresent(SampleiOSPage.passwordTextField);
			caller.type(SampleiOSPage.passwordTextField, "company");
			caller.verifyElementPresent(SampleiOSPage.login);
			caller.click(SampleiOSPage.login);
			
		}
		
}
