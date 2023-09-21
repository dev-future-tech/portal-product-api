package com.portal.product;

import com.google.schemaorg.core.CoreFactory;
import com.google.schemaorg.core.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.access.prepost.PreAuthorize;

import reactor.core.publisher.Mono;

@RequestMapping("/product/v1")
@RestController
public class ProductController {

    private Logger log = LoggerFactory.getLogger(ProductController.class);

    @PreAuthorize("hasRole('ROLE_portal-product-catalog-access')")
    @GetMapping("/{productId}")
    public ResponseEntity<Mono<Product>> getProductById(@PathVariable("productId") String productId) {
        log.info("Getting product with id {}", productId);
        Product product = CoreFactory.newProductBuilder().addProductID(productId)
                .addBrand("NegVoo")
                .addImage("/meds/grando.png")
                .addSku("53234")
                .addWeight("0.5 FlOz")
                .build();
        return ResponseEntity.ok().body(Mono.just(product));
    }
}
