package tech.avito.controller;

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

    @GetMapping("/urls")
    public List<Url> showUrls() {
        return urlRepository.findAll();
    }

    @GetMapping("/urls/{id}")
    public Url showUrlById(@PathVariable Long id) {

        return urlRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Url not found"));
    }

    @PostMapping("/urls")
    @ResponseStatus(CREATED)
    public Url createShorterUrlDefault(@RequestBody @Valid UrlDto urlDto) {

        log.info("Url created");

        return urlService.createShortUrl(urlDto);
    }

    @GetMapping("/{hash}")
    public ResponseEntity redirectShorterUrl(@PathVariable String hash) {

        Url url = urlRepository.findByHash(hash)
                .orElseThrow(() -> new NoSuchElementException("Url not found"));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", url.getOriginalUrl());

        log.info("Success redirect");

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
