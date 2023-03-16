package io.dropwizard.web.conf;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;

public class CspHeaderFactoryTest {
    @Test
    public void buildHeaders() {
        // given
        String policy = "test csp policy";
        CspHeaderFactory factory = new CspHeaderFactory();
        factory.setEnabled(true);
        factory.setPolicy(policy);

        // when
        Map<String, String> headers = factory.build();

        // then
        assertThat(headers, hasEntry("Content-Security-Policy", policy));
        assertThat(headers.size(), is(1));
    }

    @Test
    public void buildHeadersCorrectlyIncludingReportOnly() {
        // given
        String policy = "test csp policy";
        String reportOnlyPolicy = "report only test csp policy";
        CspHeaderFactory factory = new CspHeaderFactory();
        factory.setEnabled(true);
        factory.setPolicy(policy);
        factory.setReportOnlyPolicy(reportOnlyPolicy);

        // when
        Map<String, String> headers = factory.build();

        // then
        assertThat(headers, hasEntry("Content-Security-Policy", policy));
        assertThat(headers, hasEntry("Content-Security-Policy-Report-Only", reportOnlyPolicy));
        assertThat(headers.size(), is(2));
    }

    @Test
    public void buildEmptyMapWhenNotEnabled() {
        // given
        CspHeaderFactory factory = new CspHeaderFactory();

        // when
        Map<String, String> headers = factory.build();

        // then
        assertThat(headers.size(), is(0));
    }
}
