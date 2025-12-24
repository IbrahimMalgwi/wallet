package com.Ibrahim.Wallet.Service;

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