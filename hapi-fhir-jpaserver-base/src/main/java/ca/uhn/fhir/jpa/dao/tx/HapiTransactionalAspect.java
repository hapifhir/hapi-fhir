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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionCallback;

/**
 * @see HapiTransactionalAspect
 * @since 5.1.0
 */
@Aspect
public class HapiTransactionalAspect {

	@Autowired
	private HapiTransactionService myHapiTransactionService;

	/**
	 * Applies {@literal @HapiTransactional} at the type level. Note that if we need it, we can
	 * use the following to apply this at the method level:
	 * <p>
	 * {@literal @annotation(ca.uhn.fhir.jpa.dao.tx.HapiTransactional)}
	 */
	@Around("within(@ca.uhn.fhir.jpa.dao.tx.HapiTransactional *) && execution(* *(..))")
	public Object wrapCallInTransactionForType(ProceedingJoinPoint theJoinPoint) throws Throwable {

		TransactionCallback<Object> txCallback = tx -> {
			try {
				return theJoinPoint.proceed();
			} catch (Throwable theThrowable) {
				// Break out of the TransactionTemplate (this gets caught a few lines below)
				throw new HapiTransactionService.MyException(theThrowable);
			}
		};
		return myHapiTransactionService.execute(null, txCallback);

	}


}
