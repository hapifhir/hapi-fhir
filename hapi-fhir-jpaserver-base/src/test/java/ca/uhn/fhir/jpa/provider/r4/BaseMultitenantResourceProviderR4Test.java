package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.partition.PartitionManagementProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.interceptor.CapturingInterceptor;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.client.interceptor.UrlTenantSelectionInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import ca.uhn.fhir.rest.server.interceptor.partition.RequestTenantPartitionInterceptor;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.tenant.UrlBaseTenantIdentificationStrategy;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static ca.uhn.fhir.jpa.model.util.JpaConstants.DEFAULT_PARTITION_NAME;

public abstract class BaseMultitenantResourceProviderR4Test extends BaseResourceProviderR4Test implements ITestDataBuilder {

	public static final int TENANT_A_ID = 1;
	public static final int TENANT_B_ID = 2;
	public static final String TENANT_B = "TENANT-B";
	public static final String TENANT_A = "TENANT-A";
	@Autowired
	private RequestTenantPartitionInterceptor myRequestTenantPartitionInterceptor;
	@Autowired
	private PartitionManagementProvider myPartitionManagementProvider;

	protected CapturingInterceptor myCapturingInterceptor;
	protected UrlTenantSelectionInterceptor myTenantClientInterceptor;
	protected AuthorizationInterceptor myAuthorizationInterceptor;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		myInterceptorRegistry.registerInterceptor(myRequestTenantPartitionInterceptor);
		myPartitionSettings.setPartitioningEnabled(true);

		ourRestServer.registerProvider(myPartitionManagementProvider);
		ourRestServer.setTenantIdentificationStrategy(new UrlBaseTenantIdentificationStrategy());

		myCapturingInterceptor = new CapturingInterceptor();
		myClient.getInterceptorService().registerInterceptor(myCapturingInterceptor);

		myTenantClientInterceptor = new UrlTenantSelectionInterceptor();
		myClient.getInterceptorService().registerInterceptor(myTenantClientInterceptor);

		myClient.getInterceptorService().registerInterceptor(new LoggingInterceptor());

		createTenants();
	}

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		myPartitionSettings.setPartitioningEnabled(new PartitionSettings().isPartitioningEnabled());
		ourRestServer.unregisterInterceptor(myRequestTenantPartitionInterceptor);
		if (myAuthorizationInterceptor != null) {
			ourRestServer.unregisterInterceptor(myAuthorizationInterceptor);
		}
		ourRestServer.unregisterProvider(myPartitionManagementProvider);
		ourRestServer.setTenantIdentificationStrategy(null);

		myClient.getInterceptorService().unregisterAllInterceptors();
	}

	@Override
	protected boolean shouldLogClient() {
		return true;
	}


	private void createTenants() {
		myTenantClientInterceptor.setTenantId(DEFAULT_PARTITION_NAME);

		myClient
			.operation()
			.onServer()
			.named(ProviderConstants.PARTITION_MANAGEMENT_CREATE_PARTITION)
			.withParameter(Parameters.class, ProviderConstants.PARTITION_MANAGEMENT_PARTITION_ID, new IntegerType(TENANT_A_ID))
			.andParameter(ProviderConstants.PARTITION_MANAGEMENT_PARTITION_NAME, new CodeType(TENANT_A))
			.execute();

		myClient
			.operation()
			.onServer()
			.named(ProviderConstants.PARTITION_MANAGEMENT_CREATE_PARTITION)
			.withParameter(Parameters.class, ProviderConstants.PARTITION_MANAGEMENT_PARTITION_ID, new IntegerType(TENANT_B_ID))
			.andParameter(ProviderConstants.PARTITION_MANAGEMENT_PARTITION_NAME, new CodeType(TENANT_B))
			.execute();
	}

	public void setupAuthorizationInterceptorWithRules(Supplier<List<IAuthRule>> theRuleSupplier) {
		if(myAuthorizationInterceptor != null) {
			ourRestServer.unregisterInterceptor(myAuthorizationInterceptor);
		}
		myAuthorizationInterceptor = new AuthorizationInterceptor() {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return theRuleSupplier.get();
			}
		}.setDefaultPolicy(PolicyEnum.DENY);
		ourRestServer.registerInterceptor(myAuthorizationInterceptor);
	}



	protected Consumer<IBaseResource> withTenant(String theTenantId) {
		return t -> myTenantClientInterceptor.setTenantId(theTenantId);
	}

	@Override
	public IIdType doCreateResource(IBaseResource theResource) {
		return myClient.create().resource(theResource).execute().getId();
	}

	@Override
	public IIdType doUpdateResource(IBaseResource theResource) {
		return myClient.update().resource(theResource).execute().getId();
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

}
