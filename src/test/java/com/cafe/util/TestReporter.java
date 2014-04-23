package com.cafe.util;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.xml.XmlSuite;
import org.apache.log4j.Logger;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TestReporter implements IReporter {
    private static Logger log = Logger.getLogger(TestReporter.class.getName());

	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
			String outputDirectory) {
		int passedTestCnt = 0;
		int failedTestCnt = 0;
		int skippedTestCnt= 0;
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		//get current date time with Date()
		Date date = new Date();


		try  
		{
		    log.info("Calling TestReporter");
		    OutputStream fout = new FileOutputStream(outputDirectory + "/cafeLearn-test-report.txt", false); //true tells to append data.
		    PrintStream report  = new PrintStream(fout);
		    report.println("--------------------------------");
			report.println("SUMMARY TEST REPORT");
			report.println("--------------------------------");	
			report.println("generated on:"+ dateFormat.format(date));		
			report.println("--------------------------------");	
			Iterator<ISuite> itr = suites.iterator();
			while(itr.hasNext()) {
				ISuite s = itr.next();
				Set<String> keys  = s.getResults().keySet();
				Iterator<String> keyIter = keys.iterator();
				while(keyIter.hasNext()) {
					String suiteName = keyIter.next();
					
					
					ISuiteResult results =s.getResults().get(suiteName);
					ITestContext context = results.getTestContext();

				    IResultMap passedTests = context.getPassedTests();
				    IResultMap failedTests = context.getFailedTests();
				    IResultMap skippedTests = context.getSkippedTests();
				    
				    for( ITestResult r: passedTests.getAllResults()) {
				         report.println("Test:"+ r.getTestName() + " - passed");
				    }
				    for( ITestResult r: failedTests.getAllResults()) {
				         String msg = "";
				         TestCase t = (TestCase) r.getInstance();
				         TestStep step = t.getFailedStep();
				         if (step != null) {
				        	msg = step.getStepDetails();
				         }
				         report.println("--------------------------------------------");
				         report.println("Test:"+ r.getTestName() + " - Failed!! at:"+msg);
				        
				         
				         report.println("Test:"+ r.getTestName() + " - Failed!! Exception:");
				         
				    	 report.println( r.getThrowable());
				    	 report.println("-------------------------------------------- ");
				    }
				    passedTestCnt += passedTests.size();
				    failedTestCnt += failedTests.size();
				    skippedTestCnt += skippedTests.size();
				   
				    
				    report.println("Test Group:"+suiteName+"- Passed:"+passedTests.size() 
				    		+ " Failed:"+failedTests.size() + " Skipped:"+skippedTests.size());
				   
				    report.println("--------------------------------");
				   
				}
				report.println("--------------------------------");
				report.println("TOTALS: Passed:"+passedTestCnt + " Failed:"
				+failedTestCnt + " Skipped: " + skippedTestCnt); 
				report.close();
			}
		    fout.close();
		}
		catch (Exception e)
		{
		    System.err.println("Error: " + e.getMessage());
		}
		
		
	}


 
	
}
