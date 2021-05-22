package org.eldron.shorty.integration.step;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.eldron.shorty.entity.UrlEntity;
import org.eldron.shorty.repository.UrlRepository;
import org.eldron.shorty.vo.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@CucumberContextConfiguration
public class UrlStepDefinition {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UrlRepository urlRepository;

    private WireMockServer wireMockServer;

    private MvcResult mvcResult;
    private String url;
    final ObjectMapper mapper = new ObjectMapper();

    private final String customUrl = "customurl";

    @Before
    public void setUp() {
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
        urlRepository.deleteAll();
    }

    @After
    public void after() {
        wireMockServer.stop();
    }

    @Given("a valid url")
    public void aValidUrl() {
        configureMockEndpoint(HttpStatus.OK);
    }

    @When("a custom url shorten is requested")
    public void aCustomUrlShortenIsRequested() throws Exception {
        shortenCustomUrl();
    }

    @And("custom url already exists in database")
    public void customUrlAlreadyExistsInDatabase() {
        urlRepository.save(new UrlEntity(this.customUrl, this.url));
    }

    @And("custom url is unique")
    public void customUrlIsUnique() {
        assertThat(urlRepository.findAll())
                .hasSize(1);
    }

    @Given("an invalid url")
    public void anInvalidUrl() {
        this.url = "invalid-url";
    }

    @Given("an unreachable url")
    public void anUnreachableUrl() {
        configureMockEndpoint(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Given("a url without protocol")
    public void aUrlWithoutProtocol() {
        this.url = "localhost";
    }

    @And("the url is registered")
    public void theUrlIsRegistered() throws Exception {
        shortenUrl();
    }

    @When("a url shorten is requested")
    public void aUrlShortenIsRequested() throws Exception {
        shortenUrl();
    }

    @When("a url is requested")
    public void aUrlIsRequested() throws Exception {
        this.mvcResult = findShortenedUrl("anyshorturl");
    }

    @When("a url redirect is requested")
    public void aUrlRedirectIsRequested() throws Exception {
        final String shortUrl;
        if (nonNull(mvcResult)) {
            final var response = mapper.readValue(mvcResult.getResponse().getContentAsString(), Url.class);
            shortUrl = response.getShortenedUrl();
        } else {
            shortUrl = "not-registered.com";
        }

        this.mvcResult = mockMvc.perform(get("/" + shortUrl))
                .andReturn();
    }

    @Then("the response was {word}")
    public void theResponseWas(final String status) {
        final var responseStatus = mvcResult.getResponse().getStatus();
        assertThat(responseStatus).isEqualTo(HttpStatus.valueOf(status).value());
    }

    @And("the url was shortened")
    public void theUrlWasShortened() throws Exception {
        final var response = mapper.readValue(mvcResult.getResponse().getContentAsString(), org.eldron.shorty.vo.Url.class);
        final var result = findShortenedUrl(response.getShortenedUrl());
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @And("the url was not shortened")
    public void theUrlWasNotShortened() {
        assertThat(urlRepository.findAll()).isEmpty();
    }

    @And("the response header contains the original url")
    public void theResponseHeaderContainsTheOriginalUrl() {
        final var location = mvcResult.getResponse().getHeader(HttpHeaders.LOCATION);
        assertThat(location).isEqualTo(url);
    }

    private void shortenUrl() throws Exception {
        final var request = "{\"url\": \"" + url + "\"}";

        this.mvcResult = mockMvc.perform(post("/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    private void shortenCustomUrl() throws Exception {

        final var customUrlRequest = String.format("{ \"url\": \"%s\", \"custom\": \"%s\" }", this.url, this.customUrl);
        this.mvcResult = mockMvc.perform(post("/shorten/custom")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customUrlRequest))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    private MvcResult findShortenedUrl(final String shortUrl) throws Exception {
        return mockMvc.perform(get("/shorten/" + shortUrl))
                .andReturn();
    }

    private void configureMockEndpoint(final HttpStatus status) {
        this.url = "http://localhost:" + wireMockServer.port();
        configureFor("localhost", wireMockServer.port());
        stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(urlEqualTo("/"))
                .willReturn(aResponse()
                        .withStatus(status.value())));
    }

}
