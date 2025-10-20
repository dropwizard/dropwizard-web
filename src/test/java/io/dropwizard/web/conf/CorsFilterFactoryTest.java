package io.dropwizard.web.conf;

import com.google.common.collect.ImmutableList;
import io.dropwizard.jetty.MutableServletContextHandler;
import io.dropwizard.core.setup.Environment;
import java.util.Set;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.CrossOriginHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CorsFilterFactoryTest {
    @Mock
    Environment env;
    @Mock
    MutableServletContextHandler contextHandler;
    @Captor
    ArgumentCaptor<Handler.Singleton> handlerCaptor;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void configureHandler() {
        // given
        when(env.getApplicationContext()).thenReturn(contextHandler);
        doNothing().when(contextHandler).insertHandler(any(Handler.Singleton.class));
        String urlPattern = "/example/*";
        CorsFilterFactory factory = new CorsFilterFactory();
        factory.setAllowedOrigins(ImmutableList.of("example.com", "foo.com"));

        // when
        factory.build(env, urlPattern);

        // then
        verify(contextHandler).insertHandler(handlerCaptor.capture());
        assertThat(handlerCaptor.getValue(), is(notNullValue()));
        assertThat(handlerCaptor.getValue() instanceof CrossOriginHandler, is(true));
        assertThat(((CrossOriginHandler) handlerCaptor.getValue())
                .getAllowedOriginPatterns()
                , is(Set.of("example.com", "foo.com")));


    }
}
