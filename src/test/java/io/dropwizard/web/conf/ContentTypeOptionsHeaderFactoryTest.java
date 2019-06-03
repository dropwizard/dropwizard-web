package io.dropwizard.web.conf;

import org.junit.Test;

import java.util.Map;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ContentTypeOptionsHeaderFactoryTest {

    @Test
    public void buildHeaders() {
        // given
        ContentTypeOptionsHeaderFactory factory = new ContentTypeOptionsHeaderFactory();
        factory.setEnabled(true);

        // when
        Map<String, String> headers = factory.build();

        // then
        assertThat(headers, hasEntry("X-Content-Type-Options", "nosniff"));
        assertThat(headers.size(), is(1));
    }

    @Test
    public void buildEmptyMapWhenNotEnabled() {
        // given
        ContentTypeOptionsHeaderFactory factory = new ContentTypeOptionsHeaderFactory();

        // when
        Map<String, String> headers = factory.build();

        // then
        assertThat(headers.size(), is(0));
    }
}
