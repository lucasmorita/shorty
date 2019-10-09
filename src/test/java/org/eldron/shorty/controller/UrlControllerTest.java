package org.eldron.shorty.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eldron.shorty.config.EmbeddedRedisConfiguration;
import org.eldron.shorty.vo.Url;
import org.eldron.shorty.vo.response.BaseResponse;
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

    @Test
    public void whenRequestUrlThatDoesntExist_thenReturnNotFound() throws Exception {
        final var shortenedUrl = "abcd";

        mockMvc.perform(get("/shorten/" + shortenedUrl))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void whenRequestToShortenUrl_thenReturnSavedUrl() throws Exception {
        final var url = "http://www.google.com";
        final var request = "{\"url\": \"" + url + "\"}";

        final MvcResult mvcResult = mockMvc.perform(post("/shorten")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).contains(url);
    }

    @Test
    public void whenRequestToShortenUrlWithoutProtocol_thenReturnUrl() throws Exception {
        final var url = "www.google.com";
        final var request = "{\"url\": \"" + url + "\"}";

        final MvcResult mvcResult = mockMvc.perform(post("/shorten")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).contains("Invalid url");
    }

    @Test
    public void whenRequestToShortenWithUrlThatDoesntExist_thenReturnUrl() throws Exception {
        final var url = "http://www.completely-invalid-url.com";
        final var request = "{\"url\": \"" + url + "\"}";

        final MvcResult mvcResult = mockMvc.perform(post("/shorten")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).contains("Invalid url");
    }

    @Test
    public void whenRequestUrlExists_thenReturnOriginalUrl() throws Exception {
        final var url = "http://www.google.com";
        final var request = "{\"url\": \"" + url + "\"}";

        final MvcResult mvcPostResult = mockMvc.perform(post("/shorten")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        final ObjectMapper mapper = new ObjectMapper();
        final String response = mvcPostResult.getResponse().getContentAsString();
        final BaseResponse<Url> baseResponse = mapper.readValue(response, new TypeReference<BaseResponse<Url>>() {
        });

        final MvcResult mvcResult = mockMvc.perform(get("/shorten/" + baseResponse.getData().getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).contains(url);
    }
}
