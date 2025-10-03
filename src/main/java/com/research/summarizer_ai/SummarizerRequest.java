package com.research.summarizer_ai;

import lombok.Data;

@Data
public class SummarizerRequest {
    private String content;
    private String operation;
}
