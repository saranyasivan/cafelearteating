package com.cafe.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import org.testng.Reporter;
import org.testng.annotations.Test;

@Test
public class TestCase implements org.testng.ITest {
	static int instantiationOrder = 0;
	String _testName;
	String _driverPath;
	String _browser;
	String _url;
	int index = 0;
	LinkedList<TestStep> _steps;
	
	public TestCase(String testName, String driverPath, String browser, String url,LinkedList<TestStep> steps) {
		_testName = testName;
		_driverPath = driverPath;
		_browser = browser;
		_url = url;
		_steps = steps;
		index = instantiationOrder;
		instantiationOrder++;
		
	}
	
	public int getIndex() {
		return index;
	}
	
	public void runTestCase() throws Exception {
		TestUtility.tname = _testName;
		String msg = "Running Test:"+ getTestName() +  " on driver:"  + _driverPath  + " browser:" + _browser;
		Reporter.log(msg);
		TestUtility.APPLICATION_LOGS.info("_______________________________________________________________________");
		TestUtility.APPLICATION_LOGS.info(msg);
		Iterator<TestStep> itr = _steps.iterator();
	      while(itr.hasNext()) {
	         TestStep step = itr.next();
	         
	         String msg1 = step.getStepDetails();
	         Reporter.log(msg1);
	 		 TestUtility.APPLICATION_LOGS.info(msg1);
	     
	         
	         step.doStep();
	      }
		
	
		msg = "Finished Running Test:"+getTestName();
		Reporter.log(msg);
		TestUtility.APPLICATION_LOGS.info(msg);
		TestUtility.APPLICATION_LOGS.info("_______________________________________________________________________");
	}

	public void addBeginStep(TestStep step) {
		_steps.addFirst(step);
	}
	
	public void addEndStep(TestStep step) {
		_steps.addLast(step);
	}
	
	
	public String getTestName() {
		// TODO Auto-generated method stub
		return(_testName +"("+_browser + ")");
	}	
	
	public TestStep getFailedStep() {
		Iterator<TestStep> itr = _steps.iterator();
	      while(itr.hasNext()) {
	         TestStep step = itr.next();
	         if (step.stepFailed()) {
	        	 return (step);
	         }
	      }
	      return null;
	}
}

