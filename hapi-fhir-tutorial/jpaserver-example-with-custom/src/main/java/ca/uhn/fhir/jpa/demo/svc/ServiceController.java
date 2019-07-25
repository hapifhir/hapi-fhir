package ca.uhn.fhir.jpa.demo.svc;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceController {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServiceController.class);

	public ServiceController() {
		ourLog.info("Starting service controller");
	}

	@RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST} )
	public @ResponseBody LoginResponse handleFormUpload(@RequestParam("name") String theUsername) {
		ourLog.info("Login request for user[{}]", theUsername);
		
		LoginResponse retVal = new LoginResponse();
		retVal.setSuccess(true);
		
		return retVal;
	}

}
