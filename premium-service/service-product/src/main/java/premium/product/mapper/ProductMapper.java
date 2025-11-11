package premium.product.mapper;

import org.apache.ibatis.annotations.Mapper;
import premium.model.entity.product.Product;

@Mapper
public interface ProductMapper {
    Product getByProductId(Long productId);
}
