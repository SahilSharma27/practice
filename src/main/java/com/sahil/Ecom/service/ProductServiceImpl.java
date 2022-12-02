package com.sahil.Ecom.service;

import com.sahil.Ecom.dto.product.AddProductDTO;
import com.sahil.Ecom.dto.product.FetchProductSellerDTO;
import com.sahil.Ecom.dto.product.variation.AddProductVariationDTO;
import com.sahil.Ecom.dto.product.variation.FetchProductVariationSellerDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                if (product.getName().equalsIgnoreCase(addProductDTO.getName())) {
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

        //check Metadata structure
        validateMetaDataForProductVariation(addProductVariationDTO,savedProduct);

        //check for same variation

        ProductVariation productVariation = new ProductVariation();

        productVariation.setQuantityAvailable(addProductVariationDTO.getQuantityAvailable());

        productVariation.setPrice(addProductVariationDTO.getPrice());

        productVariation.setMetadata(addProductVariationDTO.getMetadata());
        productVariation.setActive(true);
        productVariation.setProduct(savedProduct);


        checkForSameVariationAlreadyExist(savedProduct,productVariation);

        ProductVariation savedProductVariation = productVariationRepository.save(productVariation);

        saveProductImage(savedProductVariation.getId(),file);

    }

    private void checkForSameVariationAlreadyExist(Product savedProduct, ProductVariation newProductVariation) {
        savedProduct.getProductVariations().forEach(productVariation ->{
            logger.info(newProductVariation.getMetadata().toString());
            logger.info(productVariation.getMetadata().toString());
            if(newProductVariation.getMetadata().toString().equalsIgnoreCase(productVariation.getMetadata().toString())){
                throw new CategoryHierarchyException("PRODUCT VARIATION ALREADY EXIST");
            }
        } );
    }



    private void validateMetaDataForProductVariation(AddProductVariationDTO addProductVariationDTO, Product savedProduct) {

        List<CategoryMetaDataFieldValue> categoryMetaDataFieldValueList =
                savedProduct.getCategory().getCategoryMetaDataFieldValueList();

        JSONObject metadata = addProductVariationDTO.getMetadata();



        if(metadata.toString().equals("{}")){
            logger.info("CAUGHT EMPTY METADATA");
            throw new CategoryHierarchyException("NO MATCHING FIELD FOUND FOR METADATA");
        }

        Map<String, Set<String>> savedMetaData = new HashMap<>();

        categoryMetaDataFieldValueList.forEach(item -> {
            savedMetaData.put(item.getCategoryMetaDataField().getName(),convertStringValuesToSet(item.getValues()));
        });

        for (Object key : metadata.keySet()) {

            String keyStr = (String) key;
            String keyValue = (String) metadata.get(keyStr);
            keyStr = keyStr.toUpperCase();
            keyValue = keyValue.toUpperCase();

            logger.info("CHECKING FOR |" + keyStr + "---" + keyValue+"|");

            if (!(savedMetaData.containsKey(keyStr) && savedMetaData.get(keyStr).contains(keyValue))) {
                logger.info("NOT MATCHING");
//                logger.info(""+savedMetaData.containsKey(keyStr));
//                logger.info(""+savedMetaData.get(keyStr).contains(keyValue));
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


//    @Override
//    public List<FetchProductSellerDTO> getAllProducts(String sellerEmail) {
//
//        Seller seller =  sellerRepository.findByEmail(sellerEmail)
//                .orElseThrow(UserEmailNotFoundException::new);
//
//        return
//                seller.getProducts()
//                        .stream()
//                        .map(FetchProductSellerDTO::new)
//                        .collect(Collectors.toList());
//
//    }


    @Override
    public List<FetchProductSellerDTO> getAllProducts(String sellerEmail, int page, int size, String sort, String order) {

        Pageable pageable = null;

        if(order.equalsIgnoreCase("ASC")){
            pageable= PageRequest.of(page,size,Sort.by(sort).ascending());
        }
        else if(order.equalsIgnoreCase("DESC")) {
           pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }


        Seller seller = sellerRepository.findByEmail(sellerEmail)
                .orElseThrow(UserEmailNotFoundException::new);

        Page<Product> products = productRepository.findAllBySeller(seller,pageable);

        return
                products
                        .stream()
                        .map(FetchProductSellerDTO::new)
                        .collect(Collectors.toList());

    }

//    @Override
//    public List<FetchProductVariationSellerDTO> getAllProductVariations(String sellerEmail, Long productId) {
//
//        Seller seller =  sellerRepository.findByEmail(sellerEmail)
//                .orElseThrow(UserEmailNotFoundException::new);
//
//
//        Product productForSeller =seller.getProducts()
//                .stream()
//                .filter(product -> Objects.equals(product.getId(), productId))
//                .findFirst().orElseThrow(IdNotFoundException::new);
//
//
//        return productForSeller.getProductVariations()
//                .stream()
//                .map(FetchProductVariationSellerDTO::new)
//                .collect(Collectors.toList());
//
//    }

    @Override
    public List<FetchProductVariationSellerDTO> getAllProductVariations(String sellerEmail, Long productId,int page, int size, String sort, String order) {


        Pageable pageable = null;

        if(order.equalsIgnoreCase("ASC")){
            pageable= PageRequest.of(page,size,Sort.by(sort).ascending());
        }
        else if(order.equalsIgnoreCase("DESC")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }

        Seller seller =  sellerRepository.findByEmail(sellerEmail)
                .orElseThrow(UserEmailNotFoundException::new);

        Product productForSeller = seller.getProducts()
                .stream()
                .filter(product -> Objects.equals(product.getId(), productId))
                .findFirst().orElseThrow(IdNotFoundException::new);

        Page<ProductVariation> productVariationPage = productVariationRepository.findAllByProduct(productForSeller,pageable);

        return productVariationPage.getContent()
                .stream()
                .map(FetchProductVariationSellerDTO::new)
                .collect(Collectors.toList());

    }

    @Override
    public FetchProductSellerDTO getProduct(String sellerEmail, Long productId) {
        Seller seller = sellerRepository.findByEmail(sellerEmail)
                .orElseThrow(UserEmailNotFoundException::new);

        Product product = productRepository.findById(seller.getId()).orElseThrow(IdNotFoundException::new);

        return
                new FetchProductSellerDTO(product);

    }

    @Override
    public FetchProductVariationSellerDTO getProductVariation(String sellerEmail, Long productVarId) {
        Seller seller = sellerRepository.findByEmail(sellerEmail)
                .orElseThrow(UserEmailNotFoundException::new);

        ProductVariation productVariation = productVariationRepository
                .findById(productVarId)
                .orElseThrow(IdNotFoundException::new);


        return
                new FetchProductVariationSellerDTO(productVariation);

    }
}