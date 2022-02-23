package ca.uhn.fhir.cql.dstu3.evaluation;

/*-
 * #%L
 * HAPI FHIR JPA Server - Clinical Quality Language
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.cql.common.helper.DateHelper;
import ca.uhn.fhir.cql.common.helper.UsingHelper;
import ca.uhn.fhir.cql.common.provider.EvaluationProviderFactory;
import ca.uhn.fhir.cql.common.provider.LibraryResolutionProvider;
import ca.uhn.fhir.cql.dstu3.helper.LibraryHelper;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.apache.commons.lang3.tuple.Triple;
import org.cqframework.cql.elm.execution.Library;
import org.hl7.fhir.dstu3.model.Measure;
import org.opencds.cqf.cql.engine.data.DataProvider;
import org.opencds.cqf.cql.engine.debug.DebugMap;
import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.execution.LibraryLoader;
import org.opencds.cqf.cql.engine.runtime.DateTime;
import org.opencds.cqf.cql.engine.runtime.Interval;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;

import java.util.Date;
import java.util.List;

public class MeasureEvaluationSeed {
	private Measure measure;
	private Context context;
	private Interval measurementPeriod;
	private final LibraryLoader libraryLoader;
	private final LibraryResolutionProvider<org.hl7.fhir.dstu3.model.Library> libraryResourceProvider;
	private final EvaluationProviderFactory providerFactory;
	private DataProvider dataProvider;
	private final LibraryHelper libraryHelper;

	public MeasureEvaluationSeed(EvaluationProviderFactory providerFactory, LibraryLoader libraryLoader,
										  LibraryResolutionProvider<org.hl7.fhir.dstu3.model.Library> libraryResourceProvider, LibraryHelper libraryHelper) {
		this.providerFactory = providerFactory;
		this.libraryLoader = libraryLoader;
		this.libraryResourceProvider = libraryResourceProvider;
		this.libraryHelper = libraryHelper;
	}

	public Measure getMeasure() {
		return this.measure;
	}

	public Context getContext() {
		return this.context;
	}

	public Interval getMeasurementPeriod() {
		return this.measurementPeriod;
	}

	public DataProvider getDataProvider() {
		return this.dataProvider;
	}

	public void setup(Measure measure, String periodStart, String periodEnd, String productLine, String source,
							String user, String pass, RequestDetails theRequestDetails) {
		this.measure = measure;

		this.libraryHelper.loadLibraries(measure, this.libraryLoader, this.libraryResourceProvider, theRequestDetails);

		// resolve primary library
		Library library = this.libraryHelper.resolvePrimaryLibrary(measure, libraryLoader, this.libraryResourceProvider, theRequestDetails);

		// resolve execution context
		context = new Context(library);
		context.registerLibraryLoader(libraryLoader);

		List<Triple<String, String, String>> usingDefs = UsingHelper.getUsingUrlAndVersion(library.getUsings());

		if (usingDefs.size() > 1) {
			throw new IllegalArgumentException(Msg.code(1647) + "Evaluation of Measure using multiple Models is not supported at this time.");
		}

		// If there are no Usings, there is probably not any place the Terminology
		// actually used so I think the assumption that at least one provider exists is
		// ok.
		TerminologyProvider terminologyProvider = null;
		if (usingDefs.size() > 0) {
			// Creates a terminology provider based on the first using statement. This
			// assumes the terminology
			// server matches the FHIR version of the CQL.
			terminologyProvider = this.providerFactory.createTerminologyProvider(usingDefs.get(0).getLeft(),
					usingDefs.get(0).getMiddle(), source, user, pass);
			context.registerTerminologyProvider(terminologyProvider);
		}

		for (Triple<String, String, String> def : usingDefs) {
			this.dataProvider = this.providerFactory.createDataProvider(def.getLeft(), def.getMiddle(),
				terminologyProvider, theRequestDetails);
			context.registerDataProvider(def.getRight(), dataProvider);
		}

		// resolve the measurement period
		measurementPeriod = new Interval(DateHelper.resolveRequestDate("periodStart", periodStart), true,
			DateHelper.resolveRequestDate("periodEnd", periodEnd), true);

		context.setParameter(null, "Measurement Period",
			new Interval(DateTime.fromJavaDate((Date) measurementPeriod.getStart()), true,
				DateTime.fromJavaDate((Date) measurementPeriod.getEnd()), true));

		if (productLine != null) {
			context.setParameter(null, "Product Line", productLine);
		}

		context.setExpressionCaching(true);

		// This needs to be made configurable
		DebugMap debugMap = new DebugMap();
		debugMap.setIsLoggingEnabled(true);
		context.setDebugMap(debugMap);
	}
}
