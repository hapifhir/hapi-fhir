package ca.uhn.fhirtest.mvc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import ca.uhn.fhir.to.BaseController;
import ca.uhn.fhir.to.model.HomeRequest;

@org.springframework.stereotype.Controller()
public class SubscriptionPlaygroundController extends BaseController {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SubscriptionPlaygroundController.class);

	@RequestMapping(value = { "/subscriptions" })
	public String subscriptionsHome(final HttpServletRequest theServletRequest, HomeRequest theRequest, final ModelMap theModel) {
		addCommonParams(theServletRequest, theRequest, theModel);

		theModel.put("notHome", true);
		theModel.put("extraBreadcrumb", "Subscriptions");

		ourLog.info(logPrefix(theModel) + "Displayed subscriptions playground page");

		return "subscriptions";
	}

}
