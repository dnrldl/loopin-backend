package com.loopin.loopinbackend.domain.test;

import com.loopin.loopinbackend.global.security.annotation.PublicApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@PublicApi("/test")
public class TestController {
    private final TestService testService;

    @GetMapping
    public ResponseEntity<TestDto> test(@RequestBody String test) {
        TestDto testData = testService.getTestData();
        System.out.println("testData = " + testData.toString());
        System.out.println("test = " + test);

        return ResponseEntity.ok(testData);
    }

    @GetMapping("/get")
    public ResponseEntity<Map<String, Object>> testGet(@RequestBody String test) {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> items = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("id", "1");
        item.put("name", "name");
        items.add(item);
        items.add(item);

        Map<String, Object> vlidInfo = new HashMap<>();
        vlidInfo.put("dtm", "20000000");
        item.put("vlidInfo", vlidInfo);

        response.put("items", items);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/post")
    public ResponseEntity<TestDto> testPost(@RequestBody TestDto testDto) throws IllegalAccessException {
        if (testDto == null) throw new IllegalAccessException("asd");

        return ResponseEntity.ok(testDto);
    }
}
