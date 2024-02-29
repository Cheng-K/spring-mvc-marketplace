package com.chengk.springmvcmarketplace.domain;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.chengk.springmvcmarketplace.model.dto.CartDto;
import com.chengk.springmvcmarketplace.model.dto.ProductDto;
import com.chengk.springmvcmarketplace.model.entity.Cart;
import com.chengk.springmvcmarketplace.model.entity.CartProductRef;
import com.chengk.springmvcmarketplace.model.entity.Products;
import com.chengk.springmvcmarketplace.repository.ProductRepository;

@Service
public class CartDtoConverter implements DtoConverter<Cart, CartDto> {

    private ProductRepository productRepository;
    private DtoConverter<Products, ProductDto> productDtoConverter;

    public CartDtoConverter(ProductRepository productRepository,
            DtoConverter<Products, ProductDto> productDtoConverter) {
        this.productRepository = productRepository;
        this.productDtoConverter = productDtoConverter;
    }

    @Override
    public CartDto convertToDto(Cart element) {
        List<ProductDto> productDto = element.getProducts().stream().map((cartProductRef) -> {
            Optional<Products> product = productRepository.findById(cartProductRef.getProductId());
            if (product.isEmpty())
                return null;
            return productDtoConverter.convertToDto(product.get());
        }).collect(Collectors.toList());
        return new CartDto(element.getId(), productDto);
    }

    @Override
    public Cart convertToEntity(CartDto element) {
        Set<CartProductRef> cartProductRef = element.getProducts().stream()
                .map((productDto) -> new CartProductRef(productDto.getId())).collect(Collectors.toSet());
        return new Cart(element.getId(), cartProductRef);
    }

}
