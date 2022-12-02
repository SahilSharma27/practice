package com.sahil.Ecom.service;

import com.sahil.Ecom.dto.product.AddProductDTO;
import com.sahil.Ecom.dto.product.variation.AddProductVariationDTO;
import com.sahil.Ecom.entity.*;
import com.sahil.Ecom.exception.CategoryHierarchyException;
import com.sahil.Ecom.exception.IdNotFoundException;
import com.sahil.Ecom.exception.UserEmailNotFoundException;
import com.sahil.Ecom.repository.*;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


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


    @Autowired
    private FileService fileService;

    Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Value("${project.image.product}")
    private String path;

    @Override
    public void addProduct(AddProductDTO addProductDTO, String username) {

        Seller seller =sellerRepository.findByEmail(username).orElseThrow(UserEmailNotFoundException::new);

        //product name should be unique for a brand category and seller
        validateProduct(addProductDTO,seller);

        Product product = new Product();

        product.setName(addProductDTO.getName());
        product.setBrand(addProductDTO.getBrand());
        product.setDescription(addProductDTO.getDescription());
        product.setBrand(addProductDTO.getBrand());
        product.setCancellable(addProductDTO.isCancellable());


        Category category = categoryRepository
                                .findById(addProductDTO.getCategoryId())
                                .orElseThrow(IdNotFoundException::new);

        //check for leaf category
        if(category.getChildren().size()>0){
            throw new CategoryHierarchyException("PRODUCT CAN ONLY BE ADDED IN LEAF CATEGORIES");
        }

        product.setCategory(category);
        product.setReturnable(addProductDTO.isReturnable());

        product.setSeller(seller);

        product.setDeleted(false);

        product.setActive(false);

        seller.addProducts(product);

        sellerRepository.save(seller);
//        productRepository.save(product);

        //send mail to admin regarding product activation with product data

    }

    private void validateProduct(AddProductDTO addProductDTO, Seller seller) {

        if(productRepository.existsByName(addProductDTO.getName())){

            //check seller already having same product
            seller.getProducts().forEach(product ->{
                if (product.getName().equals(addProductDTO.getName())) {
                    throw new CategoryHierarchyException("PRODUCT ALREADY EXIST FOR " + seller.getEmail());
                }
            });
        }


    }




    @Override
    public void addProductVariation(AddProductVariationDTO addProductVariationDTO, MultipartFile file) {


        Product savedProduct = productRepository
                .findById(addProductVariationDTO.getProductId())
                .orElseThrow(IdNotFoundException::new);

        validateMetaDataForProductVariation(addProductVariationDTO,savedProduct);

        ProductVariation productVariation = new ProductVariation();

        productVariation.setQuantityAvailable(addProductVariationDTO.getQuantityAvailable());

        productVariation.setPrice(addProductVariationDTO.getPrice());
        productVariation.setMetadata(addProductVariationDTO.getMetadata());
        productVariation.setActive(true);
        productVariation.setProduct(savedProduct);

        ProductVariation savedProductVariation = productVariationRepository.save(productVariation);

        saveProductImage(savedProductVariation.getId(),file);


    }

    private void validateMetaDataForProductVariation(AddProductVariationDTO addProductVariationDTO, Product savedProduct) {

        List<CategoryMetaDataFieldValue> categoryMetaDataFieldValueList =
                savedProduct.getCategory().getCategoryMetaDataFieldValueList();

        JSONObject metadata = addProductVariationDTO.getMetadata();

        Map<String, Set<String>> savedMetaData = new HashMap<>();

        categoryMetaDataFieldValueList.forEach(item -> {
            savedMetaData.put(item.getCategoryMetaDataField().getName(),convertStringValuesToSet(item.getValues()));
        });

        for (Object key : metadata.keySet()) {
            String keyStr = (String) key;
            String keyValue = (String) metadata.get(keyStr);

            logger.info("CHECKING FOR" + keyStr + "---" + keyValue);
            if (!(savedMetaData.containsKey(keyStr) && savedMetaData.get(keyStr).contains(keyValue))) {
                logger.info("NOT MATCHING");
                throw new CategoryHierarchyException("NO MATCHING FIELD FOUND FOR METADATA");
            }

        }
        logger.info("NO ISSUES FOUND");

    }

    private Set<String> convertStringValuesToSet(String values){

        String[] valueList = values.split(",");
        return new HashSet<>(Arrays.asList(valueList));
    }


    @Override
    public boolean saveProductImage(Long id, MultipartFile image) {
        try {
            String fileName = fileService.uploadImage(id, path, image);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
