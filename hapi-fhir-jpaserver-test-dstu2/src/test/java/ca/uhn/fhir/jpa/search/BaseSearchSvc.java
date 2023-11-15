package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.search.builder.SearchBuilder;
import ca.uhn.fhir.jpa.svc.MockHapiTransactionService;
import ca.uhn.fhir.jpa.util.BaseIterator;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.BeanFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;

public class BaseSearchSvc {
	protected int myExpectedNumberOfSearchBuildersCreated = 2;
	@Mock
	protected SearchBuilderFactory<JpaPid> mySearchBuilderFactory;
	@Spy
	protected HapiTransactionService myTransactionService = new MockHapiTransactionService();
	@Mock
	protected SearchBuilder mySearchBuilder;

	@Mock
	protected IFhirResourceDao<?> myCallingDao;

	@Mock
	protected DaoRegistry myDaoRegistry;

	@Mock
	protected BeanFactory myBeanFactory;

	@Spy
	protected JpaStorageSettings myStorageSettings = new JpaStorageSettings();

	protected static final FhirContext ourCtx = FhirContext.forDstu3Cached();

	public void after() {
		verify(mySearchBuilderFactory, atMost(myExpectedNumberOfSearchBuildersCreated)).newSearchBuilder(any(), any(), any());
	}

	protected List<JpaPid> createPidSequence(int to) {
		List<JpaPid> pids = new ArrayList<>();
		for (long i = 10; i < to; i++) {
			pids.add(JpaPid.fromId(i));
		}
		return pids;
	}

	protected Answer<Void> loadPids() {
		return theInvocation -> {
			List<JpaPid> pids = (List<JpaPid>) theInvocation.getArguments()[0];
			List<IBaseResource> resources = (List<IBaseResource>) theInvocation.getArguments()[2];
			for (IResourcePersistentId nextPid : pids) {
				Patient pt = new Patient();
				pt.setId(nextPid.toString());
				resources.add(pt);
			}
			return null;
		};
	}

	public static class ResultIterator extends BaseIterator<JpaPid> implements IResultIterator<JpaPid> {

		private final Iterator<JpaPid> myWrap;
		private int myCount;

		ResultIterator(Iterator<JpaPid> theWrap) {
			myWrap = theWrap;
		}

		@Override
		public boolean hasNext() {
			return myWrap.hasNext();
		}

		@Override
		public JpaPid next() {
			myCount++;
			return myWrap.next();
		}

		@Override
		public int getSkippedCount() {
			return 0;
		}

		@Override
		public int getNonSkippedCount() {
			return myCount;
		}

		@Override
		public Collection<JpaPid> getNextResultBatch(long theBatchSize) {
			Collection<JpaPid> batch = new ArrayList<>();
			while (this.hasNext() && batch.size() < theBatchSize) {
				batch.add(this.next());
			}
			return batch;
		}

		@Override
		public void close() {
			// nothing
		}
	}
}
