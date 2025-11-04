package premium.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import premium.model.dto.product.ProductDto;
import premium.model.entity.product.Product;

import java.util.List;

@Mapper
public interface ProductMapper {
    List<Product> findByPage(ProductDto productDto);

    void save(Product product);

    Product findProductById(Long id);

    void updateById(Product product);

    void deleteById(Long id);
}
