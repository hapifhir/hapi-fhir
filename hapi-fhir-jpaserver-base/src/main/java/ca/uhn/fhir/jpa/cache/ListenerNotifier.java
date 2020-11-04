package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.model.primitive.IdDt;
import org.springframework.stereotype.Service;

@Service
public class ListenerNotifier {
	public long compareLastVersionMapToNewVersionMapAndNotifyListenerOfChanges(ResourceVersionCache theOldResourceVersionCache, ResourceVersionMap theNewResourceVersionMap, IVersionChangeListener theListener) {
		long count = 0;
		for (IdDt id : theNewResourceVersionMap.keySet()) {
			String previousValue = theOldResourceVersionCache.addOrUpdate(id, theNewResourceVersionMap.get(id));
			IdDt newId = id.withVersion(theNewResourceVersionMap.get(id));
			if (previousValue == null) {
				theListener.handleCreate(newId);
				++count;
			} else if (!theNewResourceVersionMap.get(id).equals(previousValue)) {
				theListener.handleUpdate(newId);
				++count;
			}
		}

		// Now check for deletes
		for (IdDt id : theOldResourceVersionCache.keySet()) {
			if (!theNewResourceVersionMap.containsKey(id)) {
				theListener.handleDelete(id);
			}
		}
		return count;
	}
}

