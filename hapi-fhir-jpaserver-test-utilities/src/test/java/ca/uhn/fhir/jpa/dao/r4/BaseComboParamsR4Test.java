package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.search.reindex.ResourceReindexingSvcImpl;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.util.SpringObjectCaster;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class BaseComboParamsR4Test extends BaseJpaR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(BaseComboParamsR4Test.class);
	@Autowired
	protected ISearchParamRegistry mySearchParamRegistry;
	protected List<String> myMessages = new ArrayList<>();
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	@BeforeEach
	public void before() {
		myModelConfig.setDefaultSearchParamsCanBeOverridden(true);
		myDaoConfig.setSchedulingDisabled(true);
		myDaoConfig.setUniqueIndexesEnabled(true);

		myInterceptorBroadcaster = mock(IInterceptorBroadcaster.class);
		when(mySrd.getInterceptorBroadcaster()).thenReturn(myInterceptorBroadcaster);
		when(mySrd.getServer().getPagingProvider()).thenReturn(new DatabaseBackedPagingProvider());

		when(myInterceptorBroadcaster.hasHooks(eq(Pointcut.JPA_PERFTRACE_WARNING))).thenReturn(true);
		when(myInterceptorBroadcaster.hasHooks(eq(Pointcut.JPA_PERFTRACE_INFO))).thenReturn(true);
		when(myInterceptorBroadcaster.callHooks(eq(Pointcut.JPA_PERFTRACE_INFO), ArgumentMatchers.any(HookParams.class))).thenAnswer(t -> {
			HookParams params = t.getArgument(1, HookParams.class);
			myMessages.add("INFO " + params.get(StorageProcessingMessage.class).getMessage());
			return null;
		});
		when(myInterceptorBroadcaster.callHooks(eq(Pointcut.JPA_PERFTRACE_WARNING), ArgumentMatchers.any(HookParams.class))).thenAnswer(t -> {
			HookParams params = t.getArgument(1, HookParams.class);
			myMessages.add("WARN " + params.get(StorageProcessingMessage.class).getMessage());
			return null;
		});
		when(myInterceptorBroadcaster.callHooks(eq(Pointcut.JPA_PERFTRACE_SEARCH_REUSING_CACHED), ArgumentMatchers.any(HookParams.class))).thenAnswer(t -> {
			HookParams params = t.getArgument(1, HookParams.class);
			myMessages.add("REUSING CACHED SEARCH");
			return null;
		});
	}

	@AfterEach
	public void after() throws Exception {
		myModelConfig.setDefaultSearchParamsCanBeOverridden(new ModelConfig().isDefaultSearchParamsCanBeOverridden());
		myDaoConfig.setUniqueIndexesCheckedBeforeSave(new DaoConfig().isUniqueIndexesCheckedBeforeSave());
		myDaoConfig.setSchedulingDisabled(new DaoConfig().isSchedulingDisabled());
		myDaoConfig.setUniqueIndexesEnabled(new DaoConfig().isUniqueIndexesEnabled());
		myDaoConfig.setReindexThreadCount(new DaoConfig().getReindexThreadCount());

		ResourceReindexingSvcImpl svc = SpringObjectCaster.getTargetObject(myResourceReindexingSvc, ResourceReindexingSvcImpl.class);
		svc.initExecutor();
	}

	protected void logCapturedMessages() {
		ourLog.info("Messages:\n  {}", String.join("\n  ", myMessages));
	}

}
