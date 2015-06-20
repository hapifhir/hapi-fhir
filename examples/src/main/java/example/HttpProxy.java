package example;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.IGenericClient;

public class HttpProxy {

   public static void main(String[] args) {
      /*
       * This is out ot date - Just keeping
       * it in case it's helpful...
       */
      final String authUser = "username"; 
      final String authPassword = "password"; 
      CredentialsProvider credsProvider = new BasicCredentialsProvider();
      credsProvider.setCredentials(new AuthScope("10.10.10.10", 8080),
            new UsernamePasswordCredentials(authUser, authPassword)); 

      HttpHost myProxy = new HttpHost("10.10.10.10", 8080);
      
      
      HttpClientBuilder clientBuilder = HttpClientBuilder.create(); 
      clientBuilder
         .setProxy(myProxy)
         .setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy())
         .setDefaultCredentialsProvider(credsProvider) 
         .disableCookieManagement(); 
      CloseableHttpClient httpClient = clientBuilder.build();
      
      FhirContext ctx = FhirContext.forDstu2(); 
      String serverBase = "http://spark.furore.com/fhir/"; 
      ctx.getRestfulClientFactory().setHttpClient(httpClient); 
      IGenericClient client = ctx.newRestfulGenericClient(serverBase); 

      IdDt id = new IdDt("Patient", "123"); 
      client.read(Patient.class, id); 
      
   }
   
}
