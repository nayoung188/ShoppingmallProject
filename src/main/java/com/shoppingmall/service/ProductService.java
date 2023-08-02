package com.shoppingmall.service;

import com.shoppingmall.dto.ProductDTO;
import com.shoppingmall.entity.CategoryEntity;
import com.shoppingmall.entity.MemberEntity;
import com.shoppingmall.entity.ProdImageEntity;
import com.shoppingmall.entity.ProductEntity;
import com.shoppingmall.repository.CategoryRepository;
import com.shoppingmall.repository.ProductRepository;
import com.shoppingmall.spec.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    private final ProdImageService prodImageService;

    public Page<ProductEntity> getAllProduct(Pageable pageable){
        return productRepository.findAll(pageable);
    }

    public List<ProductEntity> getProductByNo(Long productNo){
        return productRepository.findByProductNo(productNo);
    }
    public List<ProductEntity> getProductByNoLong(Long[] productNo){
        List<Long> productList = Arrays.asList(productNo);
        return productRepository.findAllById(productList);
    }

    public Page<ProductEntity> searchList(Pageable pageable, String searchKeyword, String productName, String productCode, String productBrand, String manufacturer, List<String> productState){
        //Specification<ProductEntity> spec = (root, query, criteriaBuilder) -> null;
        Specification<ProductEntity> spec = Specification.where(ProductSpecification.containsSearchKeyword(searchKeyword));

        if(productName != null){
            spec = spec.and(ProductSpecification.equalsProductName(productName));
        }
        if(productCode != null) {
            spec = spec.and(ProductSpecification.equalsProductCode(productCode));
        }
        if(productBrand != null) {
            spec = spec.and(ProductSpecification.equalsProductBrand(productBrand));
        }
        if(manufacturer != null) {
            spec = spec.and(ProductSpecification.equalsManufacturer(manufacturer));
        }
        if(productState != null) {
            spec = spec.and(ProductSpecification.equalsCheckedProductState(productState));
        }

        return productRepository.findAll(spec, pageable);
    }

    public Long countByState(String productState){
        Specification<ProductEntity> specification = ProductSpecification.equalsProductState(productState);
        return productRepository.count(specification);
    }

    public Long getProductCountByConditions(String searchKeyword, List<String> productState, String productName, String productCode,
                                            String productBrand, String manufacturer) {
        Specification<ProductEntity> specification = Specification.where(null);

        if (!StringUtils.isEmpty(searchKeyword)) {
            specification = specification.and(ProductSpecification.containsSearchKeyword(searchKeyword));
        }
        if (!StringUtils.isEmpty(productState)) {
            specification = specification.and(ProductSpecification.equalsCheckedProductState(productState));
        }
        if (!StringUtils.isEmpty(productName)) {
            specification = specification.and(ProductSpecification.equalsProductName(productName));
        }
        if (!StringUtils.isEmpty(productCode)) {
            specification = specification.and(ProductSpecification.equalsProductCode(productCode));
        }
        if (!StringUtils.isEmpty(productBrand)) {
            specification = specification.and(ProductSpecification.equalsProductBrand(productBrand));
        }
        if (!StringUtils.isEmpty(manufacturer)) {
            specification = specification.and(ProductSpecification.equalsManufacturer(manufacturer));
        }

        return productRepository.count(specification);
    }


    public void saveProduct(ProductDTO productDTO, MultipartFile mainImage, List<MultipartFile> subImages, Long memberId) throws Exception{
        ProductEntity productEntity = ProductEntity.toProductEntity(productDTO);

        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberNo(memberId);
        productEntity.setMemberEntity(memberEntity);

        if( mainImage != null && !mainImage.isEmpty()){
            ProdImageEntity mainImageEntity = new ProdImageEntity();
            prodImageService.saveProdImage(mainImageEntity, mainImage);
            productEntity.addProdImage(mainImageEntity);
        }
        for(MultipartFile subImage : subImages){
            if(subImage != null && !subImage.isEmpty()){
                ProdImageEntity subImageEntity = new ProdImageEntity();
                prodImageService.saveProdImage(subImageEntity, subImage);
                productEntity.addProdImage(subImageEntity);
            }
        }
        productRepository.save(productEntity);
    }

    public void modifyProduct(Long productNo, ProductDTO productDTO, MultipartFile mainImage, List<MultipartFile> subImages) throws Exception{
        ProductEntity productEntity = productRepository.findById(productNo).orElse(null);
        if(productEntity != null) {
            CategoryEntity categoryEntity = categoryRepository.findById(productDTO.getCategoryId()).orElse(null);
            if(categoryEntity != null){
                ProductEntity updateProductEntity = ProductEntity.toProductEntity(productDTO);
                updateProductEntity.setProductNo(productDTO.getProductNo());
                updateProductEntity.setProdImageEntities(productEntity.getProdImageEntities());
                if(mainImage != null && !mainImage.isEmpty()){
                    ProdImageEntity mainImageEntity = new ProdImageEntity();
                    prodImageService.saveProdImage(mainImageEntity, mainImage);
                    updateProductEntity.addProdImage(mainImageEntity);
                }
                for(MultipartFile subImage : subImages) {
                    if(subImage != null && !subImage.isEmpty()){
                        ProdImageEntity subImageEntity = new ProdImageEntity();
                        prodImageService.saveProdImage(subImageEntity, subImage);
                        updateProductEntity.addProdImage(subImageEntity);
                    }
                }
                productRepository.save(updateProductEntity);
            }
        }
    }

    public void deleteProduct(Long[] productNo){
        List<Long> productList = Arrays.asList(productNo);
        productRepository.deleteAllById(productList);
    }

    public void stateUpdate(Long[] productNo, String productState){
        List<Long> productList = Arrays.asList(productNo);
        List<ProductEntity> products = productRepository.findAllById(productList);
        for(ProductEntity product : products){
            product.setProductState(productState);
        }
        productRepository.saveAll(products);
    }


}
