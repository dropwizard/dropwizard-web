package io.dropwizard.web.test;

import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
@ExtendWith(DropwizardExtensionsSupport.class)
public class WebFilterIT {
    private static final String CONFIG_DIR = "src/test/resources/conf";
    private static final String CONFIG_PATH = CONFIG_DIR + "/config.yml";

    public static final DropwizardAppExtension<TestConfig> EXT = new DropwizardAppExtension<>(TestApp.class, CONFIG_PATH);

    private Client client;
    private String hostUrl;
    private String adminUrl;

    @BeforeEach
    public void setUp() throws Exception {
        client = EXT.client();
        hostUrl = "http://localhost:" + EXT.getLocalPort();
        adminUrl = "http://localhost:" + EXT.getAdminPort() + "/admin";
    }

    @AfterEach
    public void tearDown() throws Exception {
        client.close();
    }

    @Test
    public void testHeaders() {
        // given
        String uri1 = hostUrl + "/test/one";
        String uri2 = hostUrl + "/test/two";
        String adminUri = adminUrl + "/ping";

        // when
        Response response1 = client.target(uri1).request().get();
        Response response2 = client.target(uri2).request().get();
        Response adminResponse = client.target(adminUri).request().get();

        // then
        assertThat(response1.readEntity(String.class), is("test response 1"));
        assertThat(response1.getHeaderString("Strict-Transport-Security"), is("max-age=31536000; includeSubDomains"));
        assertThat(response1.getHeaderString("X-Frame-Options"), is("DENY"));
        assertThat(response1.getHeaderString("X-Content-Type-Options"), is("nosniff"));
        assertThat(response1.getHeaderString("X-XSS-Protection"), is("1; mode=block"));
        assertThat(response1.getHeaderString("Content-Security-Policy"), is("test csp policy"));
        assertThat(response1.getHeaderString("X-Custom-Header-1"), is("custom value 1"));
        assertThat(response1.getHeaderString("X-Custom-Header-2"), is("custom value 2"));
        assertThat(response2.readEntity(String.class), is("test response 2"));
        assertThat(response2.getHeaderString("X-Second-Custom-Header"), is("custom value"));
        assertThat(adminResponse.getHeaderString("X-Admin-Custom-Header"), is("custom value"));
    }

}
