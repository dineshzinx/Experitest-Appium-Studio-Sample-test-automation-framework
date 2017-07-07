package com.seetest.driverInstance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;
import com.experitest.appium.SeeTestAndroidDriver;
import com.experitest.appium.SeeTestAndroidElement;
import com.experitest.appium.SeeTestIOSDriver;
import com.google.common.collect.Ordering;
import com.seetest.extent.ExtentReporter;
import com.seetest.propertyfilereader.PropertyFileReader;

/**
 * @author VDINESH
 */

public class SeetestCommandBase extends SeetestDriverInstance {

	/** Time out */
	private int timeout;

	/** Retry Count */
	private int retryCount;

	ExtentReporter extent = new ExtentReporter();

	private SoftAssert softAssert = new SoftAssert();

	/** The Constant logger. */
	final static Logger logger = Logger.getLogger("rootLogger");

	/** The Android driver. */
	public SeeTestAndroidDriver<SeeTestAndroidElement> androidDriver;
	
	/** The Android driver. */
	public SeeTestIOSDriver<WebElement> iOSDriver;
	

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public SeetestCommandBase(String Application) {
		super(Application);
		init();
	}

	public void init() {

		PropertyFileReader handler = new PropertyFileReader("properties/Execution.properties");
		setTimeout(Integer.parseInt(handler.getproperty("TIMEOUT")));
		setRetryCount(Integer.parseInt(handler.getproperty("RETRY_COUNT")));
		logger.info("Loaded the following properties" + " TimeOut :" + getTimeout() + " RetryCount :" + getRetryCount());
	}

	/**
	 * @param byLocator
	 * @return
	 */
	private WebElement findElement(By byLocator) {
		WebElement element = (new WebDriverWait(getDriver(), getTimeout())).until(ExpectedConditions.presenceOfElementLocated(byLocator));
		return element;
	}

	/**
	 * Check element present.
	 *
	 * @param byLocator
	 *            the by locator
	 * @return true, if successful
	 */
	public boolean verifyElementPresent(By byLocator) {

		try {
			findElement(byLocator);
			softAssert.assertEquals(findElement(byLocator).isDisplayed(), true, "The element " + byLocator +" "+ "is displayed");
			logger.info("The element " + byLocator +" "+ "is displayed");
			extent.extentLogger("checkElementPresent" , "The element " + byLocator + "is displayed");
			return true;
		} catch (Exception e) {
			softAssert.assertEquals(false, true, "Element" + byLocator +" "+ "is not visible");
			softAssert.assertAll();
			logger.error("Element" + byLocator +" "+ "is not visible");
			extent.extentLogger("checkElementPresent" , "The element " + byLocator + "is not displayed");
			return false;
		}
	}


	
	/**
	 * @param byLocator
	 * @return true or false
	 */
      public boolean checkcondition(By byLocator){
		boolean iselementPresent = false;
		try {
		iselementPresent = getDriver().findElement(byLocator).isDisplayed();
		iselementPresent = true;
		} catch (Exception e) {
		iselementPresent = false;
		} 
		return iselementPresent;
    	}
      
  	/**
  	 * Click on a web element.
  	 * 
  	 * @param byLocator
  	 *            the by locator
  	 * 
  	 */
  	public void click(By byLocator) {

  		try {
            WebElement element = findElement(byLocator);
  			element.click();
  			logger.info("Clicked on the object" + byLocator);
  			extent.extentLogger("click", "Clicked the Web Element" +" "+ element.toString());
  		} catch (Exception e) {
  			logger.error(e);

          }
  	}
  	
  	
	/**
	 * Kill or start an application using activity
	 * 
	 * @param command
	 *           to START or KILL an application
	 * @param activity
	 *           Start an application by passing the activity 
	 */          
	public void adbStartKill(String command , String activity){
		String cmd;
		try {
			if(command.equalsIgnoreCase("START")){
				cmd = "adb shell am start -n" +" "+activity;
				Runtime.getRuntime().exec(cmd);
				logger.info("Started the activity" + cmd);
				extent.extentLogger("adbStart","Started the activity" + cmd);
			}else if (command.equalsIgnoreCase("KILL")) {
				cmd = "adb shell am force-stop"+" "+activity;
				Runtime.getRuntime().exec(cmd);
				logger.info("Executed the App switch");
				extent.extentLogger("adbKill","Executed the App switch");

			}   
		} catch (Exception e) {
			logger.error(e);
		}	   
	}
	
	

    /**
     * @return true if keyboard is displayed
     * @throws IOException
     */
    public boolean checkKeyboardDisplayed() throws IOException{
    boolean mInputShown = false;	
    try {
    String cmd = "adb shell dumpsys input_method | grep mInputShown";
    Process p = Runtime.getRuntime().exec(cmd);
    BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
    String outputText ="";
    while((outputText = br.readLine()) !=null){
    if(!outputText.trim().equals("")){
    String[] output = outputText.split(" "); 
    String[] value = output[output.length-1].split("=");
    String keyFlag = value[1];
    if(keyFlag.equalsIgnoreCase("True")){
    mInputShown = true;
    }
    }
    }
    br.close();
    p.waitFor();
    } catch (Exception e) {
    System.out.println(e);	
	}	
	return mInputShown;
  	}


    
	/**
	 * Closes the Keyboard
	 */
	public void hideKeyboard() {
       try {
			getDriver().hideKeyboard();
            logger.info("Hiding keyboard was Successfull");
			extent.extentLogger("hideKeyboard","Hiding keyboard was Successfull");
		} catch (Exception e) {
			logger.error(e);
		}

	}
	
	
	/**
	 * Type on a web element.
	 * 
	 * @param byLocator
	 *            the by locator
	 * @param text
	 *            the text
	 */
	public void type(By byLocator, String text) {

		try {
			WebElement element = findElement(byLocator);
			element.sendKeys(text);
			logger.info("Typed the value " + text + " in to object " + byLocator);
			extent.extentLogger("type", "Typed the Web Element" +" "+ element.toString());
            } catch (Exception e) {
			logger.error(e);

		}
	}
	
	
	/**
	 * Wait .
	 *
	 * @param x seconds to lock
	 */
	public void Wait(int x) {

		try {
			getDriver().manage().timeouts().implicitlyWait(x, TimeUnit.SECONDS);
			logger.info("Wait for " + x + "seconds");
			extent.extentLogger("Wait","Wait for " + x + "seconds");
            } catch (Exception e) {
			logger.error(e);
		}
	}
	
	
	/**
	 * @param keyevent
	 *        pass the android key event value to perform specific action
	 * 
	 */
	public void adbKeyevents(int keyevent){

		try {
			String cmd = "adb shell input keyevent" +" "+keyevent;
			Runtime.getRuntime().exec(cmd);
			logger.info("Performed the Keyvent" + keyevent);
			extent.extentLogger("adbKeyevent","Performed the Keyvent" + keyevent);
		} catch (Exception e) {
			logger.error(e);
		}

	}
	
	/**
	 * @param byLocator
	 * @returns the list count of the element 
	 */
	public int getCount(By byLocator){

		int count = 0;
		try {		
			count = getDriver().findElements(byLocator).size();
			logger.info("List count for" +" "+ byLocator +" "+ "is"+" "+ count);	
			extent.extentLogger("getCount","List count for" +" "+ byLocator +" "+ "is"+" "+ count);
		} catch (Exception e) {
			logger.error(e);	
		}
		return count;
	}

    /**
    * @param i
    * @param byLocator
    * @returns the By locator
    */
  public String iterativeXpathtoStringGenerator(int temp, By byLocator , String property){
 	 
 	 WebElement element =null;
 	 String drug = null;
		try {
			
			String xpath = byLocator.toString();
			String var = "'"+temp+"'";
		    xpath = xpath.replaceAll("__placeholder", var);
		    String[] test = xpath.split(": ");
		    xpath = test[1];
		    element = getDriver().findElement(By.xpath(xpath));
		    drug = element.getAttribute(property);
		} catch (Exception e) {
			System.out.println(e);
		}
		return drug;
	}
  
  
	/**
	 * Back
	 */
	public void Back(int x) {

		try {

			for(int i = 0; i <x; i++){
				getDriver().navigate().back();	
				logger.info("Back button is tapped");
				extent.extentLogger("Back","Back button is tapped");
			}
         } catch (Exception e) {
			logger.error(e);
		}
	}

	
	/**
	 * Finding the duplicate elements in the list
	 * @param mono
	 * @param content
	 * @param dosechang
	 * @param enteral
	 */
	public List<String> findDuplicateElements(List<String> mono){

		List<String> duplicate = new ArrayList<String>();
		Set<String> s = new HashSet<String>();
		try {
            if(mono.size()>0){
                  for(String content : mono){
                       if(s.add(content) == false){
						int i=1;
						duplicate.add(content);
						System.out.println("List of duplicate elements is" + i + content );
						i++;
					} 
				} 
			}
         } catch (Exception e) {
			System.out.println(e);
		}
		return duplicate;
	}
	
	/**
	 * @param contents
	 * @return the list without duplicate elements
	 */
	public List<String> removeDuplicateElements(List<String> contents){
		
		 LinkedHashSet<String> set = new LinkedHashSet<String>(contents);
		 ArrayList<String> listWithoutDuplicateElements = new ArrayList<String>();
		 try {
			 
			 if(contents.size()>0){
				 listWithoutDuplicateElements = new ArrayList<String>(set);
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
		return listWithoutDuplicateElements;
	}
	
	
    /**
    * @param i
    * @param byLocator
    */
   public void iteratorClick(int temp, By byLocator){
		
		try {
		    String xpath = byLocator.toString();
		    String var = "'"+temp+"'";
		    xpath =  xpath.replaceAll("__placeholder", var);
		    String[] test = xpath.split(": ");
		    xpath = test[1];
		    getDriver().findElement(By.xpath(xpath)).click();
		    } catch (Exception e) {
			System.out.println(e);
		}
	}
   
   
	/**
	 * get specific property value of a web element and stores to string
	 * variable.
	 * 
	 * @param property
	 *            the property of the element.
	 * @param byLocator
	 *            the by locator
	 * @return value of the property.
	 */
	public String getElementPropertyToString(String property, By byLocator) {
		String propertyValue = null;
		try {
			WebElement element = findElement(byLocator);
			propertyValue = element.getAttribute(property);
			logger.info("Stored the property value of the object " + byLocator
					+ " property :" + property + "value : " + propertyValue);
			extent.extentLogger("getElementPropertyToString","Stored the property value of the object " +" "+ element.toString());
		} catch (Exception e) {
			logger.error(e);
		}
		return propertyValue;
	}
	
	
	/**
	 * @param sorted
	 * @return true if the list is sorted
	 * @return false if the list is not sorted
	 */
	public boolean checkListIsSorted(List<String> ListToSort){
		
		boolean isSorted = false ;
		
		if(ListToSort.size()>0){
			try {
				if(Ordering.natural().isOrdered(ListToSort)){
					extent.extentLogger("Check sorting", "List is sorted");
					logger.info("List is sorted");
					isSorted = true;
				    return isSorted;	
				}else{
					extent.extentLogger("Check sorting", ListToSort +" "+"List is not sorted");
					logger.info(ListToSort + "List is notsorted");
					isSorted = false;
				}
			} catch (Exception e) {
			     System.out.println(e);
			}
		} else {
			logger.info("The size of the list is zero");
			extent.extentLogger("", ListToSort +" "+"There are no elements in the list to check the sort order");
		}
		return isSorted;
}
	
 	/**
 	 * @param byLocator
 	 * @returns the list count of the element 
 	 */
 	public int iterativeGetCount(int temp, By byLocator){

 		int count = 0;
 		try {
 			
 			String xpath = byLocator.toString();
		    String var = "'"+temp+"'";
		    xpath =  xpath.replaceAll("__placeholder", var);
		    String[] test = xpath.split(": ");
		    xpath = test[1];
 			count = getDriver().findElements(By.xpath(xpath)).size();
 			logger.info("List count for" +" "+ xpath +" "+ "is"+" "+ count);	
 			extent.extentLogger("getCount","List count for" +" "+ xpath +" "+ "is"+" "+ count);
 		} catch (Exception e) {
 			logger.error(e);	
 		}
 		return count;
 	}
 	
 	
    /**
    * @param temp
    * @param byLocator
    * @return
    */
   public By iterativeXpathText(String temp, By byLocator){
   	 
   	 By searchResultList = null ;
   	 
		try {
			
			String xpath = byLocator.toString();
			String var = "'"+temp+"'";
		    xpath = xpath.replaceAll("__placeholder", var);
		    String[] test = xpath.split(": ");
		    xpath = test[1];
		    searchResultList = By.xpath(xpath);
		} catch (Exception e) {
			System.out.println(e);
		}
		return searchResultList;
	}
     
   
   /**
    * @param byLocator
    *        Checks whether element is not displayed
    */
   public void checkElementNotPresent(By byLocator){
   	boolean isElementNotPresent = true;
   	 try {
   	  isElementNotPresent = checkcondition(byLocator);
   	  softAssert.assertEquals(isElementNotPresent, false);
         logger.info("The element " + byLocator +" "+ "is not displayed");
		  extent.extentLogger("checkElementNotPresent" , "The element " + byLocator + "is not displayed");
		  } catch (Exception e) {
		   softAssert.assertEquals(isElementNotPresent, true, "Element" + byLocator +" "+ "is visible");
		   softAssert.assertAll();
		   logger.error("Element" + byLocator +" "+ "is visible");
		   extent.extentLogger("checkElementNotPresent" , "The element " + byLocator + "is displayed");
		}
   }
   
   
   /**
	 * Swipes the screen in left or right or Up or Down or direction
	 * 
	 * @param direction
	 *            to swipe Left or Right or Up or Down
	 * @param count
	 *            to swipe
	 */
	public void Swipe(String direction, int count) {

		String dire = direction;

		try {

			if (dire.equalsIgnoreCase("LEFT")) {

				for (int i = 0; i < count; i++) {
					Dimension size = getDriver().manage()
							.window().getSize();
					int startx = (int) (size.width * 0.8);
					int endx = (int) (size.width * 0.20);
					int starty = size.height / 2;
					getDriver().swipe(startx, starty, endx, starty, 1000);
					logger.info("Swiping the screen in " +" "+ dire
							+ "direction for" +" "+ count);
					extent.extentLogger("SwipeLeft","Swiping the screen in " +" "+ dire
							+ "direction for" +" "+ count);
				}
			} else if (dire.equalsIgnoreCase("RIGHT")) {

				for (int j = 0; j < count; j++) {
				     Dimension size = getDriver().manage()
							.window().getSize();
					int endx = (int) (size.width * 0.8);
					int startx = (int) (size.width * 0.20);
					int starty = size.height / 2;
					getDriver().swipe(startx, starty, endx, starty, 1000);
					logger.info("Swiping the screen in " +" "+ dire+ "direction for" +" "+ count);
					extent.extentLogger("SwipeRight","Swiping the screen in " +" "+ dire+ "direction for" +" "+ count);
				}
			} else if(dire.equalsIgnoreCase("UP")){

				for (int j = 0; j < count; j++) {
				  Dimension size = getDriver().manage().window().getSize();
				  int starty = (int) (size.height * 0.80);
				  int endy = (int) (size.height * 0.20);
				  int startx = size.width / 2;
				getDriver().swipe(startx, starty, startx, endy, 3000);
				logger.info("Swiping the screen in " +" "+ dire + "direction for" +" "+ count);
				extent.extentLogger("SwipeRight","Swiping the screen in " +" "+ dire + "direction for" +" "+ count);
			
				}
			} else if(dire.equalsIgnoreCase("DOWN")){
				for (int j = 0; j < count; j++) {
				   Dimension size = getDriver().manage().window().getSize();
				  int starty = (int) (size.height * 0.80);
				  int endy = (int) (size.height * 0.20);
				  int startx = size.width / 2;
				getDriver().swipe(startx, endy, startx, starty, 3000);
				logger.info("Swiping the screen in " +" "+ dire+ "direction for" +" "+ count);
				extent.extentLogger("SwipeRight","Swiping the screen in " +" "+ dire + "direction for" +" "+ count);
			
			}
		}

		} catch (Exception e) {
			logger.error(e);

		}

	}

	
    
        /**
         * @param bundleID
         */
      public void launchiOSApp(String bundleID){
	
	     try {
	    	iOSDriver = (SeeTestIOSDriver<WebElement>)getDriver();
		    iOSDriver.launchApp(bundleID);
		    logger.info("Started the bundle id" +" "+bundleID);
		    extent.extentLogger("Started the bundle id" +" "+bundleID, "Started the bundle id" +" "+bundleID);
	       } catch (Exception e) {
		    logger.info("Unable to Start the bundle id" +" "+bundleID);
		    extent.extentLogger("Unable to Start the bundle id" +" "+bundleID, "Unable to Start the bundle id" +" "+bundleID);
	     }
     }

      
    /**
     * Closes the iOS keyboard
     */
    public void closeIosKeyboard() {

    	try {
    		iOSDriver = (SeeTestIOSDriver<WebElement>)getDriver();
    		iOSDriver.closeKeyboard();
    		extent.extentLogger("Hiding keyboard successful", "Hiding keyboard successful");
		} catch (Exception e) {
			extent.extentLogger("Hiding keyboard not successful", "Hiding keyboard not successful");
		}
    }
    
    /**
     * closes the appliaction
     */
    public void closeiOSApp() {
    	try {
           iOSDriver = (SeeTestIOSDriver<WebElement>) getDriver();
           iOSDriver.closeApp();
           extent.extentLogger("Killed the appliaction successfully", "Killed the appliaction successfully");
		} catch (Exception e) {
			extent.extentLogger("Unable to Kill the application", "Unable to Kill the application");

		}
    }
    

}
