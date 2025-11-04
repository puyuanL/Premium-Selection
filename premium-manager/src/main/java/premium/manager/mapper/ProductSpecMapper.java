package premium.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import premium.model.entity.product.ProductSpec;

import java.util.List;

@Mapper
public interface ProductSpecMapper {
    List<ProductSpec> findByPage();

    void save(ProductSpec productSpec);

    void updateById(ProductSpec productSpec);

    void deleteById(Long id);

    List<ProductSpec> findAll();
}
