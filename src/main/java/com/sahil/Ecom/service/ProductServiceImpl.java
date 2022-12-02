package com.sahil.Ecom.service;

import com.sahil.Ecom.dto.product.AddProductDTO;
import com.sahil.Ecom.dto.product.variation.AddProductVariationDTO;
import com.sahil.Ecom.entity.Category;
import com.sahil.Ecom.entity.Product;
import com.sahil.Ecom.entity.ProductVariation;
import com.sahil.Ecom.entity.Seller;
import com.sahil.Ecom.exception.CategoryHierarchyException;
import com.sahil.Ecom.exception.IdNotFoundException;
import com.sahil.Ecom.exception.UserEmailNotFoundException;
import com.sahil.Ecom.repository.*;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariationRepository productVariationRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    @Autowired
    private SellerRepository  sellerRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public void addProduct(AddProductDTO addProductDTO, String username) {

        Product product = new Product();

        product.setName(addProductDTO.getName());
        product.setBrand(addProductDTO.getBrand());
        product.setDescription(addProductDTO.getDescription());
        product.setBrand(addProductDTO.getBrand());
        product.setCancellable(addProductDTO.isCancellable());


        Category category = categoryRepository
                                .findById(addProductDTO.getCategoryId())
                                .orElseThrow(IdNotFoundException::new);

        if(category.getChildren().size()>0){
            throw new CategoryHierarchyException("PRODUCT CAN ONLY BE ADDED IN LEAF CATEGORIES");
        }



        product.setCategory(category);
        product.setReturnable(addProductDTO.isReturnable());

        Seller seller =sellerRepository.findByEmail(username).orElseThrow(UserEmailNotFoundException::new);


        product.setSeller(seller);


        product.setDeleted(false);

        product.setActive(false);

        productRepository.save(product);

        //send mail to admin regarding product activation with product data

    }


    @Override
    public void addProductVariation(AddProductVariationDTO addProductVariationDTO) {

        Product savedProduct = productRepository
                .findById(addProductVariationDTO.getProductId())
                .orElseThrow(IdNotFoundException::new);

        ProductVariation productVariation = new ProductVariation();

        productVariation.setQuantityAvailable(addProductVariationDTO.getQuantityAvailable());

        productVariation.setPrice(addProductVariationDTO.getPrice());
        productVariation.setMetadata(addProductVariationDTO.getMetadata());
        productVariation.setActive(true);
        productVariation.setProduct(savedProduct);

        productVariationRepository.save(productVariation);


    }
}
