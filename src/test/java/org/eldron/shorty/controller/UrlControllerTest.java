package org.eldron.shorty.controller;

import org.eldron.shorty.config.EmbeddedRedisConfiguration;
import org.eldron.shorty.hash.UrlHash;
import org.eldron.shorty.repository.UrlRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EmbeddedRedisConfiguration.class)
@AutoConfigureMockMvc
public class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UrlRepository urlRepository;

    @Test
    public void whenRequestUrlThatDoesntExist_thenReturnNotFound() throws Exception {
        final var shortenedUrl = "abcd";

        mockMvc.perform(get("/shorten/" + shortenedUrl))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void whenRequestUrlExists_thenReturnOriginalUrl() throws Exception {
        // Given
        final var shortenedUrl = "abcd";
        final var originalUrl = "www.google.com";
        final var url = UrlHash.builder()
                .id(shortenedUrl)
                .originalUrl(originalUrl)
                .build();
        urlRepository.save(url);

        // When / Then
        final MvcResult mvcResult = mockMvc.perform(get("/shorten/" + shortenedUrl))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).contains(originalUrl);
    }

    @Test
    public void whenRequestToShortenUrl() throws Exception {
        final var url = "www.google.com";
        final var request = "{\"url\": \"" + url + "\"}";

        final MvcResult mvcResult = mockMvc.perform(post("/shorten")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).contains(url);
    }
}
