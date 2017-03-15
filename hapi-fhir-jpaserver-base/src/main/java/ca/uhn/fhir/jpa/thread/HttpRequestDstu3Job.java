package ca.uhn.fhir.jpa.thread;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hl7.fhir.dstu3.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpRequestDstu3Job implements Runnable {

    private HttpUriRequest request;
    private Subscription subscription;

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestDstu3Job.class);

    public HttpRequestDstu3Job(HttpUriRequest request, Subscription subscription) {
        this.request = request;
        this.subscription = subscription;
    }

    @Override
    public void run() {
        executeRequest(request, subscription);
    }

    /**
     * Sends a post back to the subscription client
     *
     * @param request
     * @param subscription
     */
    private void executeRequest(HttpUriRequest request, Subscription subscription) {
        String url = subscription.getChannel().getEndpoint();

        try {
            HttpClient client = HttpClientBuilder.create().build();
            client.execute(request);
        } catch (IOException e) {
            logger.error("Error sending rest post call from subscription " + subscription.getId() + " with endpoint " + url);
            e.printStackTrace();
        }

        logger.info("sent: " + url);
    }
}
