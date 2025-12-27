package com.aje.salesforce.infrastructure.adapter.out.salesforce.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SalesforceAuthResponse {
    
    @JsonProperty("access_token")
    private String accessToken;
    
    @JsonProperty("instance_url")
    private String instanceUrl;
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("token_type")
    private String tokenType;
    
    @JsonProperty("issued_at")
    private String issuedAt;
    
    @JsonProperty("signature")
    private String signature;
}
