package tech.avito.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.avito.dto.UrlDto;
import tech.avito.generator.HashGenerator;
import tech.avito.model.Url;
import tech.avito.repository.UrlRepository;

@Service
@Slf4j
public class UrlServiceImpl implements UrlService {
    @Value("${lengthUrl}")
    private Integer lengthUrl;

    @Value("${server.port}")
    private Integer port;

    @Value("${server.domain}")
    private String domain;

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private HashGenerator hashGenerator;

    @Override
    public Url createShortUrl(UrlDto urlDto) {
        Url url = new Url();

        String hash = urlDto.getCustomUrl() != null ? urlDto.getCustomUrl() : hashGenerator.generate(lengthUrl);
        String shortUrl = domain + port +  "/" + hash;

        url.setOriginalUrl(urlDto.getOriginalUrl());
        url.setHash(hash);
        url.setShortUrl(shortUrl);

        log.info("NEW URL IS: " + shortUrl);

        return urlRepository.save(url);
    }
}
