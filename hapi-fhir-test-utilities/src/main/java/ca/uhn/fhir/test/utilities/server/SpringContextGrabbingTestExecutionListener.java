/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
