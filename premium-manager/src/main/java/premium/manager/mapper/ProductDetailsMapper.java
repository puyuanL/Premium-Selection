package premium.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import premium.model.entity.product.ProductDetails;

@Mapper
public interface ProductDetailsMapper {

    void save(ProductDetails productDetails);

    ProductDetails findDetailsById(Long id);

    void updateById(ProductDetails productDetails);

    void deleteById(Long id);

    void deleteByProductId(Long id);
}
