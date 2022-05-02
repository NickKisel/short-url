### Status check
[![Maintainability](https://api.codeclimate.com/v1/badges/953c5f69d12a42c52891/maintainability)](https://codeclimate.com/github/NickKisel/short-url/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/953c5f69d12a42c52891/test_coverage)](https://codeclimate.com/github/NickKisel/short-url/test_coverage)
[![Java CI](https://github.com/NickKisel/short-url/actions/workflows/JAVA-CI.yml/badge.svg)](https://github.com/NickKisel/short-url/actions/workflows/JAVA-CI.yml)

## Short Url
Short Url - application for creating short URL. You can create random URL (by default) or your custom URL.

## How to use
### `POST "/urls"` - creating URL
For random URL required body in JSON:
```
{
    "originalUrl" : "http://example-url.com"
}
```
For custom URL required body in JSON:
```
{
    "originalUrl" : "http://example-url.com",
    "customUrl" : "custom-url"
}
```
### `GET "/urls"` - show all URLs
### `GET "/{hash}"` - redirect on your original URL