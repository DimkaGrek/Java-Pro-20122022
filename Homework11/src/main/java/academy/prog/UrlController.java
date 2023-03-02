package academy.prog;

import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
public class UrlController {
    private final UrlService urlService;

    private final RestTemplate restTemplate;

    public UrlController(UrlService urlService, RestTemplate restTemplate) {
        this.urlService = urlService;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/shorten")
    public UrlResultDTO shorten(@RequestBody UrlDTO urlDTO) {
        long id = urlService.saveUrl(urlDTO);

        var result = new UrlResultDTO();
        result.setUrl(urlDTO.getUrl());
        result.setShortUrl(Long.toString(id));

        return result;
    }

    // Задача 2
    @PostMapping("short")
    public ResponseEntity<String> fromForm(@RequestParam String url, HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        String baseUrl = requestURL.substring(0, requestURL.length() - request.getRequestURI().length() + request.getContextPath().length());
        //System.out.println("Full URI: " + baseUrl);

        // Определяем параметры и заголовки для POST запроса
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject requestBody = new JSONObject();
        requestBody.put("url", url);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/shorten", requestEntity, String.class);

        return response;
    }

    @GetMapping("my/{id}")
    public ResponseEntity<Void> redirect(@PathVariable("id") Long id) {
        var url = urlService.getUrl(id);

        var headers = new HttpHeaders();
        headers.setLocation(URI.create(url));
        headers.setCacheControl("no-cache, no-store, must-revalidate");

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    // Задача 1
    @DeleteMapping("my/{id}")
    public ResponseEntity<String> deleteUrl(@PathVariable("id") Long id) {
        if(urlService.delUrl(id)) {
            String json = "{\"URL deleted successfully\"}";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            return new ResponseEntity<>(json, headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("stat")
    public List<UrlStatDTO> stat() {
        return urlService.getStatistics();
    }
}
