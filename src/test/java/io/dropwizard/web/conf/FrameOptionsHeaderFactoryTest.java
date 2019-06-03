package io.dropwizard.web.conf;

import org.junit.Test;

import java.util.Map;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class FrameOptionsHeaderFactoryTest {
    @Test
    public void buildHeaders() {
        // given
        FrameOptionsHeaderFactory factory = new FrameOptionsHeaderFactory();
        factory.setEnabled(true);

        // when
        Map<String, String> headers = factory.build();

        // then
        assertThat(headers, hasEntry("X-Frame-Options", "DENY"));
        assertThat(headers.size(), is(1));
    }

    @Test
    public void buildHeadersWithAllowFrom() {
        // given
        FrameOptionsHeaderFactory factory = new FrameOptionsHeaderFactory();
        factory.setEnabled(true);
        factory.setOption(FrameOptionsHeaderFactory.FrameOption.ALLOW_FROM);
        factory.setOrigin("https://example.com/");

        // when
        Map<String, String> headers = factory.build();

        // then
        assertThat(headers, hasEntry("X-Frame-Options", "ALLOW-FROM https://example.com/"));
        assertThat(headers.size(), is(1));
    }

    @Test
    public void buildEmptyMapWhenNotEnabled() {
        // given
        FrameOptionsHeaderFactory factory = new FrameOptionsHeaderFactory();

        // when
        Map<String, String> headers = factory.build();

        // then
        assertThat(headers.size(), is(0));
    }
}
