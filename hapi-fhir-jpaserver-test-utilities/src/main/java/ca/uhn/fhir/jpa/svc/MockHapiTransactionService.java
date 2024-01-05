/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.svc;

import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import jakarta.annotation.Nullable;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.SimpleTransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

public class MockHapiTransactionService extends HapiTransactionService {

	private TransactionStatus myTransactionStatus;

	public MockHapiTransactionService() {
		this(new SimpleTransactionStatus());
	}

	public MockHapiTransactionService(TransactionStatus theTransactionStatus) {
		myTransactionStatus = theTransactionStatus;
	}

	@Nullable
	@Override
	public <T> T doExecute(ExecutionBuilder theExecutionBuilder, TransactionCallback<T> theCallback) {
		return theCallback.doInTransaction(myTransactionStatus);
	}
}
