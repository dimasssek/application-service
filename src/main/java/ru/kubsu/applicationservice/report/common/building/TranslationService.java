package ru.kubsu.applicationservice.report.common.building;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class TranslationService {

    private final RestTemplate restTemplate;

    public String translateToGerman(String text) {
        String url = "https://api.mymemory.translated.net/get?q={query}&langpair={langpair}";
        var response = restTemplate.getForEntity(url, Map.class, text, "ru|de");
        return (String) ((Map) response.getBody().get("responseData")).get("translatedText");
    }
}
