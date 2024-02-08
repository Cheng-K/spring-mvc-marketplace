package com.chengk.springmvcmarketplace.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.chengk.springmvcmarketplace.model.entity.Products;

@Repository
public interface ProductRepository
                extends PagingAndSortingRepository<Products, Integer>, CrudRepository<Products, Integer> {

        Slice<Products> findByTitleIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(String title,
                        String description, Pageable pageable);

        Slice<Products> findByCategoryId(Integer categoryId, Pageable pageable);

        Slice<Products> findBySellerId(Integer sellerId, Pageable pageable);

        @Query("SELECT * FROM \"Products\" WHERE seller_id = :sellerId AND (UPPER(title) LIKE CONCAT('%',UPPER(:title),'%') OR UPPER(description) LIKE CONCAT('%',UPPER(:description),'%'))")

        List<Products> findBySellerIdAndTitleIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(
                        @Param("sellerId") Integer sellerId,
                        @Param("title") String title,
                        @Param("description") String description, Pageable pageable);

        @Query("SELECT COUNT(*) FROM \"Products\" WHERE seller_id = :sellerId AND (UPPER(title) LIKE CONCAT('%',UPPER(:title),'%') OR UPPER(description) LIKE CONCAT('%',UPPER(:description),'%'))")

        Integer getCountBySellerIdAndTitleIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(
                        @Param("sellerId") Integer sellerId,
                        @Param("title") String title,
                        @Param("description") String description);

        Slice<Products> findByCategoryIdAndSellerId(Integer categoryId, Integer sellerId, Pageable pageable);
}
