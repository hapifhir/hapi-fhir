package ca.uhn.fhir.jpa.ips.generator;

import ca.uhn.fhir.jpa.ips.api.IIpsGenerationStrategy;
import ca.uhn.fhir.jpa.ips.api.IpsContext;
import ca.uhn.fhir.jpa.ips.api.SectionRegistry;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Organization;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DefaultIpsGenerationStrategy implements IIpsGenerationStrategy {

	@Override
	public SectionRegistry getSectionRegistry() {
		return new SectionRegistry();
	}

	@Override
	public IBaseResource createAuthor() {
		Organization organization = new Organization();
		organization.setName("eHealthLab - University of Cyprus")
			.addAddress(new Address()
				.addLine("1 University Avenue")
				.setCity("Nicosia")
				.setPostalCode("2109")
				.setCountry("CY"))
			.setId(IdType.newRandomUuid());
		return organization;
	}

	@Override
	public String createTitle(IpsContext theContext) {
		return "Patient Summary as of " + DateTimeFormatter.ofPattern("MM/dd/yyyy").format(LocalDate.now());
	}

	@Override
	public String createConfidentiality(IpsContext theIpsContext) {
		return Composition.DocumentConfidentiality.N.toCode();
	}

	@Override
	public IIdType massageResourceId(@Nullable IpsContext theIpsContext, @Nonnull IBaseResource theResource) {
		return IdType.newRandomUuid();
	}

	@Override
	public void massageResourceSearch(IpsContext.IpsSectionContext theIpsSectionContext, SearchParameterMap theSearchParameterMap) {

	}

	@Override
	public boolean shouldInclude(IpsContext.IpsSectionContext theIpsSectionContext, IBaseResource theCandidate) {
		return true;
	}

}
