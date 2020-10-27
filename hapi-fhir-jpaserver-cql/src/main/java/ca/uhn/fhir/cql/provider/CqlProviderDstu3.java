package ca.uhn.fhir.cql.provider;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.provider.BaseJpaResourceProvider;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.provider.dstu3.JpaSystemProviderDstu3;
import ca.uhn.fhir.jpa.rp.dstu3.LibraryResourceProvider;
import ca.uhn.fhir.jpa.rp.dstu3.MeasureResourceProvider;
import ca.uhn.fhir.jpa.rp.dstu3.ValueSetResourceProvider;
import ca.uhn.fhir.jpa.term.api.ITermReadSvcDstu3;
import ca.uhn.fhir.rest.annotation.Initialize;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.IRestfulServerDefaults;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import ca.uhn.fhir.util.ReflectionUtil;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.dstu3.model.ActivityDefinition;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.Endpoint;
import org.hl7.fhir.dstu3.model.Library;
import org.hl7.fhir.dstu3.model.Measure;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.dstu3.model.PlanDefinition;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.opencds.cqf.common.config.HapiProperties;
import org.opencds.cqf.common.evaluation.EvaluationProviderFactory;
import org.opencds.cqf.common.retrieve.JpaFhirRetrieveProvider;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterResolver;
import org.opencds.cqf.dstu3.evaluation.ProviderFactory;
import org.opencds.cqf.dstu3.providers.ActivityDefinitionApplyProvider;
import org.opencds.cqf.dstu3.providers.ApplyCqlOperationProvider;
import org.opencds.cqf.dstu3.providers.CacheValueSetsProvider;
import org.opencds.cqf.dstu3.providers.CodeSystemUpdateProvider;
import org.opencds.cqf.dstu3.providers.CqlExecutionProvider;
import org.opencds.cqf.dstu3.providers.HQMFProvider;
import org.opencds.cqf.dstu3.providers.JpaTerminologyProvider;
import org.opencds.cqf.dstu3.providers.LibraryOperationsProvider;
import org.opencds.cqf.dstu3.providers.MeasureOperationsProvider;
import org.opencds.cqf.dstu3.providers.ObservationProvider;
import org.opencds.cqf.dstu3.providers.PlanDefinitionApplyProvider;
import org.opencds.cqf.dstu3.providers.QuestionnaireProvider;
import org.opencds.cqf.dstu3.servlet.CdsHooksServlet;
import org.opencds.cqf.tooling.library.stu3.NarrativeProvider;
import org.opencds.cqf.tooling.measure.stu3.CodeTerminologyRef;
import org.opencds.cqf.tooling.measure.stu3.CqfMeasure;
import org.opencds.cqf.tooling.measure.stu3.PopulationCriteriaMap;
import org.opencds.cqf.tooling.measure.stu3.VersionedTerminologyRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CqlProviderDstu3 {
	private static final Logger myLogger = LoggerFactory.getLogger(CqlProviderDstu3.class);

	private ApplicationContext myApplicationContext;
	private DaoRegistry myDaoRegistry;
	private FhirContext myFhirContext;
	HQMFProvider myHQMFProvider;
	MeasureResourceProvider myMeasureResourceProvider;
	NarrativeProvider myNarrativeProvider;
	EvaluationProviderFactory myEvaluationProviderFactory;
	LibraryOperationsProvider myLibraryOperationsProvider;

	// TODO KBD Move To Base Class
	private final List<IResourceProvider> myResourceProviders = new ArrayList<>();
	private final List<Object> myPlainProviders = new ArrayList<>();
	private Lock myProviderRegistrationMutex = new ReentrantLock();
	private boolean myStarted;

	public CqlProviderDstu3(ApplicationContext theApplicationContext) {
		myApplicationContext = theApplicationContext;
		initialize();
	}

	private void initialize() {
		myNarrativeProvider = new NarrativeProvider();
		myHQMFProvider = new HQMFProvider();
		myMeasureResourceProvider = new MeasureResourceProvider();

		// TODO KBD Delete this unused code
//		IFhirSystemDao<Bundle, Meta> systemDao = myApplicationContext.getBean("mySystemDaoDstu3",
//			IFhirSystemDao.class);
		myDaoRegistry = myApplicationContext.getBean(DaoRegistry.class);

		myFhirContext = myApplicationContext.getBean(FhirContext.class);
		myFhirContext.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		myFhirContext.registerCustomType(VersionedTerminologyRef.class);
		myFhirContext.registerCustomType(CodeTerminologyRef.class);
		myFhirContext.registerCustomType(PopulationCriteriaMap.class);
		myFhirContext.registerCustomType(CqfMeasure.class);

//		JpaTerminologyProvider localSystemTerminologyProvider = new JpaTerminologyProvider(
//			myApplicationContext.getBean("terminologyService", ITermReadSvcDstu3.class), myFhirContext,
//			(ValueSetResourceProvider) this.getResourceProvider(ValueSet.class));
//
//		myEvaluationProviderFactory = new ProviderFactory(myFhirContext, myDaoRegistry,
//			localSystemTerminologyProvider);

		// System and Resource Providers
//		Object systemProvider = myApplicationContext.getBean("mySystemProviderDstu3", JpaSystemProviderDstu3.class);
//		registerProvider(systemProvider);

//		ResourceProviderFactory resourceProviders = myApplicationContext.getBean("myResourceProvidersDstu3",
//			ResourceProviderFactory.class);
//		registerProviders(resourceProviders.createProviders());

		registerProvider(myApplicationContext.getBean(TerminologyUploaderProvider.class));

		// Code System Update
//		CodeSystemUpdateProvider csUpdate = new CodeSystemUpdateProvider(myDaoRegistry.getResourceDao(ValueSet.class),
//					myDaoRegistry.getResourceDao(CodeSystem.class));
//		this.registerProvider(csUpdate);

		// Cache Value Sets
//		CacheValueSetsProvider cvs = new CacheValueSetsProvider(myDaoRegistry.getSystemDao(),
//				myDaoRegistry.getResourceDao(Endpoint.class));
//		this.registerProvider(cvs);

		// Library processing
//		NarrativeProvider narrativeProvider = new NarrativeProvider();
//		myLibraryOperationsProvider = new LibraryOperationsProvider(
//			(LibraryResourceProvider) this.getResourceProvider(Library.class), narrativeProvider);
//		this.registerProvider(myLibraryOperationsProvider);

		// CQL Execution
		CqlExecutionProvider cql = new CqlExecutionProvider(myLibraryOperationsProvider, myEvaluationProviderFactory);
		this.registerProvider(cql);

		// java.lang.AssertionError: Type of class org.opencds.cqf.tooling.measure.stu3.TerminologyRef shouldn't be here
		// Bundle processing
//		ApplyCqlOperationProvider bundleProvider = new ApplyCqlOperationProvider(myEvaluationProviderFactory,
//			myDaoRegistry.getResourceDao(Bundle.class), myFhirContext);
//		this.registerProvider(bundleProvider);

		// Measure processing
//		MeasureOperationsProvider measureProvider = new MeasureOperationsProvider(myDaoRegistry,
//			myEvaluationProviderFactory, narrativeProvider, myHQMFProvider, myLibraryOperationsProvider,
//			(MeasureResourceProvider) this.getResourceProvider(Measure.class));
//		this.registerProvider(measureProvider);

		// java.lang.AssertionError: Type of class org.opencds.cqf.tooling.measure.stu3.TerminologyRef shouldn't be here
		// ActivityDefinition processing
//		ActivityDefinitionApplyProvider actDefProvider = new ActivityDefinitionApplyProvider(myFhirContext, cql,
//			myDaoRegistry.getResourceDao(ActivityDefinition.class));
//		this.registerProvider(actDefProvider);

		JpaFhirRetrieveProvider localSystemRetrieveProvider = new JpaFhirRetrieveProvider(myDaoRegistry,
			new SearchParameterResolver(myFhirContext));

		// PlanDefinition processing
//		PlanDefinitionApplyProvider planDefProvider = new PlanDefinitionApplyProvider(myFhirContext, actDefProvider,
//			myDaoRegistry.getResourceDao(PlanDefinition.class), myDaoRegistry.getResourceDao(ActivityDefinition.class), cql);
//		this.registerProvider(planDefProvider);

//		CdsHooksServlet.setPlanDefinitionProvider(planDefProvider);
		CdsHooksServlet.setLibraryResolutionProvider(myLibraryOperationsProvider);
		//CdsHooksServlet.setSystemTerminologyProvider(localSystemTerminologyProvider);
		CdsHooksServlet.setSystemRetrieveProvider(localSystemRetrieveProvider);

		// QuestionnaireResponse processing
		if(HapiProperties.getQuestionnaireResponseExtractEnabled()) {
			QuestionnaireProvider questionnaireProvider = new QuestionnaireProvider(myFhirContext);
			this.registerProvider(questionnaireProvider);
		}
		// Observation processing
		if(HapiProperties.getObservationTransformEnabled()) {
			ObservationProvider observationProvider = new ObservationProvider(myFhirContext);
			this.registerProvider(observationProvider);
		}
	}

	public MeasureOperationsProvider getMeasureOperationsProvider () {
		return new MeasureOperationsProvider(myDaoRegistry, myEvaluationProviderFactory, myNarrativeProvider, myHQMFProvider, myLibraryOperationsProvider, myMeasureResourceProvider);
	}

	// TODO KBD Move To Base Class
	@SuppressWarnings("unchecked")
	protected <T extends IBaseResource> BaseJpaResourceProvider<T> getResourceProvider(Class<T> clazz) {
		return (BaseJpaResourceProvider<T>) this.getResourceProviders().stream()
			.filter(x -> x.getResourceType().getSimpleName().equals(clazz.getSimpleName())).findFirst().get();
	}

	// TODO KBD Move To Base Class
	public Collection<IResourceProvider> getResourceProviders() {
		return myResourceProviders;
	}

	// TODO KBD Move To Base Class
	public void registerProvider(Object provider) {
		if (provider != null) {
			Collection<Object> providerList = new ArrayList<>(1);
			providerList.add(provider);
			registerProviders(providerList);
		}
	}

	// TODO KBD Move To Base Class
	public void registerProviders(Object... theProviders) {
		Validate.noNullElements(theProviders);
		registerProviders(Arrays.asList(theProviders));
	}

	// TODO KBD Move To Base Class
	public void registerProviders(Collection<?> theProviders) {
		Validate.noNullElements(theProviders, "theProviders must not contain any null elements");

		myProviderRegistrationMutex.lock();
		try {
			if (!myStarted) {
				for (Object provider : theProviders) {
					myLogger.debug("Registration of provider [" + provider.getClass().getName() + "] will be delayed until FHIR server startup");
					if (provider instanceof IResourceProvider) {
						myResourceProviders.add((IResourceProvider) provider);
					} else {
						myPlainProviders.add(provider);
					}
				}
				return;
			}
		} finally {
			myProviderRegistrationMutex.unlock();
		}
		registerProviders(theProviders, false);
	}

	// TODO KBD Move To Base Class
	private void findResourceMethods(Object theProvider) {
		myLogger.debug("Scanning type for RESTful methods: {}", theProvider.getClass());
		int count = 0;

		Class<?> clazz = theProvider.getClass();
		Class<?> supertype = clazz.getSuperclass();
		while (!Object.class.equals(supertype)) {
			//count += findResourceMethods(theProvider, supertype);
			count += findResourceMethodsOnInterfaces(theProvider, supertype.getInterfaces());
			supertype = supertype.getSuperclass();
		}

		try {
			//count += findResourceMethods(theProvider, clazz);
			count += findResourceMethodsOnInterfaces(theProvider, clazz.getInterfaces());
		} catch (ConfigurationException e) {
			throw new ConfigurationException("Failure scanning class " + clazz.getSimpleName() + ": " + e.getMessage(), e);
		}
		if (count == 0) {
			throw new ConfigurationException("Did not find any annotated RESTful methods on provider class " + theProvider.getClass().getName());
		}
	}

	// TODO KBD Move To Base Class
	private int findResourceMethodsOnInterfaces(Object theProvider, Class<?>[] theInterfaces) {
		int count = 0;
		for (Class<?> anInterface : theInterfaces) {
			//count += findResourceMethods(theProvider, anInterface);
			count += findResourceMethodsOnInterfaces(theProvider, anInterface.getInterfaces());
		}
		return count;
	}

	// TODO KBD Move To Base Class
//	private int findResourceMethods(Object theProvider, Class<?> clazz) throws ConfigurationException {
//		int count = 0;
//
//		for (Method m : ReflectionUtil.getDeclaredMethods(clazz)) {
//			BaseMethodBinding<?> foundMethodBinding = BaseMethodBinding.bindMethod(m, myFhirContext, theProvider);
//			if (foundMethodBinding == null) {
//				continue;
//			}
//
//			count++;
//
//			if (foundMethodBinding instanceof ConformanceMethodBinding) {
//				myServerConformanceMethod = foundMethodBinding;
//				continue;
//			}
//
//			if (!Modifier.isPublic(m.getModifiers())) {
//				throw new ConfigurationException("Method '" + m.getName() + "' is not public, FHIR RESTful methods must be public");
//			}
//			if (Modifier.isStatic(m.getModifiers())) {
//				throw new ConfigurationException("Method '" + m.getName() + "' is static, FHIR RESTful methods must not be static");
//			}
//			myLogger.debug("Scanning public method: {}#{}", theProvider.getClass(), m.getName());
//
//			String resourceName = foundMethodBinding.getResourceName();
//			ResourceBinding resourceBinding;
//			if (resourceName == null) {
//				if (foundMethodBinding.isGlobalMethod()) {
//					resourceBinding = myGlobalBinding;
//				} else {
//					resourceBinding = myServerBinding;
//				}
//			} else {
//				RuntimeResourceDefinition definition = getFhirContext().getResourceDefinition(resourceName);
//				if (myResourceNameToBinding.containsKey(definition.getName())) {
//					resourceBinding = myResourceNameToBinding.get(definition.getName());
//				} else {
//					resourceBinding = new ResourceBinding();
//					resourceBinding.setResourceName(resourceName);
//					myResourceNameToBinding.put(resourceName, resourceBinding);
//				}
//			}
//
//			List<Class<?>> allowableParams = foundMethodBinding.getAllowableParamAnnotations();
//			if (allowableParams != null) {
//				for (Annotation[] nextParamAnnotations : m.getParameterAnnotations()) {
//					for (Annotation annotation : nextParamAnnotations) {
//						Package pack = annotation.annotationType().getPackage();
//						if (pack.equals(IdParam.class.getPackage())) {
//							if (!allowableParams.contains(annotation.annotationType())) {
//								throw new ConfigurationException("Method[" + m.toString() + "] is not allowed to have a parameter annotated with " + annotation);
//							}
//						}
//					}
//				}
//			}
//
//			resourceBinding.addMethod(foundMethodBinding);
//			ourLog.debug(" * Method: {}#{} is a handler", theProvider.getClass(), m.getName());
//
//		}
//
//		return count;
//	}

	// TODO KBD Move To Base Class
	private void invokeInitialize(Object theProvider) {
		invokeInitialize(theProvider, theProvider.getClass());
	}

	// TODO KBD Move To Base Class
	private void invokeInitialize(Object theProvider, Class<?> clazz) {
		for (Method m : ReflectionUtil.getDeclaredMethods(clazz)) {
			Initialize initialize = m.getAnnotation(Initialize.class);
			if (initialize != null) {
				invokeInitializeOrDestroyMethod(theProvider, m, "initialize");
			}
		}

		Class<?> supertype = clazz.getSuperclass();
		if (!Object.class.equals(supertype)) {
			invokeInitialize(theProvider, supertype);
		}
	}

	// TODO KBD Move To Base Class
	private void invokeInitializeOrDestroyMethod(Object theProvider, Method m, String theMethodDescription) {

		Class<?>[] paramTypes = m.getParameterTypes();
		Object[] params = new Object[paramTypes.length];

		int index = 0;
		for (Class<?> nextParamType : paramTypes) {

			if (RestfulServer.class.equals(nextParamType) || IRestfulServerDefaults.class.equals(nextParamType)) {
				params[index] = this;
			}

			index++;
		}

		try {
			m.invoke(theProvider, params);
		} catch (Exception e) {
			myLogger.error("Exception occurred in " + theMethodDescription + " method '" + m.getName() + "'", e);
		}
	}

	// TODO KBD Move To Base Class
	protected void registerProviders(Collection<?> theProviders, boolean inInit) {
		Validate.noNullElements(theProviders, "theProviders must not contain any null elements");

		List<IResourceProvider> newResourceProviders = new ArrayList<>();
		List<Object> newPlainProviders = new ArrayList<>();

		if (theProviders != null) {
			for (Object provider : theProviders) {
				if (provider instanceof IResourceProvider) {
					IResourceProvider rsrcProvider = (IResourceProvider) provider;
					Class<? extends IBaseResource> resourceType = rsrcProvider.getResourceType();
					if (resourceType == null) {
						throw new NullPointerException("getResourceType() on class '" + rsrcProvider.getClass().getCanonicalName() + "' returned null");
					}
					if (!inInit) {
						myResourceProviders.add(rsrcProvider);
					}
					newResourceProviders.add(rsrcProvider);
				} else {
					if (!inInit) {
						myPlainProviders.add(provider);
					}
					newPlainProviders.add(provider);
				}

			}
			if (!newResourceProviders.isEmpty()) {
				myLogger.info("Added {} resource provider(s). Total {}", newResourceProviders.size(), myResourceProviders.size());
				for (IResourceProvider provider : newResourceProviders) {
					findResourceMethods(provider);
				}
			}
			if (!newPlainProviders.isEmpty()) {
				myLogger.info("Added {} plain provider(s). Total {}", newPlainProviders.size(), myPlainProviders.size());
				for (Object provider : newPlainProviders) {
					findResourceMethods(provider);
				}
			}
			if (!inInit) {
				myLogger.trace("Invoking provider initialize methods");
				if (!newResourceProviders.isEmpty()) {
					for (IResourceProvider provider : newResourceProviders) {
						invokeInitialize(provider);
					}
				}
				if (!newPlainProviders.isEmpty()) {
					for (Object provider : newPlainProviders) {
						invokeInitialize(provider);
					}
				}
			}
		}
	}
}
