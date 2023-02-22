# dropwizard-web
[![Build Status](https://github.com/dropwizard/dropwizard-web/actions/workflows/build.yml/badge.svg?branch=2.0.x)](https://github.com/dropwizard/dropwizard-web/actions?query=branch%3A2.0.x)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=dropwizard_dropwizard-web&metric=coverage)](https://sonarcloud.io/summary/new_code?id=dropwizard_dropwizard-web)
[![Maven Central](https://img.shields.io/maven-central/v/io.dropwizard.modules/dropwizard-web.svg)](http://mvnrepository.com/artifact/io.dropwizard.modules/dropwizard-web)

Provides support for configuring various http headers that are important for web services.

## Supports
- HTTP Strict Transport Security (HSTS)
- X-Frame-Options
- X-Content-Type-Options
- X-XSS-Protection (XSS)
- Content Security Policy (CSP)
- Cross-Origin Resource Sharing (CORS)
- other custom headers

## Dropwizard Version Support Matrix
dropwizard-web | Dropwizard v1.3.x  | Dropwizard v2.0.x  | Dropwizard v2.1.x
-------------- | ------------------ | ------------------ | ------------------
v1.3.x         | :white_check_mark: | :white_check_mark: | :question:
v1.4.x         | :white_check_mark: | :white_check_mark: | :question:
v1.5.x         | :white_check_mark: | :white_check_mark: | :white_check_mark:

## Usage
In your application's `Configuration` class, add a `WebConfiguration` object:
```java
public class ExampleConfiguration extends Configuration {
    ...

    @Valid
    @NotNull
    @JsonProperty("web")
    private WebConfiguration webConfiguration = new WebConfiguration();

    public WebConfiguration getWebConfiguration() {
        return webConfiguration;
    }

    public void setWebConfiguration(final WebConfiguration webConfiguration) {
        this.webConfiguration = webConfiguration;
    }
}
```

Add a `WebBundle` to the `Boostrap` object in your `initialize` method:
```java
bootstrap.addBundle(new WebBundle<ExampleConfiguration>() {
    @Override
    public WebConfiguration getWebConfiguration(final ExampleConfiguration configuration) {
        return configuration.getWebConfiguration();
    }
});
```

## Basic Configuration
Define the following configuration in your `config.yml` file:
```yaml
web:
  uriPath: /api
  hsts:
    enabled: true
  frame-options:
    enabled: true
  content-type-options:
    enabled: true
  xss-protection:
    enabled: true
```
`uriPath` should indicate the path where APIs are served from.

This minimal config results in the following:
- HSTS configured for 1 year, including sub domains
- Frames disabled
- Content-Type sniffing disabled
- XSS filtering on in `block` mode

Support for CORS or CSP require additional configuration.

## Maven Artifacts
This project is available on Maven Central. To add it to your project simply add the following dependencies to your 
`pom.xml`:
```xml
<dependency>
  <groupId>io.dropwizard.modules</groupId>
  <artifactId>dropwizard-web</artifactId>
  <version>${dropwizard-web.version}</version>
</dependency>
```

## Configuration Reference
### Web Configuration
Name | Default | Description
---- | ------- | -----------
hsts | (1 year, including sub domains) | Configure Strict-Transport-Security.
frame-options | (disable frames) | Configure X-Frame-Options.
content-type-options | (disable content-type sniffing) | Configure X-Content-Type-Options.
xss-protection | (on in block mode) | Configure X-XSS-Protection.
csp | (none) | Configure Content Security Policy.
cors | (none) | Configure Cross-Origin Resource Sharing.
headers | (none) | Configure custom headers.

### HTTP Strict Transport Security (HSTS)
https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Strict-Transport-Security

```yaml
web:
  hsts:
    enabled: true
    maxAge: 365 days
    includeSubDomains: true
```

Name | Default | Description
---- | ------- | -----------
maxAge | 365 days | The time that the browser should remember that a site is only to be accessed using HTTPS.
includeSubDomains | true | If `true`, this rule applies to all of the site's subdomains as well.
preload | false | See [Preloading Strict Transport Security](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Strict-Transport-Security#Preloading_Strict_Transport_Security) for details.
enabled | false | If false, does not apply header(s).

### X-Frame-Options
https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Frame-Options

```yaml
web:
  frame-options:
    enabled: true
    option: SAMEORIGIN
```

Name | Default | Description
---- | ------- | -----------
option | DENY | Must be one of: DENY, SAMEORIGIN, ALLOW-FROM
origin | (none) | If option is ALLOW-FROM, identifies the origin that will be allowed to display this page in a frame.
enabled | false | If false, does not apply header(s).

### X-Content-Type-Options
https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Content-Type-Options

```yaml
web:
  content-type-options:
    enabled: true
```

Name | Default | Description
---- | ------- | -----------
enabled | false | If false, does not apply header(s).

### X-XSS-Protection (XSS)
https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-XSS-Protection

```yaml
web:
  xss-protection:
    enabled: true
    on: true
    block: true
```

Name | Default | Description
---- | ------- | -----------
on | true | If true, enables XSS filtering.
block | true | If true, when browser detects an attack, the page will not be rendered. If false, the browser will sanitize the page to remove the unsafe parts.
enabled | false | If false, does not apply header(s).


### Content Security Policy (CSP)
- https://developer.mozilla.org/en-US/docs/Web/HTTP/CSP
- https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Security-Policy
- https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Security-Policy-Report-Only

```yaml
web:
  csp:
    enabled: true
    policy: "default-src 'self'"
```

Name | Default | Description
---- | ------- | -----------
policy | (none) | Policy directives that control resources the browser is allowed to load for a page.
reportOnlyPolicy | (none) | Same as 'policy' but only reports violations instead of preventing them.
enabled | false | If false, does not apply header(s).


### Cross-Origin Resource Sharing (CORS)
- https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS
- https://www.eclipse.org/jetty/documentation/9.4.x/cross-origin-filter.html

```yaml
web:
  cors:
    allowedOrigins: ["example.com"]
    allowedMethods: ["DELETE","GET","HEAD","POST","PUT"]
    allowedHeaders: ["Accept","Authorization","Content-Type","Origin","X-Requested-With"]
    preflightMaxAge: 30 minutes
```

Name | Default | Description
---- | ------- | -----------
allowedOrigins | (all origins) | A list of origins that are allowed to access the resources.
allowedTimingOrigins | (no origins) | A list of origins that are allowed to time the resources.
allowedMethods | ["GET","POST","HEAD"] | A list of HTTP methods that are allowed to be used when accessing the resources.
preflightMaxAge | 30 minutes | The duration that preflight requests can be cached by the client.
allowCredentials | true | A boolean indicating if the resource allows requests with credentials.
exposedHeaders | (empty list) | A list of HTTP headers that are allowed to be exposed on the client.
chainPreflight | true | If true, preflight requests are chained to their target resource for normal handling (as an OPTION request). Else, the filter will respond to the preflight.

### Other Headers
```yaml
web:
  headers:
    X-Custom-Header-1: custom value 1
    X-Custom-Header-2: custom value 2
```

Name | Default | Description
---- | ------- | -----------
headers | (none) | Map of headers (name and value) to include in the response.

## Support
Please file bug reports and feature requests in [GitHub issues](https://github.com/dropwizard/dropwizard-web/issues).
