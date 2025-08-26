package com.asklio.procurement.dto.openai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.net.ContentHandler;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenAiResponse {

    private List<Choice> choices;

    public static class Choice {
        private Message message;

        public Message getMessage() {
            return message;
        }
    }

    public static class Message {
        private String role;
        private String content;

        public String getRole() {
            return role;
        }

        public String getContent() {
            return content;
        }
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
}

