package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TransactionUtils {

	public TransactionUtils() {}

	public static final String SEARCH_URL_IDS_KEY = "searchIdsToDelete";

	public static void addSearchUrlIdToTransactionDetails(
			TransactionDetails theTransactionDetails, ResourceTable theEntity) {
		List<Long> ids =
				theTransactionDetails.getOrCreateUserData(SEARCH_URL_IDS_KEY, (Supplier<List<Long>>) ArrayList::new);
		ids.add((Long) theEntity.getPersistentId().getId());
		theEntity.setSearchUrlPresent(false);
		//		myResourceSearchUrlSvc.deleteByResId(
		//			(Long) theEntity.getPersistentId().getId());
		//		entity.setSearchUrlPresent(false);
	}

	public static List<Long> getSearchResearchUrlIdsToDelete(TransactionDetails theTransactionDetails) {
		return theTransactionDetails.getUserData(SEARCH_URL_IDS_KEY);
	}
}
