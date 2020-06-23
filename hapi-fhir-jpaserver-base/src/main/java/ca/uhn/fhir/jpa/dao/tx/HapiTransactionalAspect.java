package ca.uhn.fhir.jpa.dao.tx;

/*
 * #%L
 * Smile CDR - CDR
 * %%
 * Copyright (C) 2016 - 2018 Simpatico Intelligent Systems Inc
 * %%
 * All rights reserved.
 * #L%
 */

import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;

/**
 * @see HapiTransactionalAspect
 * @since 5.1.0
 */
@Aspect
public class HapiTransactionalAspect {
	private static final Logger ourLog = LoggerFactory.getLogger(HapiTransactionalAspect.class);

	@Autowired
	private PlatformTransactionManager myTransactionManager;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	private TransactionTemplate myTxTemplate;

	@PostConstruct
	public void start() {
		myTxTemplate = new TransactionTemplate(myTransactionManager);
	}

	/**
	 * Applies {@literal @HapiTransactional} at the type level. Note that if we need it, we can
	 * use the following to apply this at the method level:
	 *
	 * {@literal @annotation(ca.uhn.fhir.jpa.dao.tx.HapiTransactional)}
	 */
	@Around("within(@ca.uhn.fhir.jpa.dao.tx.HapiTransactional *) && execution(* *(..))")
	public Object runAsSystem(ProceedingJoinPoint theJoinPoint) throws Throwable {
		TransactionCallback<Object> txCallback = tx -> {
			try {
				return theJoinPoint.proceed();
			} catch (Throwable theThrowable) {
				// Break out of the TransactionTemplate (this gets caught a few lines below)
				throw new MyException(theThrowable);
			}
		};
		return execute(txCallback);

	}

	public <O> O execute(TransactionCallback<O> theCallback) {

		for (int i = 0; ; i++) {
			try {

				try {
					return myTxTemplate.execute(theCallback);
				} catch (MyException e) {
					if (e.getCause() instanceof RuntimeException) {
						RuntimeException cause = (RuntimeException) e.getCause();
						throw cause;
					} else {
						throw new InternalErrorException(e);
					}
				}

			} catch (ResourceVersionConflictException e) {



				//			 FIXME: logs?
				ourLog.info("Version conflict: {}", e.toString());
//			theCleanupTask.run();
				try {
					Thread.sleep(100 * i);
				} catch (InterruptedException e2) {
					// ignore
				}
				if (i > 10) {
					throw e;
				}
			}
		}


	}

	/**
	 * This is just an unchecked exception so that we can catch checked exceptions inside TransactionTemplate
	 * and rethrow them outside of it
	 */
	private class MyException extends RuntimeException {

		public MyException(Throwable theThrowable) {
			super(theThrowable);
		}
	}


}
