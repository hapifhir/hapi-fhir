package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.provider.r4.BaseResourceProviderR4Test;
import ca.uhn.fhir.util.TerserUtilHelper;
import org.checkerframework.checker.units.qual.A;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Account;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;

import static org.junit.jupiter.api.Assertions.*;

class ResourceFlatteningInterceptorTest extends BaseResourceProviderR4Test {

	private ResourceFlatteningInterceptor myInterceptor;

	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private FhirContext myFhirContext;

	@BeforeEach
	public void init() {
		myInterceptor = new ResourceFlatteningInterceptor(myFhirContext, myDaoRegistry);
	}

	public <T extends IBaseResource> IFhirResourceDao<T> getDao(IBaseResource theResource) {
		String type = myFhirContext.getResourceType(theResource);
		return myDaoRegistry.getResourceDao(type);
	}

	@Test
	public void testFlatteningOfLocation() {
		// Location.address becomes Organization.address
		Organization newOrganization = newPersistentOrganization();

		Location location = new Location();
		Address newAddress = new Address();
		newAddress.addLine("NEW LINE 1").setCity("NEW CITY");
		location.setAddress(newAddress);
		location.setManagingOrganization(new Reference(newOrganization));

		myInterceptor.resourceCreatedPreCommit(null, location);

		Organization savedOrganization = (Organization) getDao(newOrganization)
			.read(newOrganization.getIdElement());
		assertEquals(1, savedOrganization.getAddress().size(), "Expect old addresses cleared up");

		Address savedAddress = savedOrganization.getAddressFirstRep();
		assertTrue(newAddress.equalsDeep(savedAddress), "Expect organization address replaced");
	}

	@Nonnull
	private Organization newPersistentOrganization() {
		Organization newOrganization = newOrganization();
		return (Organization) getDao(newOrganization).create(newOrganization).getResource();
	}

	@Nonnull
	private Organization newOrganization() {
		Organization retVal = new Organization();
		retVal.addAddress()
			.addLine("Address 1 Line 1")
			.setCity("Address 1 City");
		retVal.addAddress()
			.addLine("Address 2 Line 1")
			.setCity("Address 2 City");
		return retVal;
	}

	@Test
	public void testFlatteningOfLocationWithEmptyManagingOrg() {
		try {
			myInterceptor.resourceCreatedPreCommit(null, new Location());
		} catch (Exception e) {
			fail("Expected no errors");
		}
	}

	@Test
	public void testFlatteningOfAccount() {
		// Account.status become Organization.Extension.valueCode
		Organization organization = newPersistentOrganization();
		assertTrue(organization.getExtension().isEmpty());

		Account account = new Account();
		account.setName("Test Account");
		account.setStatus(Account.AccountStatus.ACTIVE);
		account.setDescription("Test Active Account");
		account.setOwner(new Reference(organization));

		myInterceptor.resourceCreatedPreCommit(null, account);

		Organization savedOrganization = (Organization) getDao(organization).read(organization.getIdElement());
		assertFalse(organization.getExtension().isEmpty());

		assertEquals(Account.AccountStatus.ACTIVE.toCode(), organization.getExtension().get(0).getValue().primitiveValue());
	}

	@Test
	public void testFlatteningOfAnEmptyAccout() {
		myInterceptor.resourceCreatedPreCommit(null, new Account());
	}

	public void testFlatteningOfPractitionerRole() {
		// PractitionerRole.specialty becomes Practitioner.Extension.valueCode
		PractitionerRole role = new PractitionerRole();
		CodeableConcept specialty = role.addSpecialty();
	}

}
