package com.byc.tools.patch.log;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.exception.ExceptionUtils;


/**
 * 
 * @author ycbai
 *
 */
public class ExceptionLogger {

	public static void log(Exception exception) {
		log(ExceptionLogger.class, exception);
	}

	public static void log(Class<?> cls, Exception exception) {
		Logger logger = Logger.getLogger(cls.getName());
		logger.log(getPriority(exception), ExceptionUtils.getStackTrace(exception));
	}

	private static Level getPriority(Throwable ex) {
		if (ex == null) {
			throw new IllegalArgumentException("ex param cannot be null");
		}
		// TODO: will handle some kinds of exceptions after...
		return Level.SEVERE;
	}

}
