package com.cafe.testcases;

import java.util.ArrayList;
import java.util.LinkedList;

import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import com.cafe.testcases.CafeLearnRegressionTest;
import com.cafe.util.EnvironmentHeader;
import com.cafe.util.TestCase;
import com.cafe.util.TestStep;
import com.cafe.util.TestUtility;
import com.cafe.util.xmlsuiteheader;

public class CafeLearnRegressionTest  extends TestUtility {
	
	
	
	  @Factory
	  public Object[] getTestForAllBrowsers() throws Exception  {
		  
		  dateTime = CafeLearnRegressionTest.RANDOM_TIMESTAMP;
		
		  
		  LinkedList<TestCase> testList = new LinkedList<TestCase>(); 
		  ArrayList<EnvironmentHeader> env=environmentParsing();
		 
		  for(int j=0;j<env.size();j++)
		  {
			 
			  if(env.get(j).getExecute().equals("yes"))
			  {
				  String driverPath = env.get(j).getDriver_path();
				  String browser = env.get(j).getBrowser();
				  String urlStr = env.get(j).getURL();
				  TestCase firstTest = null;
				  TestCase lastTest  = null;
				  		  
				  ArrayList<xmlsuiteheader> arr1= masterFileParsing("Masterfile");
				  for(int i=0;i<arr1.size();i++)
				  { 
					  if(arr1.get(i).getRunmode().equals("yes"))
					  {
						  String testName = arr1.get(i).getTestSuiteName();
						  TestCase testSuite = new TestCase(testName,driverPath,browser,urlStr, getStepsForSuite(testName));
						  testSuite.addBeginStep(TestStep.TestStepSetUrl(testName,urlStr));
						  testList.add(testSuite);
						  if (firstTest == null) {
							firstTest = testSuite;  
						  }
						  lastTest = testSuite;
					  }
				  
				  }
				  // add a step to open Browser in first test case
				  firstTest.addBeginStep(TestStep.TestStepOpenBrowser(firstTest.getTestName(),driverPath, browser,urlStr));
				  // add a stepp to close Browser in last test case
				  lastTest.addEndStep(TestStep.TestStepCloseBrowser(lastTest.getTestName()));
				  
			  	}
		  	}
		  
		  	return(testList.toArray());
		 	
	  }	
}
