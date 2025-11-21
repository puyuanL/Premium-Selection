package premium.product.mapper;

import org.apache.ibatis.annotations.Mapper;
import premium.model.dto.h5.ProductSkuDto;
import premium.model.dto.product.SkuSaleDto;
import premium.model.entity.product.ProductSku;

import java.util.List;

@Mapper
public interface ProductSkuMapper {
    List<ProductSku> findProductSkuBySaleSale();

    List<ProductSku> findByPage(ProductSkuDto productSkuDto);

    ProductSku getBySkuId(Long skuId);

    List<ProductSku> findByProductId(Long productId);

    void updateSkuSaleAndStockNum(SkuSaleDto skuSaleDto);
}
