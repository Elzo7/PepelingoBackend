package com.example.pepelingbackendv2.Responses;

import java.lang.reflect.Array;
import java.util.List;

public class ErrorResponse {
    private boolean error;
    private List<String> messages;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
