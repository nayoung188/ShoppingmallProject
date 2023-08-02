package test.shoppingmall;


import com.shoppingmall.repository.ProdImageRepository;
import com.shoppingmall.repository.ProductRepository;
import com.shoppingmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ProductTest {
    @Autowired
    ProductService productService;

    @Autowired
    ProdImageRepository prodImageRepository;

    @Autowired
    ProductRepository productRepository;

//    @Test
//    @DisplayName("상품등록 테스트")
//    public void insertProductTest(){
//        ProductDTO productDTO = new ProductDTO((ProductEntity) p);
//        productDTO.setProductName("테스트이름");
//        productDTO.setProductCode("테스트-1");
//        productDTO.setProductBrand("테스트 브랜드");
//        productDTO.setManufacturer("테스트 제조사");
//        productDTO.setOriginalPrice(10000);
//        productDTO.setSalePrice(2500);
//        productDTO.setProductState("판매중");
//        productDTO.setProductStock(1);
//        productDTO.setProductContent("테스트 상세설명");
//
//        // Act
//        ProductEntity productEntity = ProductEntity.toProductEntity(productDTO);
//        productRepository.save(productEntity);
//
//        // Retrieve the saved product from the database
//        ProductEntity savedProduct = productRepository.findById(productEntity.getProductNo()).orElse(null);
//
//        // Assert
//        Assertions.assertNotNull(savedProduct);
//        Assertions.assertEquals(productEntity.getProductName(), savedProduct.getProductName());
//        Assertions.assertEquals(productEntity.getProductCode(), savedProduct.getProductCode());
//        Assertions.assertEquals(productEntity.getProductBrand(), savedProduct.getProductBrand());
//        Assertions.assertEquals(productEntity.getManufacturer(), savedProduct.getManufacturer());
//        Assertions.assertEquals(productEntity.getOriginalPrice(), savedProduct.getOriginalPrice());
//        Assertions.assertEquals(productEntity.getSalePrice(), savedProduct.getSalePrice());
//        Assertions.assertEquals(productEntity.getProductState(), savedProduct.getProductState());
//        Assertions.assertEquals(productEntity.getProductStock(), savedProduct.getProductStock());
//        Assertions.assertEquals(productEntity.getProductContent(), savedProduct.getProductContent());
//    }
}
