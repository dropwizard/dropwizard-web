package io.dropwizard.web.test;

import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class WebFilterIT {
    private static final String CONFIG_DIR = "src/test/resources/conf";
    private static final String CONFIG_PATH = CONFIG_DIR + "/config.yml";
    private static final String HOST = "localhost";

    @ClassRule
    public static final DropwizardAppRule<TestConfig> APP = new DropwizardAppRule<>(TestApp.class, CONFIG_PATH);

    private Client client;
    private String hostUrl;

    @Before
    public void setUp() throws Exception {
        client = ClientBuilder.newClient();
        hostUrl = "http://" + HOST + ":" + APP.getLocalPort();
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }

    @Test
    public void testHeaders() {
        // given
        String url = hostUrl + "/test";

        // when
        Response response = client.target(url).request().get();

        // then
        assertThat(response.readEntity(String.class), is("test response"));
        assertThat(response.getHeaderString("Strict-Transport-Security"), is("max-age=31536000; includeSubDomains"));
        assertThat(response.getHeaderString("X-Frame-Options"), is("DENY"));
        assertThat(response.getHeaderString("X-Content-Type-Options"), is("nosniff"));
        assertThat(response.getHeaderString("X-XSS-Protection"), is("1; mode=block"));
        assertThat(response.getHeaderString("Content-Security-Policy"), is("test csp policy"));
        assertThat(response.getHeaderString("X-Custom-Header-1"), is("custom value 1"));
        assertThat(response.getHeaderString("X-Custom-Header-2"), is("custom value 2"));
    }
}
