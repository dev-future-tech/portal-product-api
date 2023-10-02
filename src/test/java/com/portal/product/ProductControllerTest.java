package com.portal.product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers =ProductController.class)
public class ProductControllerTest {

    @Autowired
    WebTestClient client;

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
