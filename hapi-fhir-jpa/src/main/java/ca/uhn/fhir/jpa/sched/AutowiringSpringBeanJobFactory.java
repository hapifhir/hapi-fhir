package ca.uhn.fhir.jpa.sched;

/*-
 * #%L
 * hapi-fhir-jpa
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.hl7.fhir.r4.model.InstantType;
import org.quartz.JobKey;
import org.quartz.spi.TriggerFiredBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import java.util.Date;

public class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {

	private transient AutowireCapableBeanFactory myBeanFactory;
	private ApplicationContext myAppCtx;
	private static final Logger ourLog = LoggerFactory.getLogger(AutowiringSpringBeanJobFactory.class);

	@Override
	public void setApplicationContext(final ApplicationContext theApplicationContext) {
		myAppCtx = theApplicationContext;
		myBeanFactory = theApplicationContext.getAutowireCapableBeanFactory();
	}

	@Override
	protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {

		String prev = toString(bundle.getPrevFireTime());
		String scheduled = toString(bundle.getScheduledFireTime());
		String next = toString(bundle.getNextFireTime());
		String fireInstanceId = bundle.getTrigger().getFireInstanceId();
		JobKey key = bundle.getJobDetail().getKey();
		ourLog.debug("Firing job[{}] ID[{}] - Previous[{}] Scheduled[{}] Next[{}]", key, fireInstanceId, prev, scheduled, next);

		Object job = super.createJobInstance(bundle);
		myBeanFactory.autowireBean(job);
		if (job instanceof ApplicationContextAware) {
			((ApplicationContextAware) job).setApplicationContext(myAppCtx);
		}
		return job;
	}

	private String toString(Date theDate) {
		if (theDate == null) {
			return null;
		}
		return new InstantType(theDate).getValueAsString();
	}
}
