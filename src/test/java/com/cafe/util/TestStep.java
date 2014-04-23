package com.cafe.util;


import java.awt.AWTException;
import java.io.IOException;
import java.util.concurrent.Callable;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
//


import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

public class TestStep {
	static DefaultSelenium selenium= new DefaultSelenium("localhost", 4444, "firefox", "http://54.83.39.177");
	String _suite;
	String _desc;
	String _xpath;
	String _data;
	String _action;
	Callable<Void> _c;
	
	String _browser;
	String _driverPath;
	boolean  _stepPassed = true;
	
	public TestStep(String suite,String desc,Callable<Void> c, String xpath, String data, String action) {
		_suite = suite;
		_desc = desc;
		_c = c;
		_xpath = xpath;
		_data = data;
		_action = action;
		
	}
	public static TestStep TestStepSetUrl(String suite, String url) {
		final String _action = "SetUrl";
		final String _url =  url;
		Callable<Void> c = new Callable<Void>() { public Void call() {
		try {
			String msg = "SetUrl:" + _url;
			Reporter.log(msg);
			TestUtility.APPLICATION_LOGS.info(msg);
			TestUtility.setUrl(_url);
		} catch (Exception e) {
				Assert.fail(_action,e);
		}
		    return null; }};
		return new TestStep(suite,_action,c,_url,"","");
				
		
	}
	public static TestStep TestStepOpenBrowser(String suite, String driverPath, String browser,String url) {
		final String _action = "OpenBrowser";
		final String _browser =  browser;
		final String _driverPath = driverPath;
		final String _url =  url;
		
		Callable<Void> c = new Callable<Void>() { public Void call() {
		try {
			String msg = "OpenBrowser:" + _browser;
			Reporter.log(msg);
			TestUtility.APPLICATION_LOGS.info(msg);
			TestUtility.changeTimeStamp();
			TestUtility.openBrowser(_driverPath, _browser,_url);
		} catch (Exception e) {
				Assert.fail(_action,e);
		}
		    return null; }};
		return new TestStep(suite,"OpenBrowser",c,_driverPath,_browser,_url);
	}
	
	public static TestStep TestStepCloseBrowser(String suite) {
		final String _action = "CloseBrowser";
		Callable<Void> c = new Callable<Void>() { public Void call() {
		try {
			String msg = "CloseBrowser";
			Reporter.log(msg);
			TestUtility.APPLICATION_LOGS.info(msg);
			TestUtility.tearDown_AfterClass();
		} catch (Exception e) {
				Assert.fail(_action,e);
		}
		    return null; }};
		return new TestStep(suite,"CloseBrowser",c,"","","");
	}
	
	public static TestStep TestStepAction( String suite,String desc,String action, String xpath,String data,String additional) 
	{
		final String _action = action;
		final String _xpath = xpath;
		final String _data = data;
		final String _additional1 = additional;
		Callable<Void> _c = null;
		

		 if(action.equals("Navigate"))
		     _c = new Callable<Void>() { public Void call() {
			 try {
				TestUtility.navigate(_xpath);
			} catch (InterruptedException e) {
				Assert.fail(_action,e);
			}
		    return null; }};
		 else if(action.equals("Input"))
		     _c = new Callable<Void>() { public Void call() {
			 try {
				try {
					TestUtility.inputData(_xpath,_data,_additional1);
				} catch (AWTException e) {
					e.printStackTrace();
				}
			} catch (InterruptedException e) {
					Assert.fail(_action,e);
			}
		    return null; }};
		 else if(action.equals("Click"))
		     _c = new Callable<Void>() { public Void call() {
			 TestUtility.performclick(_xpath,_additional1);	
		    return null; }};
		 else if(action.equals("VerifyText"))
		     _c = new Callable<Void>() { public Void call() {
			 try {
				TestUtility.verifyText(_xpath,_data,_additional1);
			} catch (InterruptedException e) {
				Assert.fail(_action,e);
			}
		    return null; }};
		 else if(action.equals("VerifyTitle"))
		     _c = new Callable<Void>() { public Void call() {
			 try {
				TestUtility.verifyTitle(_data);
			} catch (InterruptedException e) {
				e.printStackTrace();
				Assert.fail(_action,e);
			}
		    return null; }};
		 else if  (action.equals("Wait"))
		     _c = new Callable<Void>() { public Void call() {
			 try {
				TestUtility.waitFor();
			} catch (InterruptedException e) {
				Assert.fail(_action,e);
			}
		    return null; }};
	
		
		 else if (action.equals("UniqueInput"))
		     _c = new Callable<Void>() { public Void call() {
			 try {
			
				TestUtility.uniqueInput(_xpath,_data);
			} catch (Exception e) {
				Assert.fail(_action,e);
			}
		    return null; }};
		 else if (action.equals("VerifyUniqueInput"))
		     _c = new Callable<Void>() { public Void call() {
			 try {
				TestUtility.verifyUniqueInput(_xpath,_data,_additional1);
			} catch (InterruptedException e) {
				Assert.fail(_action,e);
			}
		    return null; }};	
		    
		 else if (action.equals("InputDropDown"))
		     _c = new Callable<Void>() { public Void call() {
			 try {
				TestUtility.inputDropDown(_xpath,_data,_additional1);
			} catch (InterruptedException e) {
				Assert.fail(_action,e);

			}
		    return null; }};
		 
		 else if (action.equals("Alert"))
		     _c = new Callable<Void>() { public Void call() {
			 try {
				TestUtility.dialogCheck(_xpath);
			} catch (InterruptedException e) {
				Assert.fail(_action,e);
			}
		    return null; }};
		 else if(action.equals("LinkClick"))
		     _c = new Callable<Void>() { public Void call() {
		    	 TestUtility.performLinkClick(_data,_additional1);
			    return null; }};
		 else if (action.equals("Sleep"))
		     _c = new Callable<Void>() { public Void call() throws InterruptedException {
			Thread.sleep(3000L);
			 return null;
		    }};
		 else if (action.equals("DeleteCookie"))
		     _c = new Callable<Void>() { public Void call() throws InterruptedException {
			 TestUtility.deleteCookies();
			 return null;
		    }};
		 else if (action.equals("StoreValue"))
		     _c = new Callable<Void>() { public Void call() throws InterruptedException {
			 TestUtility.StoreValue(_additional1);
			 return null;}};
		 else if (action.equals("ClickStoreId"))
		     _c = new Callable<Void>() { public Void call() throws InterruptedException {
			 TestUtility.ClickStoreId(_xpath,_additional1);
			 return null;
		    }};
		 else if (action.equals("StoreId"))
		     _c = new Callable<Void>() { public Void call() throws InterruptedException {
			 TestUtility.StoreschoolId();
			 return null;
		    }};
		    
		 else if (action.equals("InputDate"))
		     _c = new Callable<Void>() { public Void call() throws InterruptedException, AWTException {
			 TestUtility.inputDate(_xpath,_data);
			 return null;
		    }};
		 else if (action.equals("Scroll"))
		     _c = new Callable<Void>() { public Void call() throws InterruptedException {
			 TestUtility.scroll(_xpath);
			 return null;}};
		    
		    
		 else
		     _c  = new Callable<Void>() { public Void  call() {
			 TestUtility.APPLICATION_LOGS.warn(" Warning: Has not created Test cases yet for " + _action);
		     return null;
		     }};
		return new TestStep(suite, desc,_c,_xpath,_data,_action);
	}

	
	
	public boolean doStep() throws Exception {
	    try {
	    	Reporter.log("---------------------------------------------------------");
		Reporter.log("Suite:"+_suite+"\n:Step:"+_desc+ "\nAction:"+_action);
		Reporter.log("---------------------------------------------------------");
		_c.call();
	    } catch (AssertionError e) {
		_stepPassed = false;
		throw e;
	    } catch (Exception e) {
	    	_stepPassed = false;
	    	throw e;
		} 
	    return(_stepPassed);
	}
	public boolean stepFailed() {
		return (! _stepPassed);
	}
	
	public String getDesc() { return _desc; }
	public String getXpath() { return _xpath; }
	public String getData() { return _data; }
	public String getStepDetails() { return "Step: " + _desc + " \nAction:"+_action + " \nXpath:" + _xpath + " \nData:" + _data ;}

}