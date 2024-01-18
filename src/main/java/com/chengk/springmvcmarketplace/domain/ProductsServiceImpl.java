package com.chengk.springmvcmarketplace.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.chengk.springmvcmarketplace.model.dto.ProductDto;
import com.chengk.springmvcmarketplace.model.entity.Products;
import com.chengk.springmvcmarketplace.repository.ProductRepository;

@Service
public class ProductsServiceImpl implements ProductsService {

    private ProductRepository productRepository;
    private DtoConverter<Products, ProductDto> productDtoConverter;
    private StorageService storageService;
    private PageRequest pageRequest = PageRequest.of(0, 10);

    public ProductsServiceImpl(ProductRepository productRepository,
            DtoConverter<Products, ProductDto> productDtoConverter, StorageService storageService) {
        this.productRepository = productRepository;
        this.productDtoConverter = productDtoConverter;
        this.storageService = storageService;
    }

    @Override
    public void addNewProduct(ProductDto productDto) {
        try {
            String fileName = saveImageFile(productDto);
            productDto.setImage(fileName);
            productRepository.save(productDtoConverter.convertToEntity(productDto));
        } catch (IOException e) {
            System.err.println("Cannot read the bytes of multipart file");
            e.printStackTrace();
            return;
        }
    }

    @Override
    public ProductDto getProductById(Integer productId) {
        Optional<Products> foundProduct = productRepository.findById(productId);
        if (foundProduct.isEmpty())
            return null;
        return productDtoConverter.convertToDto(foundProduct.get());
    }

    @Override
    public void editProduct(ProductDto productDto) {
        if (productDto.getImageFile() != null && !productDto.getImageFile().isEmpty()) {
            try {
                storageService.deleteFile("./src/main/resources/images/products/" + productDto.getImage());
                String fileName = saveImageFile(productDto);
                productDto.setImage(fileName);
            } catch (IOException e) {
                System.err.println("Cannot read the bytes of multipart file");
                e.printStackTrace();
                return;
            } catch (Exception e) {
                System.err.println("Cannot delete file");
                e.printStackTrace();
                return;
            }
        }
        productRepository.save(productDtoConverter.convertToEntity(productDto));
    }

    private String saveImageFile(ProductDto productDto) throws IOException {
        String fileName = storageService.replaceFileNameWithUUID(productDto.getImageFile().getOriginalFilename());
        storageService.saveFile(productDto.getImageFile().getBytes(),
                fileName, "./src/main/resources/images/products");
        return fileName;
    }

    @Override
    public void removeProduct(Integer productId) {
        Products product = productRepository.findById(productId).get();
        if (product == null)
            return;
        try {
            storageService.deleteFile("./src/main/resources/images/products/" + product.getImage());
        } catch (IOException e) {
            System.err.println("Cannot read the bytes of multipart file");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Cannot delete file");
            e.printStackTrace();
        } finally {
            productRepository.deleteById(productId);
        }
    }

    @Override
    public Slice<ProductDto> getAllProductsWithPagination(int pageNumber) {
        Slice<Products> products = productRepository.findAll(pageRequest.withPage(pageNumber));
        return products.map((product) -> productDtoConverter.convertToDto(product));
    }

    @Override
    public Slice<ProductDto> getProductsByQuery(String query, int pageNumber) {
        Slice<Products> products = productRepository.findByTitleIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(
                query, query, pageRequest.withPage(pageNumber));
        return products.map((product) -> productDtoConverter.convertToDto(product));
    }

    @Override
    public Slice<ProductDto> getAllProductsWithSort(Sort sort, int pageNumber) {
        Slice<Products> products = productRepository.findAll(pageRequest.withPage(pageNumber).withSort(sort));
        return products.map((product) -> productDtoConverter.convertToDto(product));
    }

    @Override
    public Slice<ProductDto> getProductsByCategoryId(Integer categoryId, int pageNumber) {
        Slice<Products> products = productRepository.findByCategoryId(categoryId, pageRequest.withPage(pageNumber));
        return products.map((product) -> productDtoConverter.convertToDto(product));
    }

    @Override
    public Slice<ProductDto> getProductsByQueryWithSort(String query, Sort sort, int pageNumber) {
        Slice<Products> products = productRepository.findByTitleIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(
                query, query, pageRequest.withPage(pageNumber).withSort(sort));
        return products.map((product) -> productDtoConverter.convertToDto(product));
    }

}
