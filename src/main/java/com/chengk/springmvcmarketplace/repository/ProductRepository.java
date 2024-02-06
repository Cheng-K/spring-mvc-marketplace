package com.chengk.springmvcmarketplace.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.chengk.springmvcmarketplace.model.entity.Products;

@Repository
public interface ProductRepository
                extends PagingAndSortingRepository<Products, Integer>, CrudRepository<Products, Integer> {

        Slice<Products> findByTitleIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(String title,
                        String description, Pageable pageable);

        Slice<Products> findByCategoryId(Integer categoryId, Pageable pageable);

        Slice<Products> findBySellerId(Integer sellerId, Pageable pageable);

        Slice<Products> findBySellerIdAndTitleIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(Integer sellerId,
                        String title,
                        String description, Pageable pageable);

        Slice<Products> findByCategoryIdAndSellerId(Integer categoryId, Integer sellerId, Pageable pageable);
}
