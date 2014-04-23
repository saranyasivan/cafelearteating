package com.cafe.util;



import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import java.awt.AWTException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.Reporter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.log4j.Logger;
import com.thoughtworks.selenium.*;





public class TestUtility extends SeleneseTestNgHelper{

	public static DesiredCapabilities capabilities = new DesiredCapabilities();
    public static Selenium driver = null;
	public static String baseUrl;
    public static Logger APPLICATION_LOGS = Logger.getLogger("cafeLearn");
	public static StringBuffer verificationErrors = new StringBuffer();
	public static int sleeptime = 2000;
	public static String uniqueEntry = null;
	public static WebDriverWait wait = null;
	static String tname = null;
	public static String a = " ";
	public static String RANDOM_TIMESTAMP = getCurrentTimeStamp();
	public static String dateTime = RANDOM_TIMESTAMP;
	public static String ac = " ";
	public static String StoredValue = "1";
	public static String SchoolId = "0";

	
	
	
	public static void changeTimeStamp() {
	RANDOM_TIMESTAMP = getCurrentTimeStamp();
	dateTime = RANDOM_TIMESTAMP;}
	
	public static String getCurrentTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("ddMMyyhhmmss");// dd/MM/yy
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}
	
	public static LinkedList<TestStep> getStepsForSuite(String suiteName) {
		LinkedList<TestStep> testList = new LinkedList<TestStep>();
		ArrayList<xmlheader> arr1 = xmlParsing(suiteName);
		for (int i = 0; i < arr1.size(); i++) {
			if ((!a.equals(arr1.get(i).getTestCaseName()))) {
				String testCase = arr1.get(i).getTestCaseName();
				String xpath = arr1.get(i).getXpath();
				String data = arr1.get(i).getData();
				String action = arr1.get(i).getAction();
				String additional = arr1.get(i).getAdditional();
				testList.add(TestStep.TestStepAction(suiteName, testCase,action, xpath, data,additional));
				}
			}
		return (testList);
		}	
	
	static void detailActionReport(String xp, String dt, String ac)
	{
		APPLICATION_LOGS.info("-----------------------------------------------------------");
		APPLICATION_LOGS.info("Action is        :" + ac);
		APPLICATION_LOGS.info("Xpath is         :" + xp);
		APPLICATION_LOGS.info("Expected Data is :" + dt);
		APPLICATION_LOGS.info("Actual data is   :" + driver.getText("xpath="+xp));
	}

	protected static void setUrl(String data) {
		APPLICATION_LOGS.info("Test Site:" + data);
		baseUrl = data;
		try{
			driver.open(baseUrl);
			}
		catch (Exception e) {
			Assert.fail("error opening browser:",e);
			}
		}
	
	
	protected static void openBrowser(String driverpath, String browserType,String url)
			throws IOException {

	    APPLICATION_LOGS.info("Browser: "+browserType);
	    driver = null;
	    try {
		driver = new DefaultSelenium("localhost",4444,"*"+browserType,url);
		driver.start();
	    } catch (Exception e) {
		Assert.fail("error opening browser:",e);
	    }
	    Assert.assertNotNull(driver);
	    driver.windowMaximize();

	}

	public static void tearDown_AfterClass() throws Exception {

		String msg = "Closing down browser";
		APPLICATION_LOGS.info(msg);
		Reporter.log(msg);
		driver.close();
	}

	public static ArrayList<xmlheader> xmlParsing(String TestCaseId)  {
		ArrayList<xmlheader> arrobj = new ArrayList<xmlheader>();
		try {
			String nodename[] = new String[10];

			String path = new File(".").getCanonicalPath();
			path = path + "/src/test/java/com/cafe/xml/" + TestCaseId + ".xml";

			File empdet = new File(path);
			DocumentBuilderFactory bf = DocumentBuilderFactory.newInstance();
			DocumentBuilder bul = bf.newDocumentBuilder();
			org.w3c.dom.Document doc = bul.parse(empdet);
			doc.getDocumentElement().normalize();
			NodeList nodes = doc.getElementsByTagName("TestCase");
			for (int j = 0; j < nodes.getLength(); j++) {
				Element eElement = (Element) nodes.item(j);
				String id = eElement.getAttribute("id");

				if (id.contains(TestCaseId)) {
					NodeList childNodes = eElement.getChildNodes();
					int childCount = childNodes.getLength();
					Node childNode;
					for (int i = 0; i < childCount; i++) {
						childNode = childNodes.item(i);
						if (childNode.getNodeType() == Node.ELEMENT_NODE) {

							Element el = (Element) childNode;
							NodeList node1 = el.getElementsByTagName("*");
							nodename = new String[node1.getLength()];
							for (int k = 0; k < node1.getLength(); k++) {

								nodename[k] = node1.item(k).getNodeName()
										.toString();

							}
							break;

						}

					}
					for (int i = 0; i < childCount; i++) {
						Node childlist = childNodes.item(i);
						xmlheader xh = new xmlheader();
						if (childlist.getNodeType() == Node.ELEMENT_NODE) {
							Element el = (Element) childlist;

							for (int k = 0; k < nodename.length; k++) {

								if (nodename[k].equals("Action")) {

									xh.setAction(getValue(nodename[k], el));
								} else if (nodename[k].equals("Xpath")) {
									xh.setXpath(getValue(nodename[k], el));
								} else if (nodename[k].equals("Data")) {
									xh.setData(getValue(nodename[k], el));
								}else if (nodename[k].equals("Additional")) {
									xh.setAdditional(getValue(nodename[k], el));
								}
								

							}
							xh.setTestCaseName(id);
							arrobj.add(xh);
						}
					}

				}

			}
		}

		catch (Exception e) {
			APPLICATION_LOGS.error("Exception in xmlParsing",e);
			Assert.fail("Exception in xmlParsing",e);
		}

		return arrobj;
	}

	public static ArrayList<xmlsuiteheader> masterFileParsing(String masterfilename) {
		ArrayList<xmlsuiteheader> arrobj = new ArrayList<xmlsuiteheader>();
		try {
			String nodename[] = new String[10];

			String path = new File(".").getCanonicalPath();
			path = path + "/src/test/java/com/cafe/xml/"+masterfilename+".xml";

			File empdet = new File(path);
			DocumentBuilderFactory bf = DocumentBuilderFactory.newInstance();
			DocumentBuilder bul = bf.newDocumentBuilder();
			org.w3c.dom.Document doc = bul.parse(empdet);
			doc.getDocumentElement().normalize();

			NodeList node = doc.getElementsByTagName("TestSuite");
			for (int i = 0; i < node.getLength(); i++) {

				Node childlist = node.item(i);
				if (childlist.getNodeType() == Node.ELEMENT_NODE) {

					Element el = (Element) childlist;
					NodeList node1 = el.getElementsByTagName("*");
					nodename = new String[node1.getLength()];
					for (int k = 0; k < node1.getLength(); k++) {

						nodename[k] = node1.item(k).getNodeName().toString();

					}

					break;
				}

			}

			for (int i = 0; i < node.getLength(); i++) {

				Node childlist = node.item(i);
				xmlsuiteheader xh = new xmlsuiteheader();
				if (childlist.getNodeType() == Node.ELEMENT_NODE) {
					Element el = (Element) childlist;

					for (int k = 0; k < nodename.length; k++) {

						if (nodename[k].equals("Name"))
							xh.setTestSuiteName(getValue(nodename[k], el));

						else if (nodename[k].equals("RunMode"))
							xh.setRunmode(getValue(nodename[k], el));

					}
					arrobj.add(xh);
				}
			}

		} catch (Exception e) {
			APPLICATION_LOGS.error("Exception in masterFileParsing",e);
			Assert.fail("Exception in masterFileParsing",e);
		}

		return arrobj;
	}

	public ArrayList<EnvironmentHeader> environmentParsing() {
		ArrayList<EnvironmentHeader> arrobj = new ArrayList<EnvironmentHeader>();
		try {
			String nodename[] = new String[10];

			String path = new File(".").getCanonicalPath();
			path = path + "/src/test/java/com/cafe/xml/Environment.xml";
			File empdet = new File(path);
			DocumentBuilderFactory bf = DocumentBuilderFactory.newInstance();
			DocumentBuilder bul = bf.newDocumentBuilder();
			org.w3c.dom.Document doc = bul.parse(empdet);
			Document doc1 = bul.parse(empdet);
			doc.getDocumentElement().normalize();

			String URL;
			NodeList URL_node = doc1.getElementsByTagName("URL");
			Element URL_el = (Element) URL_node.item(0);
			URL = URL_el.getTextContent();

			NodeList node = doc.getElementsByTagName("BrowserList");
			for (int i = 0; i < node.getLength(); i++) {

				Node childlist = node.item(i);
				if (childlist.getNodeType() == Node.ELEMENT_NODE) {

					Element el = (Element) childlist;
					NodeList node1 = el.getElementsByTagName("*");
					nodename = new String[node1.getLength()];
					for (int k = 0; k < node1.getLength(); k++) {

						nodename[k] = node1.item(k).getNodeName().toString();

					}

					break;
				}

			}

			for (int i = 0; i < node.getLength(); i++) {

				Node childlist = node.item(i);
				EnvironmentHeader xh = new EnvironmentHeader();
				if (childlist.getNodeType() == Node.ELEMENT_NODE) {
					Element el = (Element) childlist;

					for (int k = 0; k < nodename.length; k++) {

						if (nodename[k].equals("Browser")) {
							xh.setBrowser(getValue(nodename[k], el));
						} else if (nodename[k].equals("DriverPath")) {
							xh.setDriver_path(getValue(nodename[k], el));
						} else if (nodename[k].equals("Execute")) {
							xh.setExecute(getValue(nodename[k], el));
						}

						xh.setURL(URL);

					}
					arrobj.add(xh);
				}
			}

		} catch (Exception e) {
			APPLICATION_LOGS.error("Exception in environmntParsing",e);
			Assert.fail("Exception in environmntParsing",e);
		}

		return arrobj;
	}

	private static String getValue(String tag, Element el) throws Exception {

		String nodeval;
		nodeval = "";
		try {
			NodeList nodes = el.getElementsByTagName(tag).item(0)
					.getChildNodes();
			Node node = (Node) nodes.item(0);
			nodeval = node.getNodeValue();
		} catch (Exception e) {
			nodeval = "";
		}
		return nodeval;

	}
	
	

	static void navigate(String xpathVal) throws InterruptedException {
		driver.open(baseUrl + xpathVal);}
	
	
	static void inputData(String xpathVal, String data,String _additional)
			throws InterruptedException, AWTException {
		driver.type("xpath="+xpathVal," ");
		driver.type("xpath="+xpathVal,data);
		driver.fireEvent("xpath="+xpathVal, "blur");
		ac = "inputData";
		detailActionReport(xpathVal, data, ac);
		}
	
	
	static void performclick(String xpathVal,String _additional1)  {
		try {
		     if(!_additional1.equals(""))
		     {
		    	 xpathVal=xpathVal+StoredValue+_additional1;
		     }
		     
			driver.click("xpath=" + xpathVal);
			APPLICATION_LOGS.info("Saranya:: "+xpathVal);
			APPLICATION_LOGS.info("Found the web element to click");
		} catch (Exception e) {
			verificationErrors.append(e.toString());
			APPLICATION_LOGS.error("Error: ",e);
			APPLICATION_LOGS.info("Cannot find web element to click");
			Assert.fail("performClick",e);
		}
	}

	
	static void verifyText(String xpathVal, String data,String _additional1) throws InterruptedException {
		try {
			 if(!_additional1.equals(""))
		    	 xpathVal=xpathVal+StoredValue+_additional1;
			assertTrue(driver.getText("xpath="+ xpathVal).contains(data));
			APPLICATION_LOGS.info("Verified Text: " + data);
		}
		catch (Error e) {
			verificationErrors.append(e.toString());
			APPLICATION_LOGS.info("Text not found");
			APPLICATION_LOGS.error("Error: ",e);
			throw e;
		}
		ac = "verifyText";
		detailActionReport(xpathVal, data, ac);
		}
	
	
	static void verifyTitle(String data) throws InterruptedException {
		Thread.sleep(1000L);
		try {
			assertEquals(driver.getTitle(), data);
			APPLICATION_LOGS.info("verified Title: " + data);
			}
		catch (Error e) {
			verificationErrors.append(e.toString());
			APPLICATION_LOGS.info("Title not found");
			APPLICATION_LOGS.error("Error: ",e);
			throw e;}
		}
	
	
	
	static void waitFor() throws InterruptedException {
		try{
			driver.waitForPageToLoad("50000");
			Thread.sleep(1000L);
		} 
		catch(Exception e){
			APPLICATION_LOGS.error("Error: (wait) ",e);
			detailActionReport("", "", e.toString());
			}
		}
	
	static void uniqueInput(String xpathVal, String data)
			throws InterruptedException {
			uniqueEntry = dateTime + data;
			try {
				inputData(xpathVal, uniqueEntry,"");
				} 
			catch (AWTException e) {
				e.printStackTrace();
				}
			}
	
	
	static void verifyUniqueInput(String xpathVal, String data,String _additional1)
			throws InterruptedException {
		uniqueEntry = dateTime + data;
		if(!_additional1.equals(""))
	    	 xpathVal=xpathVal+StoredValue+_additional1;
		if (isElementPresent(xpathVal)) {
			try {
				assertTrue(driver.getText("xpath="+xpathVal).contains(uniqueEntry));
				APPLICATION_LOGS.info("Verified Text: " + uniqueEntry);
				}
			catch (Error e) {
				verificationErrors.append(e.toString());
				APPLICATION_LOGS.error("Error: ",e);
				throw e;
				}
			} 
		else {
			APPLICATION_LOGS.info("No records found");
			}
		ac = "verifyUniqueInput";
		detailActionReport(xpathVal, data, ac);
		}
	
	private static boolean isElementPresent(String xpathVal) {
		try {
			driver.isElementPresent("xpath="+xpathVal);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
		
	}
	
	
	static void inputDropDown(String xpathVal, String data,String _additional)
			throws InterruptedException {
		Thread.sleep(2000L);
		if(!_additional.equals(""))
		data=dateTime + data;
		APPLICATION_LOGS.info(driver.getAttribute("xpath="+xpathVal+"@id") + ": " + data);
		driver.select("xpath="+xpathVal, "label="+data);
		driver.fireEvent("xpath="+xpathVal, "blur");
		Thread.sleep(2000L);
		ac = "inputDropDown";
		detailActionReport(xpathVal, data, ac);
	}
	
	
	static void dialogCheck(String xpathVal) throws InterruptedException {
		try{
			if (xpathVal.equals("yes"))
			{
				driver.chooseOkOnNextConfirmation();
				driver.getConfirmation();
				ac = "Alert";
				detailActionReport(xpathVal, "yes", ac);
				}
			else if (xpathVal.equals("no"))
			{
				driver.chooseCancelOnNextConfirmation();
				ac = "Alert";
				detailActionReport(xpathVal, "no", ac);
				}
			}
		catch(Exception e){
			verificationErrors.append(e.toString());
			APPLICATION_LOGS.error("Error: ",e);
			}
		
	}
	
	
	public static void performLinkClick(String _data,String _additional1) {
		try {
			if(!_additional1.equals(""))
			{
				_data=_additional1;
			}
			
			APPLICATION_LOGS.info(_data);
			driver.click("link="+_data);
			driver.waitForPageToLoad("30000");
			APPLICATION_LOGS.info("Found the web element to click");
			} 
		catch (Exception e) {
			verificationErrors.append(e.toString());
			APPLICATION_LOGS.error("Error: ",e);
			APPLICATION_LOGS.info("Cannot find web element to click");
			Assert.fail("performLinkClick",e);
			}
		ac = "performLinkClickt";
		detailActionReport(_data, _data, ac);
		}

	
	public static void deleteCookies() {
		try
		{
			driver.deleteCookie("JSESSIONID","/");
			APPLICATION_LOGS.info("Deleted cookies");	
			}
		catch (Error e) {
			verificationErrors.append(e.toString());
			APPLICATION_LOGS.error("Error: ",e);
		}	
		ac = "LogOut";
		detailActionReport("deleteCookies", "deleteCookies", ac);
	}

	public static void StoreValue(String _additional) {
			String[] URL =driver.getLocation().split("/");
			StoredValue=URL[5];
			APPLICATION_LOGS.info("Value Stored:" + StoredValue );	
		
	}

	public static void inputDate(String _xpath,String _data) throws InterruptedException, AWTException {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");// dd/MM/yy
		Date now = new Date();
		
		if (!_data.equals(""))
		{
			Calendar cal = Calendar.getInstance();
	        cal.setTime(now);
	        cal.add(Calendar.DATE,Integer.parseInt(_data)); //minus number would decrement the days 
			now = cal.getTime();	
		}
		String strDate = sdfDate.format(now);
			
		inputData(_xpath, strDate, "");
				
	}

	public static void scroll(String _xpath) {
		driver.getEval("window.scrollBy(100,0)");
		
	}

	public static void StoreschoolId() {
		String[] URL =driver.getLocation().split("/");
		SchoolId=URL[5];
		APPLICATION_LOGS.info("Value Stored:" + SchoolId );	
		
	}

	public static void ClickStoreId(String xpathVal,String _additional1) {
		try {
		     if(!_additional1.equals(""))
		     {
		    	 xpathVal=xpathVal+SchoolId+_additional1;
		     }
		     
			driver.click("xpath=" + xpathVal);
			APPLICATION_LOGS.info("Saranya:: "+xpathVal);
			APPLICATION_LOGS.info("Found the web element to click");
		} catch (Exception e) {
			verificationErrors.append(e.toString());
			APPLICATION_LOGS.error("Error: ",e);
			APPLICATION_LOGS.info("Cannot find web element to click");
			Assert.fail("performClick",e);
		}
		
	}
	

	
	
}
	
