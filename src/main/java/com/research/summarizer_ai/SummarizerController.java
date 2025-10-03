package com.research.summarizer_ai;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/summarizer")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class SummarizerController {
    private final SummarizerService summarizerService;

    @PostMapping("/process")
    public ResponseEntity<String> processContent(@RequestBody SummarizerRequest request) {
        String result = summarizerService.processContent(request);
        return ResponseEntity.ok(result);
    }
}
