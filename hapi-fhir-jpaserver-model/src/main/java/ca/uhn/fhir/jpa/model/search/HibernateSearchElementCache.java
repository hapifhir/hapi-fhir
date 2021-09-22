package ca.uhn.fhir.jpa.model.search;

import org.hibernate.search.engine.backend.document.DocumentElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HibernateSearchElementCache {
	private static final Logger ourLog = LoggerFactory.getLogger(HibernateSearchElementCache.class);
	final DocumentElement myRoot;
	final Map<String, DocumentElement> myCache = new HashMap<>();

	public HibernateSearchElementCache(DocumentElement theRoot) {
		this.myRoot = theRoot;
	}

	public DocumentElement getNode(@Nonnull String... path) {
		return getNode(Arrays.asList(path));
	}

	public DocumentElement getNode(@Nonnull List<String> thePath) {
		if (thePath.size() == 0) {
			return myRoot;
		}
		String key = String.join(".", thePath);
		// re-implement computeIfAbsent since we're recursive, and it isn't rentrant.
		DocumentElement result = myCache.get(key);
		if (result == null) {
			DocumentElement parent = getNode(thePath.subList(0, thePath.size() - 1));
			result = parent.addObject(thePath.get(thePath.size() - 1));
			myCache.put(key, result);
		}
		ourLog.trace("getNode {}: {}", key, result);
		return result;
	}
}
