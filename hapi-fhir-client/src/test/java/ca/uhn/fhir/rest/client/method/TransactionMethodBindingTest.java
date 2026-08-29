package ca.uhn.fhir.rest.client.method;

/*
 * #%L
 * HAPI FHIR - Client Framework
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

import static ca.uhn.fhir.context.FhirVersionEnum.R4;
import static ca.uhn.fhir.rest.api.EncodingEnum.JSON;
import static ca.uhn.fhir.rest.api.RequestTypeEnum.POST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IFhirVersion;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IRestfulClientFactory;
import ca.uhn.fhir.rest.client.impl.BaseHttpClientInvocation;
import java.util.List;
import java.util.Map;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransactionMethodBindingTest {

  private static final String URL_BASE_CONSTANT = "http://localhost:8080";
  private static final String RAW_BUNDLE_CONSTANT = "{}";

  @Mock
  FhirContext myContext;

  @Mock
  IRestfulClientFactory myRestfulClientFactory;

  @Mock
  IBaseBundle myBundle;

  @Mock
  IParser myParser;

  @Mock
  IFhirVersion myVersion;

  @Mock
  IHttpClient myHttpClient;

  @Mock
  IHttpRequest myHttpRequest;

  @Test
  void createTransactionInvocation_bundle() {
    when(myContext.getRestfulClientFactory()).thenReturn(myRestfulClientFactory);
    when(myContext.newJsonParser()).thenReturn(myParser);
    when(myContext.getVersion()).thenReturn(myVersion);
    when(myVersion.getVersion()).thenReturn(R4);
    when(myRestfulClientFactory.getHttpClient(argThat(sb -> sb.toString().equals(URL_BASE_CONSTANT)),
        isNull(), isNull(), eq(POST), eq(List.of()))).thenReturn(myHttpClient);
    when(myHttpClient.createByteRequest(myContext, null, "application/fhir+json", JSON)).thenReturn(
        myHttpRequest);

    BaseHttpClientInvocation invocation = TransactionMethodBinding.createTransactionInvocation(
        myBundle, myContext);

    IHttpRequest request = invocation.asHttpRequest(URL_BASE_CONSTANT, Map.of(), JSON, false);

    assertSame(myHttpRequest, request);
  }

  @Test
  void createTransactionInvocation_rawBundle() {
    when(myContext.getRestfulClientFactory()).thenReturn(myRestfulClientFactory);
    when(myContext.newJsonParser()).thenReturn(myParser);
    when(myContext.getVersion()).thenReturn(myVersion);
    when(myVersion.getVersion()).thenReturn(R4);
    when(myRestfulClientFactory.getHttpClient(argThat(sb -> sb.toString().equals(URL_BASE_CONSTANT)),
        isNull(), isNull(), eq(POST), eq(List.of()))).thenReturn(myHttpClient);
    when(myHttpClient.createByteRequest(myContext, "{}", "application/fhir+json", JSON)).thenReturn(
        myHttpRequest);

    BaseHttpClientInvocation invocation = TransactionMethodBinding.createTransactionInvocation(
        RAW_BUNDLE_CONSTANT, myContext);

    IHttpRequest request = invocation.asHttpRequest(URL_BASE_CONSTANT, Map.of(), JSON, false);

    assertSame(myHttpRequest, request);
  }
}
