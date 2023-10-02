package com.portal.product;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public class ProductControllerTest {

    private final Logger log = LoggerFactory.getLogger(getClass().getCanonicalName());

    @Autowired
    WebTestClient client;

    @Test
    public void testCorsOptionsRequest() throws Exception {
        log.info("Testing CORS...");
        WebTestClient.ResponseSpec response = client.options().uri("http://localhost:8090/product/v1")
                .header("Origin", "http://localhost:4200")
                .header("Host", "localhost:4200")
                .header("Access-Control-Request-Method", "GET")
                .header("Access-Control-Request-Headers", "authorization")
                .exchange();
        response.expectStatus().isOk()
                .expectHeader().valueEquals("Access-Control-Allow-Origin", "http://localhost:4200")
                .expectHeader().valueEquals("Access-Control-Allow-Methods", "GET")
                .expectBody().consumeWith(System.out::println).isEmpty();
    }

    @Test
    public void testGetAllProducts() throws Exception {
        WebTestClient.ResponseSpec response = client.get().uri("/product/v1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();
        response.expectBody().consumeWith(System.out::println)
                .jsonPath("[0].brandList[0].value").isEqualTo("Nostle");
    }

    @Test
    public void testGetProduct() throws Exception {
        WebTestClient.ResponseSpec response = client.get().uri("/product/v1/12345")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        response.expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.productIDList[0].value").isEqualTo("12345")
                .jsonPath("$.brandList[0].value").isEqualTo("NegVoo")
                .jsonPath("$.skuList[0].value").isEqualTo("53234");
    }
}
