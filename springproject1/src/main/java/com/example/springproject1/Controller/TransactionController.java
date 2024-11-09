package com.example.springproject1.Controller;

import com.example.springproject1.Repo.ProductTransactionRepository;
import com.example.springproject1.Serviexe.ProductTransactionService;
import com.example.springproject1.entity.ProductTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private ProductTransactionService service;

    @Autowired
    private ProductTransactionRepository repository;

    @GetMapping("/view")
    public String viewTransactionsPage() {
        return "transactions";
    }

    @GetMapping("/api")
    @ResponseBody
    public Page<ProductTransaction> listTransactions(
            @RequestParam(value = "month", required = false, defaultValue = "3") Integer month,
            @RequestParam(value = "search", required = false, defaultValue = "") String search,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "per_page", defaultValue = "10") int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage);
        return repository.searchTransactions(search, month, pageable);
    }
    @GetMapping("/api/statistics")
    @ResponseBody
    public Map<String, Object> getStatistics(
            @RequestParam(value = "month", required = false, defaultValue = "3") Integer month) {

        double totalSaleAmount = repository.calculateTotalSaleAmount(month);
        long totalSoldItems = repository.countBySoldStatus(true, month);
        long totalNotSoldItems = repository.countBySoldStatus(false, month);

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalSaleAmount", totalSaleAmount);
        statistics.put("totalSoldItems", totalSoldItems);
        statistics.put("totalNotSoldItems", totalNotSoldItems);

        return statistics;
    }
}

