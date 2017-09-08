package example;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.SearchParameter;

public class GenomicsUploader {

	public static void main(String[] theArgs) {
		FhirContext ctx = FhirContext.forR4();
		IGenericClient client = ctx.newRestfulGenericClient("http://fhirtest.uhn.ca/baseR4");
		client.registerInterceptor(new LoggingInterceptor(false));

		SearchParameter dnaSequenceVariantName = new SearchParameter();
		dnaSequenceVariantName.setId("SearchParameter/dnaSequenceVariantName");
		dnaSequenceVariantName.setStatus(Enumerations.PublicationStatus.ACTIVE);
		dnaSequenceVariantName.addBase("Observation");
		dnaSequenceVariantName.setCode("dnaSequenceVariantName");
		dnaSequenceVariantName.setType(Enumerations.SearchParamType.TOKEN);
		dnaSequenceVariantName.setTitle("DNASequenceVariantName");
		dnaSequenceVariantName.setExpression("Observation.extension('http://hl7.org/fhir/StructureDefinition/observation-geneticsDNASequenceVariantName')");
		dnaSequenceVariantName.setXpathUsage(SearchParameter.XPathUsageType.NORMAL);
		client.update().resource(dnaSequenceVariantName).execute();

		SearchParameter dNAVariantId = new SearchParameter();
		dNAVariantId.setId("SearchParameter/dNAVariantId");
		dNAVariantId.setStatus(Enumerations.PublicationStatus.ACTIVE);
		dNAVariantId.addBase("Observation");
		dNAVariantId.setCode("dnaVariantId");
		dNAVariantId.setType(Enumerations.SearchParamType.TOKEN);
		dNAVariantId.setTitle("DNAVariantId");
		dNAVariantId.setExpression("Observation.extension('http://hl7.org/fhir/StructureDefinition/observation-geneticsDNAVariantId')");
		dNAVariantId.setXpathUsage(SearchParameter.XPathUsageType.NORMAL);
		client.update().resource(dNAVariantId).execute();

		SearchParameter gene = new SearchParameter();
		gene.setId("SearchParameter/gene");
		gene.setStatus(Enumerations.PublicationStatus.ACTIVE);
		gene.addBase("Observation");
		gene.setCode("gene");
		gene.setType(Enumerations.SearchParamType.TOKEN);
		gene.setTitle("Gene");
		gene.setExpression("Observation.extension('http://hl7.org/fhir/StructureDefinition/observation-geneticsGene')");
		gene.setXpathUsage(SearchParameter.XPathUsageType.NORMAL);
		client.update().resource(gene).execute();

		SearchParameter alleleName = new SearchParameter();
		alleleName.setId("SearchParameter/alleleName");
		alleleName.setStatus(Enumerations.PublicationStatus.ACTIVE);
		alleleName.addBase("Observation");
		alleleName.setCode("alleleName");
		alleleName.setType(Enumerations.SearchParamType.TOKEN);
		alleleName.setTitle("AlleleName");
		alleleName.setExpression("Observation.extension('http://hl7.org/fhir/StructureDefinition/observation-geneticsAlleleName')");
		alleleName.setXpathUsage(SearchParameter.XPathUsageType.NORMAL);
		client.update().resource(alleleName).execute();
	}

}
