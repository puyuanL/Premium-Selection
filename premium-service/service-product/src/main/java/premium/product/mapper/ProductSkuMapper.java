package premium.product.mapper;

import org.apache.ibatis.annotations.Mapper;
import premium.model.entity.product.ProductSku;

import java.util.List;

@Mapper
public interface ProductSkuMapper {
    List<ProductSku> findProductSkuBySaleSale();
}
