package tech.avito.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import tech.avito.dto.UrlDto;
import tech.avito.model.Url;
import tech.avito.repository.UrlRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class UrlControllerTest {
    private final String originalUrl1 = "https://ru.hexlet.io/u/ncklksl";
    private final String originalUrl2 = "https://github.com/NickKisel";
    private final String customUrl1 = "test-url";
    private final String customUrl2 = "another-test-url";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void createUrlTestDefault() throws Exception {
        assertThat(urlRepository.count()).isEqualTo(0);

        UrlDto urlDto = new UrlDto(originalUrl1, null);
        MockHttpServletResponse response = mockMvc.perform(
                        post("/urls")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(urlDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        assertThat(urlRepository.count()).isEqualTo(1);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
    }

    @Test
    public void createUrlTestCustom() throws Exception {
        assertThat(urlRepository.count()).isEqualTo(0);

        UrlDto urlDto = new UrlDto(originalUrl1, customUrl1);
        ResultActions actions = mockMvc.perform(
                        post("/urls")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(urlDto)))
                .andExpect(status().isCreated());

        assertThat(urlRepository.count()).isEqualTo(1);

        Url existUrl = urlRepository.findAll().get(0);
        assertThat(existUrl.getShortUrl()).contains(customUrl1);
    }

    @Test
    public void createUrlTestNegative() throws Exception {
        assertThat(urlRepository.count()).isEqualTo(0);

        UrlDto urlDto = new UrlDto("bot valid url", null);
        MockHttpServletResponse postResponse = mockMvc.perform(
                        post("/urls")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(urlDto)))
                .andExpect(status().is(422))
                .andReturn()
                .getResponse();

        assertThat(urlRepository.count()).isEqualTo(0);
    }

    @Test
    public void showUrlsTest() throws Exception {
        assertThat(urlRepository.count()).isEqualTo(0);

        UrlDto urlDto1 = new UrlDto(originalUrl1, customUrl1);
        UrlDto urlDto2 = new UrlDto(originalUrl2, customUrl2);

        ResultActions actions1 = mockMvc.perform(
                        post("/urls")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(urlDto1)))
                .andExpect(status().isCreated());

        assertThat(urlRepository.count()).isEqualTo(1);

        ResultActions actions2 = mockMvc.perform(
                        post("/urls")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(urlDto2)))
                .andExpect(status().isCreated());

        assertThat(urlRepository.count()).isEqualTo(2);

        MockHttpServletResponse getResponse = mockMvc.perform(
                        get("/urls"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertThat(getResponse.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(getResponse.getContentAsString()).contains(originalUrl1, originalUrl2, customUrl1, customUrl2);
    }

    @Test
    public void showUrlByIdTest() throws Exception {
        assertThat(urlRepository.count()).isEqualTo(0);

        UrlDto urlDto1 = new UrlDto(originalUrl1, customUrl1);
        ResultActions actions = mockMvc.perform(
                        post("/urls")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(urlDto1)))
                .andExpect(status().isCreated());

        assertThat(urlRepository.count()).isEqualTo(1);

        Long id = urlRepository.findAll().get(0).getId();

        MockHttpServletResponse getResponse = mockMvc.perform(
                        get("/urls/" + id))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertThat(getResponse.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(getResponse.getContentAsString()).contains(id.toString(), originalUrl1, customUrl1);
    }

    @Test
    public void showUrlByIdNegativeTest() throws Exception {
        assertThat(urlRepository.count()).isEqualTo(0);

        UrlDto urlDto1 = new UrlDto(originalUrl1, customUrl1);
        ResultActions actions = mockMvc.perform(
                        post("/urls")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(urlDto1)))
                .andExpect(status().isCreated());

        assertThat(urlRepository.count()).isEqualTo(1);

        Long id = urlRepository.findAll().get(0).getId();

        ResultActions getResponse = mockMvc.perform(
                        get("/urls/" + (id + 1)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void redirectTest() throws Exception {
        assertThat(urlRepository.count()).isEqualTo(0);

        UrlDto urlDto = new UrlDto(originalUrl1, null);
        MockHttpServletResponse postResponse = mockMvc.perform(
                        post("/urls")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(urlDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        assertThat(urlRepository.count()).isEqualTo(1);

        Url existUrl = urlRepository.findAll().get(0);
        String existUrlHash = existUrl.getHash();
        MockHttpServletResponse getResponse = mockMvc.perform(
                        get("/" + existUrlHash))
                .andExpect(status().is(302))
                .andReturn()
                .getResponse();

        assertThat(getResponse.getHeader("Location")).isEqualTo(originalUrl1);
    }
}
