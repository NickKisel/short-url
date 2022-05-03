package tech.avito.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import tech.avito.dto.UrlDto;
import tech.avito.model.Url;
import tech.avito.repository.UrlRepository;
import tech.avito.service.UrlServiceImpl;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/")
@Slf4j
public class UrlController {

    @Autowired
    private UrlServiceImpl urlService;

    @Autowired
    private UrlRepository urlRepository;

    @Operation(summary = "Get list of all URLs")
    @ApiResponse(responseCode = "200", description = "List of all URLs", content =
    @Content(mediaType = "application/json", schema =
    @Schema(implementation = Url.class)))

    @GetMapping("/urls")
    public List<Url> showUrls() {
        return urlRepository.findAll();
    }

    @Operation(summary = "Get URL by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "URL found", content =
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = Url.class))),
            @ApiResponse(responseCode = "404", description = "URL not found")
    })

    @GetMapping("/urls/{id}")
    public Url showUrlById(@PathVariable Long id) {

        return urlRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Url not found"));
    }

    @Operation(summary = "Create new URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "URL created", content =
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = Url.class))),
            @ApiResponse(responseCode = "422", description = "Original URL not valid")
    })

    @PostMapping("/urls")
    @ResponseStatus(CREATED)
    public Url createShortUrl(@RequestBody @Valid UrlDto urlDto) {

        log.info("Url created");

        return urlService.createShortUrl(urlDto);
    }

    @Operation(summary = "Redirect on original URL by your short URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Successful redirect"),
            @ApiResponse(responseCode = "404", description = "URL not found")
    })

    @GetMapping("/{hash}")
    public ResponseEntity redirectByShortUrl(@PathVariable String hash) {

        Url url = urlRepository.findByHash(hash)
                .orElseThrow(() -> new NoSuchElementException("Url not found"));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", url.getOriginalUrl());

        log.info("Success redirect");

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
