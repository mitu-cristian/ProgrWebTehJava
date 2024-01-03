package com.example.demo;

public class MessageResponse {
    private boolean success;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class Builder {
        private final MessageResponse response;

        public Builder() {
            response = new MessageResponse();
        }

        public Builder success(boolean success) {
            response.success = success;
            return this;
        }

        public Builder message(String message) {
            response.message = message;
            return this;
        }

        public MessageResponse build() {
            return response;
        }
    }
}
