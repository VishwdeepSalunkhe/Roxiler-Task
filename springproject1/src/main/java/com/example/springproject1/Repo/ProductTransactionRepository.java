package com.example.springproject1.Repo;

import com.example.springproject1.entity.ProductTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductTransactionRepository extends JpaRepository<ProductTransaction, Long> {

    @Query("SELECT p FROM ProductTransaction p WHERE " +
            "(LOWER(p.title) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR CAST(p.price AS string) LIKE CONCAT('%', :searchText, '%')) " +
            "AND FUNCTION('MONTH', p.dateOfSale) = :month")
    Page<ProductTransaction> searchTransactions(
            @Param("searchText") String searchText,
            @Param("month") int month,
            Pageable pageable);


    @Query("SELECT SUM(p.price) FROM ProductTransaction p WHERE FUNCTION('MONTH', p.dateOfSale) = :month AND p.sold = true")
    double calculateTotalSaleAmount(@Param("month") int month);

    @Query("SELECT COUNT(p) FROM ProductTransaction p WHERE FUNCTION('MONTH', p.dateOfSale) = :month AND p.sold = :sold")
    long countBySoldStatus(@Param("sold") boolean sold, @Param("month") int month);
}

