package com.byc.tools.patch.log;

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
		BasicConfigurator.configure(new PatchToolLogAppender());
	}

	public static void log(Exception exception) {
		logger.log(getPriority(exception), exception.getMessage(), exception);
	}

	private static Level getPriority(Throwable ex) {
		if (ex == null) {
			throw new IllegalArgumentException("ex param cannot be null");
		}
		// TODO: will handle some kinds of exceptions after...
		return Level.ERROR;
	}

}
