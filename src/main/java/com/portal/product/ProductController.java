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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/product/v1")
@RestController
public class ProductController {

    private final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final List<ProductDTO> products = new ArrayList<>();

    @GetMapping
    public ResponseEntity<Mono<List<Product>>> getProducts(){
        if (this.products.isEmpty()) {
            loadProducts();
        }

        List<Product> results = this.products.stream().map(prod -> {
            return CoreFactory.newProductBuilder().addProductID(prod.getProductId().toString())
                    .addBrand(prod.getBrand())
                    .addSku(prod.getSku())
                    .addWeight(prod.getWeight())
                    .addImage(prod.getImageUrl()).build();
        }).toList();
        return ResponseEntity.ok(Mono.just(results));
    }

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

    private void loadProducts() {
        try (InputStream is = getClass().getResourceAsStream("/products.csv")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                if(line.startsWith("id,")) continue;

                String[] product = line.split(",");
                var dto = new ProductDTO();
                dto.setProductId(Integer.parseInt(product[0]));
                dto.setBrand(product[1]);
                dto.setImageUrl(product[2]);
                dto.setSku(product[3]);
                dto.setWeight(product[4]);
                products.add(dto);
            }
        } catch (IOException ioe) {
            log.error("Error loading products", ioe);
        }
    }
}
