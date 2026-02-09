package ca.uhn.fhir.storage.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.HashingWriter;
import com.google.common.hash.HashCode;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

@Interceptor
public class BlockDuplicateCreatesInterceptor {

	private final Object myLock = new Object();
	private final Deque<HashCode> myResourceHashDeque = new ArrayDeque<>();
	private final Set<HashCode> myResourceHashSet = new HashSet<>();

	private final IParser myParser;

	public BlockDuplicateCreatesInterceptor(FhirContext theFhirContext) {
		myParser = theFhirContext.newNDJsonParser();
		myParser.setDontEncodeElements("id", "meta", "text");
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
	public void onCreate(IBaseResource theResource) {
		HashCode hashCode;
		try (HashingWriter hashingWriter = new HashingWriter()) {
			hashingWriter.append(myParser, theResource);
			hashCode = hashingWriter.getHash();
		}

		synchronized (myLock) {
			boolean added = myResourceHashSet.add(hashCode);
			if (added) {
				myResourceHashDeque.add(hashCode);
				if (myResourceHashDeque.size() > 1000) {
					HashCode removed = myResourceHashDeque.removeFirst();
					myResourceHashSet.remove(removed);
				}
			} else {
				throw new Precon
			}
		}
	}

}
