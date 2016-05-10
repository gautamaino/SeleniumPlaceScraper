package com.ainosoft.seleniumplacescraper.util;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author tushar@ainosoft.com
 */
public class ScraperLogger {

	private final static Logger logger = Logger.getLogger(ScraperLogger.class.getName());

	private File loggerFile;
	private static String loggerFileName;

	private FileHandler fileHandler;

	public ScraperLogger(){

	}

	public ScraperLogger(String fileName){
		try {
			loggerFileName = fileName;

			loggerFile = new File(fileName);

			boolean fileExists = loggerFile.exists();
			if(fileExists){
				loggerFile.delete();
				loggerFile = new File(fileName);
			}

			fileHandler = new FileHandler(fileName,true);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.addHandler(fileHandler);
		SimpleFormatter simpleFormatter = new SimpleFormatter();
		fileHandler.setFormatter(simpleFormatter);
	}

	/**
	 * This method will print the message.
	 */
	public static void log(String msg) {
		logger.log(Level.INFO, msg);
	}

	/**
	 * This method will print the exception.
	 */
	public static void log(String str,Exception ex) {
		logger.log(Level.SEVERE, str,ex);
	}

	public static String getLoggerFileName() {
		return loggerFileName;
	}

	public static void setLoggerFileName(String loggerFileName) {
		ScraperLogger.loggerFileName = loggerFileName;
	}
}