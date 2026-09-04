package com.skillgap.skillgap.service;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class EmailService {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public void sendOtp(String toEmail, String otp) {

        try {
            String apiKey = System.getenv("RESEND_API_KEY");

            if (apiKey == null || apiKey.isBlank()) {
                throw new IllegalStateException("RESEND_API_KEY is not configured");
            }

            String jsonBody = """
                    {
                      "from": "onboarding@resend.dev",
                      "to": ["%s"],
                      "subject": "SkillGap Email Verification OTP",
                      "html": "<p>Your OTP for SkillGap account verification is: <strong>%s</strong></p>"
                    }
                    """.formatted(toEmail, otp);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.resend.com/emails"))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                System.out.println("✅ OTP email sent successfully");
            } else {
                System.out.println("❌ Resend email failed: " + response.body());
            }

        } catch (Exception e) {
            System.out.println("❌ EMAIL FAILED");
            e.printStackTrace();
        }
    }
}