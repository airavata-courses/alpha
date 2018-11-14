package team.alpha;

import com.google.common.base.Preconditions;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.net.URI;

public class GetRequestThread extends Thread {

    private final CloseableHttpClient httpClient;
    private final HttpContext context;
    private final URI uri;

    public GetRequestThread(CloseableHttpClient httpClient, URI uri) {
        this.httpClient = httpClient;
        this.context = HttpClientContext.create();
        this.uri = uri;
    }

    @Override
    public void run() {
        HttpGet httpGet = new HttpGet(uri);
        httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        try (CloseableHttpResponse response = httpClient.execute(httpGet, context)) {
            Preconditions.checkState(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK, "Get request to '%s' failed with status code '%s'", uri, response.getStatusLine().getStatusCode());
            EntityUtils.consumeQuietly(response.getEntity());
        } catch (Exception ex) {
            throw new RuntimeException("Request to " + uri + " did not complete successfully", ex);
        }
    }

}