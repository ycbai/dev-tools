package com.byc.tools.patch.log;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * 
 * @author ycbai
 *
 */
public class ExceptionLogger {

	private static Logger logger = Logger.getLogger(ExceptionLogger.class);

	static {
		BasicConfigurator.configure();
	}

	public static void log(Exception exception) {
		logger.log(getPriority(exception), ExceptionUtils.getStackTrace(exception));
	}

	private static Level getPriority(Throwable ex) {
		if (ex == null) {
			throw new IllegalArgumentException("ex param cannot be null");
		}
		// TODO: will handle some kinds of exceptions after...
		return Level.ERROR;
	}

}
