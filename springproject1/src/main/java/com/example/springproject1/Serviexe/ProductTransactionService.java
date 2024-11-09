package com.example.springproject1.Serviexe;

import com.example.springproject1.Repo.ProductTransactionRepository;
import com.example.springproject1.entity.ProductTransaction;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

@Service
public class ProductTransactionService {

    private static final String API_URL = "https://s3.amazonaws.com/roxiler.com/product_transaction.json";

    @Autowired
    private ProductTransactionRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void initializeDatabase() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String json = restTemplate.getForObject(API_URL, String.class);

            List<ProductTransaction> transactions = objectMapper.readValue(json, new TypeReference<List<ProductTransaction>>() {});
            repository.saveAll(transactions);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
