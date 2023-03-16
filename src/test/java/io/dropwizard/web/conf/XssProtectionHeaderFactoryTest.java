package io.dropwizard.web.conf;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;

public class XssProtectionHeaderFactoryTest {
    @Test
    public void buildHeaders() {
        // given
        XssProtectionHeaderFactory factory = new XssProtectionHeaderFactory();
        factory.setEnabled(true);

        // when
        Map<String, String> headers = factory.build();

        // then
        assertThat(headers, hasEntry("X-XSS-Protection", "1; mode=block"));
        assertThat(headers.size(), is(1));
    }

    @Test
    public void buildHeadersToDisable() {
        // given
        XssProtectionHeaderFactory factory = new XssProtectionHeaderFactory();
        factory.setEnabled(true);
        factory.setOn(false);

        // when
        Map<String, String> headers = factory.build();

        // then
        assertThat(headers, hasEntry("X-XSS-Protection", "0"));
        assertThat(headers.size(), is(1));
    }

    @Test
    public void buildHeadersToNotBlock() {
        // given
        XssProtectionHeaderFactory factory = new XssProtectionHeaderFactory();
        factory.setEnabled(true);
        factory.setBlock(false);

        // when
        Map<String, String> headers = factory.build();

        // then
        assertThat(headers, hasEntry("X-XSS-Protection", "1"));
        assertThat(headers.size(), is(1));
    }

    @Test
    public void buildEmptyMapWhenNotEnabled() {
        // given
        XssProtectionHeaderFactory factory = new XssProtectionHeaderFactory();

        // when
        Map<String, String> headers = factory.build();

        // then
        assertThat(headers.size(), is(0));
    }
}
