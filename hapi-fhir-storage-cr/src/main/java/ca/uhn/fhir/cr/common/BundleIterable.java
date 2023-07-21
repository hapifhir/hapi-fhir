package ca.uhn.fhir.cr.common;

import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.opencds.cqf.fhir.api.Repository;

import java.util.Iterator;
import java.util.List;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * This class leverages IBundleProvider Iterable to provide an iterator for processing bundle search results into manageable paginated chunks. This helped to avoid loading large result sets into lists
 **/
@NotThreadSafe
public class BundleIterable implements Iterable<IBaseResource> {

	private final IBundleProvider myBundleProvider;
	private final Repository myRepository;

	public BundleIterable(Repository theRepository, IBundleProvider theBundleProvider) {
		this.myBundleProvider = theBundleProvider;
		this.myRepository = theRepository;
	}

	@Override
	public Iterator<IBaseResource> iterator() {
		return new BundleIterator(myRepository, myBundleProvider);
	}

	static class BundleIterator implements Iterator<IBaseResource> {

		private IBundleProvider myBundleProvider;

		private int offset = 0;
		private int increment = 50;
		private List<IBaseResource> myCurrentResourceList;

		private final Repository myRepository;

		private int currentResourceListIndex = 0;

		public BundleIterator(Repository theRepository, IBundleProvider theBundleProvider) {
			this.myBundleProvider = theBundleProvider;
			this.myRepository = theRepository;
			initChunk();
		}

		private void initChunk() {
			this.myCurrentResourceList = this.myBundleProvider.getResources(offset, increment + offset);
			// next offset created
			offset += increment;
			// restart counter on new chunk
			currentResourceListIndex = 0;
		}

		private void loadNextChunk() {
			initChunk();
		}

		@Override
		public boolean hasNext() {
			// We still have things in the current chunk to return
			if (this.currentResourceListIndex < this.myCurrentResourceList.size()) {
				return true;
			} else if (this.myCurrentResourceList.size() == 0) {
				// no more resources!
				return false;
			}

			// We need our next chunk
			this.loadNextChunk();
			return this.hasNext();
		}

		@Override
		public IBaseResource next() {
			assert this.currentResourceListIndex < this.myCurrentResourceList.size();

			var result = this.myCurrentResourceList.get(this.currentResourceListIndex);
			this.currentResourceListIndex++;
			return result;
		}
	}
}
