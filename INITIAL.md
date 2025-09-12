We have a performance bug while processing Synthea-generated transaction bundles.
Synthea is a synthetic test data generator for FHIR.
Synthea test data is structured as one bundle per patient, along with two extra bundles: hospitals (Organizations and Locations), and practitioners (Practitioner resources).
The references from the patient bundle resources to the static hospital and practitioner resources use logical references (i.e. a reference like Practitioner?identifier=abc123) instead of literal id references.
We resolve these references is a batch during the TransactionProcessor pre-fetch.
During the processing, we replace these references with the literal id references.
But during indexing, we are seeing that the indexing pass has to resolve these ids - i.e. use the fhir id to look up the resource res_id primary key using the IdHelperService.
This is causing much slower performance.
See this stack trace for where this happens:
```
at ca.uhn.fhir.jpa.dao.index.IdHelperService.resolveResourceIdentitiesForFhirIdsUsingDatabase(IdHelperService.java:326)
at ca.uhn.fhir.jpa.dao.index.IdHelperService.resolveResourceIdentities(IdHelperService.java:228)
at ca.uhn.fhir.jpa.dao.index.IdHelperService.resolveResourceIdentity(IdHelperService.java:169)
at ca.uhn.fhir.jpa.dao.index.DaoResourceLinkResolver.findTargetResource(DaoResourceLinkResolver.java:146)
at ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorService.resolveTargetAndCreateResourceLinkOrReturnNull(SearchParamExtractorService.java:992)
at ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorService.extractResourceLinks(SearchParamExtractorService.java:760)
at ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorService.extractResourceLinks(SearchParamExtractorService.java:574)
at ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorService.extractFromResource(SearchParamExtractorService.java:178)
at ca.uhn.fhir.jpa.dao.index.SearchParamWithInlineReferencesExtractor.populateFromResource(SearchParamWithInlineReferencesExtractor.java:71)
at ca.uhn.fhir.jpa.dao.BaseHapiFhirDao.updateEntity(BaseHapiFhirDao.java:969)
at ca.uhn.fhir.jpa.dao.BaseHapiFhirDao.updateEntity(BaseHapiFhirDao.java:166)
at java.lang.invoke.LambdaForm$DMH/0x00000070023d1000.invokeInterface(LambdaForm$DMH:-1)
at java.lang.invoke.LambdaForm$MH/0x00000070023d9400.invoke(LambdaForm$MH:-1)
at java.lang.invoke.LambdaForm$MH/0x00000070013dc000.invoke(LambdaForm$MH:-1)
at java.lang.invoke.Invokers$Holder.invokeExact_MT(Invokers$Holder:-1)
at jdk.internal.reflect.DirectMethodHandleAccessor.invokeImpl(DirectMethodHandleAccessor.java:157)
at jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)
at java.lang.reflect.Method.invoke(Method.java:580)
at org.springframework.aop.support.AopUtils.invokeJoinpointUsingReflection(AopUtils.java:355)
at org.springframework.aop.framework.ReflectiveMethodInvocation.invokeJoinpoint(ReflectiveMethodInvocation.java:196)
at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)
at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:138)
at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:184)
at org.springframework.aop.framework.JdkDynamicAopProxy.invoke(JdkDynamicAopProxy.java:223)
at jdk.proxy1.$Proxy290.updateEntity(Unknown Source:-1)
at ca.uhn.fhir.jpa.dao.BaseTransactionProcessor.resolveReferencesThenSaveAndIndexResource(BaseTransactionProcessor.java:2093)
at ca.uhn.fhir.jpa.dao.BaseTransactionProcessor.resolveReferencesThenSaveAndIndexResources(BaseTransactionProcessor.java:1893)
at ca.uhn.fhir.jpa.dao.BaseTransactionProcessor.doTransactionWriteOperations(BaseTransactionProcessor.java:1600)
at ca.uhn.fhir.jpa.dao.TransactionProcessor.doTransactionWriteOperations(TransactionProcessor.java:203)
at ca.uhn.fhir.jpa.dao.BaseTransactionProcessor.lambda$prepareThenExecuteTransactionWriteOperations$1(BaseTransactionProcessor.java:856)
at ca.uhn.fhir.jpa.dao.BaseTransactionProcessor$$Lambda/0x0000007002399388.doInTransaction(Unknown Source:-1)
at org.springframework.transaction.support.TransactionTemplate.execute(TransactionTemplate.java:140)
at ca.uhn.fhir.jpa.dao.tx.HapiTransactionService.doExecuteCallback(HapiTransactionService.java:436)
at ca.uhn.fhir.jpa.dao.tx.HapiTransactionService.doExecuteInTransaction(HapiTransactionService.java:343)
at ca.uhn.fhir.jpa.dao.tx.HapiTransactionService.doExecute(HapiTransactionService.java:280)
at ca.uhn.fhir.jpa.dao.tx.HapiTransactionService$ExecutionBuilder.execute(HapiTransactionService.java:552)
at ca.uhn.fhir.jpa.dao.BaseTransactionProcessor.prepareThenExecuteTransactionWriteOperations(BaseTransactionProcessor.java:881)
at ca.uhn.fhir.jpa.dao.BaseTransactionProcessor.processTransaction(BaseTransactionProcessor.java:638)
at ca.uhn.fhir.jpa.dao.BaseTransactionProcessor.processTransactionAsSubRequest(BaseTransactionProcessor.java:390)
at ca.uhn.fhir.jpa.dao.BaseTransactionProcessor.transaction(BaseTransactionProcessor.java:236)
at ca.uhn.fhir.jpa.dao.BaseHapiFhirSystemDao.transaction(BaseHapiFhirSystemDao.java:168)
at ca.uhn.fhir.jpa.dao.BaseHapiFhirSystemDao.transaction(BaseHapiFhirSystemDao.java:70)
```

The goal is to make sure these ids are added to the TransactionDetails cache when they are resolved so that the indexing path can resolve them without going to the db.

