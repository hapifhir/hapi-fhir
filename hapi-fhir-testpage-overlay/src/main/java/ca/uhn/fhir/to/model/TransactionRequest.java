package ca.uhn.fhir.to.model;

import org.springframework.web.bind.annotation.ModelAttribute;

public class TransactionRequest extends HomeRequest {

	private String myTransactionBody;

	@ModelAttribute("transactionBody")
	public String getTransactionBody() {
		return myTransactionBody;
	}

	public void setTransactionBody(String theTransactionBody) {
		myTransactionBody = theTransactionBody;
	}
	
}
