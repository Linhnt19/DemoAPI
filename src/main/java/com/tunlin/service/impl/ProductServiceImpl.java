package com.tunlin.service.impl;

import com.tunlin.exceptions.ProductException;
import com.tunlin.model.Category;
import com.tunlin.model.Product;
import com.tunlin.model.Seller;
import com.tunlin.repository.CategoryRepository;
import com.tunlin.repository.ProductRepository;
import com.tunlin.request.CreateProductRequest;
import com.tunlin.service.ProductService;
import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    @Override
    public Product createProduct(CreateProductRequest rep, Seller seller) throws Exception {

        Category category1 = categoryRepository.findByCategoryId(rep.getCategory1());
        if (category1 == null){
            Category category = new Category();
            category.setCategoryId(rep.getCategory1());
            category.setLevel(1);
            category1 = categoryRepository.save(category);
        }

        Category category2 = categoryRepository.findByCategoryId(rep.getCategory2());
        if (category2 == null){
            Category category = new Category();
            category.setCategoryId(rep.getCategory2());
            category.setLevel(2);
            category.setParentCategory(category1);
            category2 = categoryRepository.save(category);
        }
        Category category3 = categoryRepository.findByCategoryId(rep.getCategory3());
        if (category3 == null){
            Category category = new Category();
            category.setCategoryId(rep.getCategory3());
            category.setLevel(3);
            category.setParentCategory(category2);
            category3 = categoryRepository.save(category);
        }

        int discountPercentage = calculateDiscountPercentage(rep.getMrpPrice(), rep.getSellingPrice());

        Product product = new Product();
        product.setSeller(seller);
        product.setCategory(category3);
        product.setDescription(rep.getDescription());
        product.setCreatedAt(LocalDateTime.now());
        product.setTitle(rep.getTitle());
        product.setColor(rep.getColor());
        product.setSellingPrice(rep.getSellingPrice());
        product.setImages(rep.getImages());
        product.setMrpPrice(rep.getMrpPrice());
        product.setSizes(rep.getSizes());
        product.setDiscountPercent(discountPercentage);
        return productRepository.save(product);

    }

    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) throws Exception {

        if (mrpPrice <= 0){
            throw new Exception("Actual price must be greater than 0");
        }
        double discount = mrpPrice - sellingPrice;
        double discountPercentage = (discount/mrpPrice)*100;
        return (int)discountPercentage;

    }

    @Override
    public void deleteProduct(Long productId) throws ProductException {

        Product product = findProductById(productId);
        productRepository.delete(product);

    }

    @Override
    public Product updateProduct(Long productId, Product product) throws ProductException {

        findProductById(product.getId());
        product.setId(productId);
        return productRepository.save(product);

    }


    @Override
    public Product findProductById(Long productId) throws ProductException {
        return productRepository.findById(productId).orElseThrow(()->
                new ProductException(("Product not found with "+ productId)));
    }

    @Override
    public List<Product> searchProducts(String query) {
        return productRepository.searchProduct(query);
    }

    @Override
    public Page<Product> getAllProducts(String category, String brand, String colors, String sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber) {
        Specification<Product> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> perdicates = new ArrayList<>();

            if (category != null){
                Join<Product, Category> categoryJoin = root.join("category");
                perdicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"),category));
            }
            if (colors != null && !colors.isEmpty()){
                perdicates.add( criteriaBuilder.equal(root.get("color"),colors));
            }
            if (sizes != null && !sizes.isEmpty()){
                perdicates.add( criteriaBuilder.equal(root.get("size"),sizes));
            }
            if (minPrice != null){
                perdicates.add( criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"),minPrice));
            }
            if (maxPrice != null){
                perdicates.add( criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"),maxPrice));
            }
            if (minDiscount != null){
                perdicates.add( criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercentage"),minDiscount));
            }
            if (stock != null){
                perdicates.add( criteriaBuilder.equal(root.get("stock"),stock));
            }
            return criteriaBuilder.and(perdicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };

        Pageable pageable;
        if (sort != null && !sort.isEmpty()){
            switch (sort){
                case "price_low":
                    pageable = PageRequest.of(pageNumber!=null? pageNumber:0, 10,Sort.by("sellingPrice").ascending());
                    break;

                case "price_high":
                    pageable = PageRequest.of(pageNumber!=null? pageNumber:0, 10,Sort.by("sellingPrice").descending());
                    break;

                default:
                    pageable = PageRequest.of(pageNumber!=null? pageNumber:0, 10,Sort.unsorted());
                    break;
            }
        }
        else {
            pageable = PageRequest.of(pageNumber != null ? pageNumber :0,10,Sort.unsorted());
        }
        return productRepository.findAll(spec, pageable);
    }

    @Override
    public List<Product> getProductBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }
}
