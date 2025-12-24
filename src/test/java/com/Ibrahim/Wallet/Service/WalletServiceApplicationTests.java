package com.Ibrahim.Wallet.Service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WalletServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateWallet() throws Exception {
        String requestJson = """
            {
                "userId": "user123",
                "currency": "NGN"
            }
            """;

        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.userId").value("user123"));
    }

    @Test
    void testTransferWithInsufficientFunds() throws Exception {
        // Setup wallets first
        // Then attempt transfer

        String transferJson = """
            {
                "senderWalletId": "sender-id",
                "receiverWalletId": "receiver-id",
                "amount": 100000,
                "reference": "TRF-001"
            }
            """;

        mockMvc.perform(post("/api/v1/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transferJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }
}