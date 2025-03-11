package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.search.reindex.ResourceReindexingSvcImpl;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.util.ComboSearchParameterTestHelper;
import ca.uhn.fhir.jpa.util.SpringObjectCaster;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.test.utilities.MockInvoker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
	protected ComboSearchParameterTestHelper myComboSearchParameterTestHelper;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myStorageSettings.setMarkResourcesForReindexingUponSearchParameterChange(false);
		myStorageSettings.setDefaultSearchParamsCanBeOverridden(true);
		myStorageSettings.setSchedulingDisabled(true);
		myStorageSettings.setUniqueIndexesEnabled(true);

		myInterceptorBroadcaster = mock(IInterceptorBroadcaster.class);
		when(mySrd.getInterceptorBroadcaster()).thenReturn(myInterceptorBroadcaster);
		when(mySrd.getServer().getPagingProvider()).thenReturn(new DatabaseBackedPagingProvider());

		when(myInterceptorBroadcaster.hasHooks(eq(Pointcut.JPA_PERFTRACE_WARNING))).thenReturn(true);
		when(myInterceptorBroadcaster.hasHooks(eq(Pointcut.JPA_PERFTRACE_INFO))).thenReturn(true);
		when(myInterceptorBroadcaster.hasHooks(eq(Pointcut.JPA_PERFTRACE_SEARCH_REUSING_CACHED))).thenReturn(true);
		when(myInterceptorBroadcaster.hasHooks(eq(Pointcut.STORAGE_PRECHECK_FOR_CACHED_SEARCH))).thenReturn(true);
		when(myInterceptorBroadcaster.getInvokersForPointcut(eq(Pointcut.JPA_PERFTRACE_INFO))).thenReturn(MockInvoker.list(params->{
			myMessages.add("INFO " + params.get(StorageProcessingMessage.class).getMessage());
		}));


		when(myInterceptorBroadcaster.getInvokersForPointcut(eq(Pointcut.JPA_PERFTRACE_WARNING))).thenReturn(MockInvoker.list(params->{
			myMessages.add("WARN " + params.get(StorageProcessingMessage.class).getMessage());
		}));
		when(myInterceptorBroadcaster.getInvokersForPointcut(eq(Pointcut.JPA_PERFTRACE_SEARCH_REUSING_CACHED))).thenReturn(MockInvoker.list(params->{
			myMessages.add("REUSING CACHED SEARCH");
		}));

		// allow searches to use cached results
		when(myInterceptorBroadcaster.getInvokersForPointcut(eq(Pointcut.STORAGE_PRECHECK_FOR_CACHED_SEARCH))).thenReturn(MockInvoker.list(params->true));
		myComboSearchParameterTestHelper = new ComboSearchParameterTestHelper(mySearchParameterDao, mySearchParamRegistry);
	}

	@AfterEach
	public void after() throws Exception {
		myStorageSettings.setDefaultSearchParamsCanBeOverridden(new JpaStorageSettings().isDefaultSearchParamsCanBeOverridden());
		myStorageSettings.setUniqueIndexesCheckedBeforeSave(new JpaStorageSettings().isUniqueIndexesCheckedBeforeSave());
		myStorageSettings.setSchedulingDisabled(new JpaStorageSettings().isSchedulingDisabled());
		myStorageSettings.setUniqueIndexesEnabled(new JpaStorageSettings().isUniqueIndexesEnabled());
		myStorageSettings.setReindexThreadCount(new JpaStorageSettings().getReindexThreadCount());

		ResourceReindexingSvcImpl svc = SpringObjectCaster.getTargetObject(myResourceReindexingSvc, ResourceReindexingSvcImpl.class);
		svc.initExecutor();
	}

	protected void logCapturedMessages() {
		ourLog.info("Messages:\n  {}", String.join("\n  ", myMessages));
	}

	protected void createBirthdateAndGenderSps(boolean theUnique) {
		myComboSearchParameterTestHelper.createBirthdateAndGenderSps(theUnique);
		myMessages.clear();
	}

}
