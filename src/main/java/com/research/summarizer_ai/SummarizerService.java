package com.research.summarizer_ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class SummarizerService {
    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public SummarizerService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
    }


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

        String response = webClient.post()
                .uri(geminiApiUrl + geminiApiKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        
        // parse the response
        
        // return response
        return extractTextFromResponse(response);
    }

    private String extractTextFromResponse(String response) {
        try {
            // json response to objects
            GeminiResponse geminiResponse = objectMapper.readValue(response, GeminiResponse.class);

            // checking responses if exists
            if(geminiResponse.getCandidates() != null && geminiResponse.getCandidates().isEmpty()) {
                GeminiResponse.Candidate firstCandidate = geminiResponse.getCandidates().get(0);

                if(firstCandidate.getContent() != null &&
                        firstCandidate.getContent().getParts() != null &&
                        !firstCandidate.getContent().getParts().isEmpty()) {
                    return firstCandidate.getContent().getParts().get(0).getText();
                }
            }
            return "No content found in response";
        } catch (Exception e) {
            return "Error Parsing: " + e.getMessage();
        }

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
