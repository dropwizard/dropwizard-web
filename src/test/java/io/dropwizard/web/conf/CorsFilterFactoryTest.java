package io.dropwizard.web.conf;

import com.google.common.collect.ImmutableList;
import io.dropwizard.jetty.setup.ServletEnvironment;
import io.dropwizard.core.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CorsFilterFactoryTest {
    @Mock
    Environment env;
    @Mock
    ServletEnvironment servlets;
    @Mock
    FilterRegistration.Dynamic registration;
    @Captor
    ArgumentCaptor<String> filterNameCaptor;
    @Captor
    ArgumentCaptor<Class<? extends Filter>> filterClassCaptor;
    @Captor
    ArgumentCaptor<String> urlPatternCaptor;
    @Captor
    ArgumentCaptor<Map<String, String>> initParamsCaptor;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void configureFilter() {
        // given
        when(env.servlets()).thenReturn(servlets);
        when(servlets.addFilter(anyString(), any(Class.class))).thenReturn(registration);
        doNothing().when(registration).addMappingForUrlPatterns(any(EnumSet.class), anyBoolean(), anyString());
        when(registration.setInitParameters(anyMap())).thenReturn(Collections.emptySet());
        String urlPattern = "/example/*";
        CorsFilterFactory factory = new CorsFilterFactory();
        factory.setAllowedOrigins(ImmutableList.of("example.com", "foo.com"));

        // when
        factory.build(env, urlPattern);

        // then
        verify(servlets).addFilter(filterNameCaptor.capture(), filterClassCaptor.capture());
        verify(registration).addMappingForUrlPatterns(any(), eq(true), urlPatternCaptor.capture());
        verify(registration).setInitParameters(initParamsCaptor.capture());
        assertThat(filterNameCaptor.getValue(), is("cross-origin-filter"));
        assertThat(filterClassCaptor.getValue(), is(CoreMatchers.<Class<?>>equalTo(CrossOriginFilter.class)));
        assertThat(urlPatternCaptor.getValue(), is(urlPattern));
        assertThat(initParamsCaptor.getValue(), aMapWithSize(1));
        assertThat(initParamsCaptor.getValue(), hasEntry(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "example.com,foo.com"));
    }
}
