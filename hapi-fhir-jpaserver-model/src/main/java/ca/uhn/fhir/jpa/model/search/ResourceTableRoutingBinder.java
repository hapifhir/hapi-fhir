package ca.uhn.fhir.jpa.model.search;

import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import org.hibernate.search.mapper.pojo.bridge.RoutingBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.RoutingBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.RoutingBinder;
import org.hibernate.search.mapper.pojo.bridge.runtime.RoutingBridgeRouteContext;
import org.hibernate.search.mapper.pojo.route.DocumentRoutes;

public class ResourceTableRoutingBinder implements RoutingBinder {
	@Override
	public void bind(RoutingBindingContext theRoutingBindingContext) {
		theRoutingBindingContext.dependencies().use("myDeleted").use("myIndexStatus");
		theRoutingBindingContext.bridge(ResourceTable.class, new ResourceTableBridge());
	}

	private static class ResourceTableBridge implements RoutingBridge<ResourceTable> {

		@Override
		public void route(DocumentRoutes theDocumentRoutes, Object theO, ResourceTable theResourceTable, RoutingBridgeRouteContext theRoutingBridgeRouteContext) {
			if (theResourceTable.getDeleted() == null && theResourceTable.getIndexStatus() != null ) {
				theDocumentRoutes.addRoute();
			} else {
				theDocumentRoutes.notIndexed();
			}
		}

		@Override
		public void previousRoutes(DocumentRoutes theDocumentRoutes, Object theO, ResourceTable theResourceTable, RoutingBridgeRouteContext theRoutingBridgeRouteContext) {
			theDocumentRoutes.addRoute();
		}
	}
}
