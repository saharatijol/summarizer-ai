package com.research.summarizer_ai;

import org.springframework.stereotype.Service;

@Service
public class SummarizerService {
    public String processContent(SummarizerRequest request) {

        // build the prompt
        // query the ai model api
        // parse the response
        // return response
    }

    private String buildPrompt(SummarizerRequest request) {
        StringBuilder prompt = new StringBuilder();

        switch(request.getOperation()) {
            case "summarize":
                prompt.append("");
        }
    }
}
