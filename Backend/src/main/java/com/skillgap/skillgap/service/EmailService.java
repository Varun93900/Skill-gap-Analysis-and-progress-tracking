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
            String apiKey = System.getenv("BREVO_API_KEY");

            if (apiKey == null || apiKey.isBlank()) {
                throw new IllegalStateException("BREVO_API_KEY is not configured");
            }

            String jsonBody = """
                    {
                      "sender": {
                        "name": "SkillGap",
                        "email": "varunallam870@gmail.com"
                      },
                      "to": [
                        {
                          "email": "%s"
                        }
                      ],
                      "subject": "SkillGap Email Verification OTP",
                      "htmlContent": "<html><body><h2>SkillGap Email Verification</h2><p>Your OTP is: <strong>%s</strong></p><p>This OTP is valid for 10 minutes.</p></body></html>",
                      "textContent": "Your SkillGap email verification OTP is: %s"
                    }
                    """.formatted(toEmail, otp, otp);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
                    .header("accept", "application/json")
                    .header("api-key", apiKey)
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                System.out.println("✅ OTP email sent successfully: " + response.body());
            } else {
                System.out.println("❌ Brevo email failed: " + response.statusCode());
                System.out.println("Response: " + response.body());
            }

        } catch (Exception e) {
            System.out.println("❌ EMAIL FAILED");
            e.printStackTrace();
        }
    }
}