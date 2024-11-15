package com.tunlin.service;

import com.tunlin.exceptions.ProductException;
import com.tunlin.model.Product;
import com.tunlin.model.Seller;
import com.tunlin.request.CreateProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    public Product createProduct(CreateProductRequest rep, Seller seller) throws Exception;
    public void deleteProduct(Long productId) throws ProductException;
    public Product updateProduct(Long productId, Product product) throws ProductException;
    Product findProductById(Long productId) throws ProductException;
    List<Product> searchProducts(String query);
    public Page<Product> getAllProducts(String category,
                                        String brand,
                                        String colors,
                                        String sizes,
                                        Integer minPrice,
                                        Integer maxPrice,
                                        Integer minDiscount,
                                        String sort,
                                        String stock,
                                        Integer pageNumber);
    List<Product> getProductBySellerId(Long sellerId);

}
