package com.chengk.springmvcmarketplace.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.chengk.springmvcmarketplace.model.dto.ProductDto;
import com.chengk.springmvcmarketplace.model.entity.Products;
import com.chengk.springmvcmarketplace.repository.ProductRepository;

@Service
public class ProductsServiceImpl implements ProductsService {

    private ProductRepository productRepository;
    private DtoConverter<Products, ProductDto> productDtoConverter;
    private StorageService storageService;

    public ProductsServiceImpl(ProductRepository productRepository,
            DtoConverter<Products, ProductDto> productDtoConverter, StorageService storageService) {
        this.productRepository = productRepository;
        this.productDtoConverter = productDtoConverter;
        this.storageService = storageService;
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<ProductDto> result = new ArrayList<>();
        Iterable<Products> products = productRepository.findAll();
        for (var product : products) {
            result.add(productDtoConverter.convertToDto(product));
        }
        return result;
    }

    @Override
    public void addNewProduct(ProductDto productDto) {
        String originalFileName = productDto.getImageFile().getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + fileExtension;
        try {
            storageService.saveFile(productDto.getImageFile().getBytes(),
                    fileName, "./src/main/resources/static/img");
        } catch (IOException e) {
            System.err.println("Cannot read the bytes of multipart file");
            e.printStackTrace();
        }
        productDto.setImage(fileName);
        productRepository.save(productDtoConverter.convertToEntity(productDto));
    }

    @Override
    public ProductDto getProductById(Integer productId) {
        Optional<Products> foundProduct = productRepository.findById(productId);
        if (foundProduct.isEmpty())
            return null;
        return productDtoConverter.convertToDto(foundProduct.get());
    }

}
