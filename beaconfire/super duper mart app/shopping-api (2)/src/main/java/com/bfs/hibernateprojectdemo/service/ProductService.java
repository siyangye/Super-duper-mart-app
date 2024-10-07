package com.bfs.hibernateprojectdemo.service;

import com.bfs.hibernateprojectdemo.dao.ProductDao;
import com.bfs.hibernateprojectdemo.domain.Product;
import com.bfs.hibernateprojectdemo.domain.dto.product.ProductRequest;
import com.bfs.hibernateprojectdemo.domain.vo.ProductUserVO;
import com.bfs.hibernateprojectdemo.domain.vo.ProductVO;
import com.bfs.hibernateprojectdemo.exception.NotEnoughInventoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private ProductDao productDao;

    @Autowired
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    @Transactional
    public List<Product> getAllAvailableProducts() {
        return productDao.getAllAvailableProducts();
    }

    @Transactional
    public List<ProductUserVO> getAllAvailableProductsForUser() {
        return productDao.getAllAvailableProducts().stream().map(product -> {
            ProductUserVO productUserVO = new ProductUserVO();
            productUserVO.setProductId(product.getProductId());
            productUserVO.setName(product.getName());
            productUserVO.setDescription(product.getDescription());
            productUserVO.setRetailPrice(product.getRetailPrice());
            return productUserVO;
        }).collect(Collectors.toList());
    }

    @Transactional
    public Product purchaseProduct(int productId, int quantity) {
        Product product = productDao.getProductById(productId);
        if (product == null) {
            throw new RuntimeException("Product not found");
        }
        if (product.getQuantity() < quantity) {
            throw new NotEnoughInventoryException("Not enough inventory for product ID: " + productId);
        }
        productDao.updateProductQuantity(product, quantity);
        return product;
    }

    @Transactional
    public void cancelOrder(int productId, Integer quantity) {
        Product product = productDao.getProductById(productId);
        productDao.updateProductQuantity(product, quantity);
    }

    @Transactional
    public ProductVO getProductById(int id) {
        Product product = productDao.getProductById(id);
        return ProductVO.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .wholesalePrice(product.getWholesalePrice())
                .retailPrice(product.getRetailPrice())
                .description(product.getDescription())
                .quantity(product.getQuantity())
                .build();
    }

    @Transactional
    public Product updateProduct(int id, ProductRequest productRequest) {
        Product product = productDao.getProductById(id);
        if (product != null) {
            product.setName(productRequest.getName());
            product.setDescription(productRequest.getDescription());
            product.setWholesalePrice(productRequest.getWholesalePrice());
            product.setRetailPrice(productRequest.getRetailPrice());
            product.setQuantity(productRequest.getQuantity());
            productDao.updateProduct(product);
            return product;
        }
        return null;
    }

    @Transactional
    public Product addProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setWholesalePrice(productRequest.getWholesalePrice());
        product.setRetailPrice(productRequest.getRetailPrice());
        product.setQuantity(productRequest.getQuantity());
        productDao.addProduct(product);
        return product;
    }

    @Transactional
    public List<Product> getMostProfitableProduct(int limit) {
        List<Product> allProducts = productDao.getAllProducts();
        return allProducts.stream()
                .sorted(Comparator.comparingDouble((Product product) -> (product.getRetailPrice() - product.getWholesalePrice()) * product.getQuantity()).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ProductUserVO> getProductByIds(List<Integer> topProductIds) {
        List<Product> products = productDao.getProductsByIds(topProductIds);
        return products.stream().map(product -> {
            ProductUserVO productUserVO = new ProductUserVO();
            productUserVO.setProductId(product.getProductId());
            productUserVO.setName(product.getName());
            productUserVO.setDescription(product.getDescription());
            productUserVO.setRetailPrice(product.getRetailPrice());
            return productUserVO;
        }).collect(Collectors.toList());
    }

    @Transactional
    public List<ProductUserVO> getProductBySortedIds(ArrayList<Integer> ids) {
        ArrayList<ProductUserVO> list = new ArrayList<>();
        for (Integer id : ids) {
            Product productById = productDao.getProductById(id);
            ProductUserVO productUserVO = new ProductUserVO();
            productUserVO.setProductId(productById.getProductId());
            productUserVO.setName(productById.getName());
            productUserVO.setDescription(productById.getDescription());
            productUserVO.setRetailPrice(productById.getRetailPrice());
            list.add(productUserVO);
        }
        return list;
    }
}