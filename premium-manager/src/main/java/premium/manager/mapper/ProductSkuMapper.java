package premium.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import premium.model.entity.product.ProductSku;

import java.util.List;

@Mapper
public interface ProductSkuMapper {
    void save(ProductSku productSku);

    List<ProductSku> findProductSkuById(Long id);

    void updateById(ProductSku productSku);

    void deleteByProductId(Long id);
}
