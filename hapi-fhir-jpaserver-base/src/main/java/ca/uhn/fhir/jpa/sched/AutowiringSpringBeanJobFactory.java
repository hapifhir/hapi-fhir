package ca.uhn.fhir.jpa.sched;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

public class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {

	private transient AutowireCapableBeanFactory myBeanFactory;
	private ApplicationContext myAppCtx;

	@Override
	public void setApplicationContext(final ApplicationContext theApplicationContext) {
		myAppCtx = theApplicationContext;
		myBeanFactory = theApplicationContext.getAutowireCapableBeanFactory();
	}

	@Override
	protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
		Object job = super.createJobInstance(bundle);
		myBeanFactory.autowireBean(job);
		if (job instanceof ApplicationContextAware) {
			((ApplicationContextAware) job).setApplicationContext(myAppCtx);
		}
		return job;
	}
}
