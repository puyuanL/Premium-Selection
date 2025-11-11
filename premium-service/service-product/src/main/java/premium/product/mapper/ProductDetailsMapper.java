package premium.product.mapper;

import org.apache.ibatis.annotations.Mapper;
import premium.model.entity.product.ProductDetails;

@Mapper
public interface ProductDetailsMapper {
    ProductDetails getByProductId(Long productId);
}
