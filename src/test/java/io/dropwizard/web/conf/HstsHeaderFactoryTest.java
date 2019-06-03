package io.dropwizard.web.conf;

import io.dropwizard.util.Duration;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HstsHeaderFactoryTest {
    @Test
    public void buildHeaders() {
        // given
        HstsHeaderFactory factory = new HstsHeaderFactory();
        factory.setEnabled(true);

        // when
        Map<String, String> headers = factory.build();

        // then
        assertThat(headers, hasEntry("Strict-Transport-Security", "max-age=31536000; includeSubDomains"));
        assertThat(headers.size(), is(1));
    }

    @Test
    public void buildHeadersToNotIncludeSubDomains() {
        // given
        HstsHeaderFactory factory = new HstsHeaderFactory();
        factory.setEnabled(true);
        factory.setMaxAge(Duration.seconds(30));
        factory.setIncludeSubDomains(false);

        // when
        Map<String, String> headers = factory.build();

        // then
        assertThat(headers, hasEntry("Strict-Transport-Security", "max-age=30"));
        assertThat(headers.size(), is(1));
    }

    @Test
    public void buildHeadersToPreload() {
        // given
        HstsHeaderFactory factory = new HstsHeaderFactory();
        factory.setEnabled(true);
        factory.setMaxAge(Duration.minutes(1));
        factory.setPreload(true);

        // when
        Map<String, String> headers = factory.build();

        // then
        assertThat(headers, hasEntry("Strict-Transport-Security", "max-age=60; includeSubDomains; preload"));
        assertThat(headers.size(), is(1));
    }

    @Test
    public void buildEmptyMapWhenNotEnabled() {
        // given
        HstsHeaderFactory factory = new HstsHeaderFactory();

        // when
        Map<String, String> headers = factory.build();

        // then
        assertThat(headers.size(), is(0));
    }
}
