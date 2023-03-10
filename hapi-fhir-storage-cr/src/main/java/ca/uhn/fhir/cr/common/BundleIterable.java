package ca.uhn.fhir.cr.common;

import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IPagingProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Iterator;
import java.util.List;

public class BundleIterable implements Iterable<IBaseResource> {

	private final IBundleProvider sourceBundleProvider;
	private final IPagingProvider pagingProvider;

	private final RequestDetails requestDetails;

	private int currentPageIndex = 0;


	public BundleIterable(RequestDetails requestDetails, IBundleProvider bundleProvider, IPagingProvider pagingProvider) {
		this.sourceBundleProvider = bundleProvider;
		this.pagingProvider = pagingProvider;
		this.requestDetails = requestDetails;
	}

	@Override
	public Iterator<IBaseResource> iterator() {
		return new BundleIterator(this.requestDetails, this.sourceBundleProvider, this.pagingProvider);
	}

	static class BundleIterator implements Iterator<IBaseResource> {

		private IBundleProvider currentBundleProvider;
		private List<IBaseResource> currentResourceList;
		private final IPagingProvider pagingProvider;
		private final RequestDetails requestDetails;

		private int currentResourceListIndex = 0;


		public BundleIterator(RequestDetails requestDetails, IBundleProvider bundleProvider, IPagingProvider pagingProvider) {
			this.currentBundleProvider = bundleProvider;
			this.pagingProvider = pagingProvider;
			this.requestDetails = requestDetails;
			initPage();
		}

		private void initPage() {
			var size = this.currentBundleProvider.getCurrentPageSize();
			this.currentResourceList = this.currentBundleProvider.getResources(0, size != null ? size : 10000);
			currentResourceListIndex = 0;
		}

		private void loadNextPage() {
			currentBundleProvider = this.pagingProvider.retrieveResultList(this.requestDetails, this.currentBundleProvider.getUuid(), this.currentBundleProvider.getNextPageId());
			initPage();
		}

		@Override
		public boolean hasNext() {
			// We still have things in the current page to return, so we have a next.
			if (this.currentResourceListIndex < this.currentResourceList.size()) {
				return true;
			}

			// We're at the end of the current page, and there's no next page.
			if (this.currentBundleProvider.getNextPageId() == null) {
				return false;
			}

			// We have a next page, so let's load it.
			this.loadNextPage();
			;

			return this.hasNext();
		}


		@Override
		public IBaseResource next() {
			if (this.currentResourceListIndex >= this.currentResourceList.size()) {
				throw new RuntimeException("Shouldn't happen bruh");
			}

			var result = this.currentResourceList.get(this.currentResourceListIndex);
			this.currentResourceListIndex++;
			return result;
		}
	}
}

