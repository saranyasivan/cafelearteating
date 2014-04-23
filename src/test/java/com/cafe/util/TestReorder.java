package com.cafe.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;


public class TestReorder implements IMethodInterceptor {

	public List<IMethodInstance> intercept(List<IMethodInstance> paramList,
			ITestContext paramITestContext) {

		 TreeMap<Integer,IMethodInstance> result = new TreeMap<Integer,IMethodInstance>();
	     //You have to watch out to get the right test if you have other tests in oyur suite        
	    if (!paramITestContext.getName().equals("UnwantedTest")) {
	        for (IMethodInstance iMethodInstance : paramList) {
	            Object[] obj = iMethodInstance.getInstances();
	            if (obj[0] instanceof com.cafe.util.TestCase) {
	                //DO your stuff like putting it in a list/array
	            	TestCase z = (TestCase) obj[0];
	            	result.put(z.getIndex(),iMethodInstance);
	            } else {
	                //DO your stuff like putting it in a list/array with the other Testclasses
	            }
	        }
	   
	       
	    }
	    return new ArrayList<IMethodInstance>(result.values());

	}
	
	
	


}