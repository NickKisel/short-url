package tech.avito.service;

import tech.avito.dto.UrlDto;
import tech.avito.model.Url;

public interface UrlService {
    Url createShortUrl(UrlDto urlDto);
}
