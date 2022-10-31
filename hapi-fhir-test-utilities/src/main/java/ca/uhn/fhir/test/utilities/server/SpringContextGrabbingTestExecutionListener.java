package ca.uhn.fhir.test.utilities.server;

import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.util.Assert;

public class SpringContextGrabbingTestExecutionListener extends AbstractTestExecutionListener {

	private static final ThreadLocal<ApplicationContext> ourApplicationContext = new ThreadLocal<>();

	@Override
	public void beforeTestClass(TestContext theTestContext) {
		ApplicationContext applicationContext = theTestContext.getApplicationContext();
		Assert.notNull(applicationContext, "No application context saved");
		ourApplicationContext.set(applicationContext);
	}

	@Override
	public void afterTestClass(TestContext theTestContext) {
		ourApplicationContext.remove();
	}

	public static ApplicationContext getApplicationContext(){
		ApplicationContext applicationContext = ourApplicationContext.get();
		Assert.notNull(applicationContext, "No application context saved. Did you remember to register the context grabbing listener by annotating your class with:\n" +
			"@TestExecutionListeners(value = SpringContextGrabbingTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)");
		return applicationContext;
	}
}
