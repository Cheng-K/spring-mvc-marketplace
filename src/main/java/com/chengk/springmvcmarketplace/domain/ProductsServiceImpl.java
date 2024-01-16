package com.chengk.springmvcmarketplace.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public List<ProductDto> getProductsByQuery(String query) {
        List<Products> found = productRepository.findByTitleIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(query,
                query);
        List<ProductDto> result = new ArrayList<>();
        for (var product : found) {
            result.add(productDtoConverter.convertToDto(product));
        }
        return result;
    }

    @Override
    public List<ProductDto> getProductsByCategoryId(Integer categoryId) {
        List<Products> found = productRepository.findByCategoryId(categoryId);
        List<ProductDto> result = new ArrayList<>();
        for (var product : found) {
            result.add(productDtoConverter.convertToDto(product));
        }
        return result;
    }

    @Override
    public List<ProductDto> getAllProductsWithSort(Sort sort) {
        List<ProductDto> result = new ArrayList<>();
        Iterable<Products> products = productRepository.findAllBy(sort);
        for (var product : products) {
            result.add(productDtoConverter.convertToDto(product));
        }
        return result;
    }

    @Override
    public List<ProductDto> getProductsByQueryWithSort(String query, Sort sort) {
        List<Products> found = productRepository.findByTitleIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(query,
                query, sort);
        List<ProductDto> result = new ArrayList<>();
        for (var product : found) {
            result.add(productDtoConverter.convertToDto(product));
        }
        return result;
    }

}
