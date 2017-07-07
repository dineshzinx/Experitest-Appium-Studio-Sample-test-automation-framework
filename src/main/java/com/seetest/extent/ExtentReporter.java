package com.seetest.extent;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.seetest.driverInstance.SeetestDriverInstance;

import io.appium.java_client.AppiumDriver;

public class ExtentReporter implements ITestListener {

	private static ExtentReports extent;
	private static ExtentTest test;
	private static String time = getDate();
	public AppiumDriver<WebElement> driver = null;
	public String report;

	/**
	 * @return the date
	 */
	public static String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String name = dateFormat.format(date).toString().replaceFirst(" ", "_").replaceAll("/", "_").replaceAll(":",
				"_");
		return name;
	}


	public void onTestStart(ITestResult result) {
		test = extent.startTest(result.getMethod().getMethodName());
		test.log(LogStatus.INFO, result.getMethod().getMethodName());
	}


	public void onTestSuccess(ITestResult result) {
		test.log(LogStatus.PASS, result.getMethod().getMethodName());
	}


	public void onTestFailure(ITestResult result) {
		String scrnshot = result.getName();
		try {
			screencapture(scrnshot);
			test.log(LogStatus.FAIL, result.getThrowable());
		} catch (Exception e) {
			System.out.println(e);
		}
	}


	public void onTestSkipped(ITestResult result) {
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
	}


	public void onStart(ITestContext context) {
		try {
			report = context.getName();
			extent = new ExtentReports(System.getProperty("user.dir") + "/reports" + "/" + report + "/" + "/" + report
					+ "_" + time + "/" + report + "_" + time + ".html", true);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public AppiumDriver<WebElement> getDriver() {
		if (driver == null) {
			driver = SeetestDriverInstance.getDriver();
		}
		return driver;
	}

	public void onFinish(ITestContext context) {
		extent.endTest(test);
		extent.flush();
	}

	/**
	 * @param stepName
	 * @param details
	 */
	public void extentLogger(String stepName, String details) {
		test.log(LogStatus.INFO, stepName, details);
	}

	/**
	 * @param testname
	 * @throws IOException
	 */
	public void screencapture(String testname) throws IOException {
		try {
			File scrfile = ((TakesScreenshot) this.getDriver()).getScreenshotAs(org.openqa.selenium.OutputType.FILE);
			org.apache.commons.io.FileUtils.copyFile(scrfile,
					new File(System.getProperty("user.dir") + "/reports" + "/" + report + "/" + "/" + report + "_"
							+ time + "/" + "/Screenshots/" + testname + "_" + time + ".jpg"));
			test.log(LogStatus.INFO, test.addScreenCapture(System.getProperty("user.dir") + "/reports" + "/" + report
					+ "/" + report + "_" + time + "/Screenshots/" + testname + "_" + time + ".jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}