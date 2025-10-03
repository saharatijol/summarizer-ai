package com.research.summarizer_ai;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SummarizerService {
    public String processContent(SummarizerRequest request) {

        // build the prompt
        String prompt = buildPrompt(request);

        // query the ai model api
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[] {
                        Map.of("parts", new Object[] {
                            Map.of("text", prompt)
                        })
                }
        );
        // parse the response
        // return response
    }

    private String buildPrompt(SummarizerRequest request) {
        StringBuilder prompt = new StringBuilder();

        switch(request.getOperation()) {
            case "summarize":
                prompt.append("Summarize the following highlighted text in clear, concise language. Focus only on the " +
                        "key points and main ideas. Do not add outside information, and avoid unnecessary details or " +
                        "repetition. If the text contains multiple ideas, present them in a logical order. Provide the " +
                        "summary in 3–4 sentences:\n\n");
                break;

            case "suggest":
                prompt.append("Based on the following content: suggest related topics and further reading. " +
                        "Format the response with clear headings and bullet points:\n\n");
                break;
            default:
                throw new IllegalArgumentException("Unknown Operation: " + request.getOperation());
        }
        prompt.append(request.getContent());
        return prompt.toString();
    }
}
