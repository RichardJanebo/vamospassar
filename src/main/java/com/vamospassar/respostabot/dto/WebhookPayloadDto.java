package com.vamospassar.respostabot.dto;
import com.fasterxml.jackson.annotation.JsonProperty;


public class WebhookPayloadDto {
    @JsonProperty("webhook_event_type")
    private String webhookEventType;

    @JsonProperty("Customer")
    private Customer customer;

    public String getWebhookEventType() {
        return webhookEventType;
    }

    public void setWebhookEventType(String webhookEventType) {
        this.webhookEventType = webhookEventType;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public static class Customer {
        @JsonProperty("full_name")
        private String fullName;

        @JsonProperty("first_name")
        private String firstName;

        private String email;
        private String mobile;
        private String CPF;
        private String city;
        private String state;

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getMobile() { return mobile; }
        public void setMobile(String mobile) { this.mobile = mobile; }

        public String getCPF() { return CPF; }
        public void setCPF(String CPF) { this.CPF = CPF; }

        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }

        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
    }
}
