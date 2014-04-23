package com.cafe.util;

public class EnvironmentHeader {
String URL;
String browser;
String driver_path;
String Execute;
public String getURL() {
	return URL;
}
public String getExecute() {
	return Execute;
}
public void setExecute(String execute) {
	Execute = execute;
}
public void setURL(String uRL) {
	URL = uRL;
}
public String getBrowser() {
	return browser;
}
public void setBrowser(String browser) {
	this.browser = browser;
}
public String getDriver_path() {
	return driver_path;
}
public void setDriver_path(String driver_path) {
	this.driver_path = driver_path;
}

}
