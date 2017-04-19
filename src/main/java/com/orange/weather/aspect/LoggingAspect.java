package com.orange.weather.aspect;


import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;


@Aspect
public class LoggingAspect {

	final static Logger logger = Logger.getLogger(LoggingAspect.class);
	
	/**
	 * Log any call of any method, print method name and parameters
	 * @param joinPoint: the method call
	 */
	
	@Before("execution(public * com.orange.weather..*(..))")
	public void logBefore(JoinPoint joinPoint) {
		logger.info("Method is called: " + joinPoint.getSignature().getName());
		
		Object[] arguments = joinPoint.getArgs();
	    for (int i =0; i < arguments.length; i++){
	        Object argument = arguments[i];
	        if (argument != null){
	           logger.debug("With argument of type" + argument.getClass().toString() + " and value " + argument);
	        }
	    }
		
	}

}