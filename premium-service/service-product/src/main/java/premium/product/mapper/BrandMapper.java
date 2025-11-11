package premium.product.mapper;

import org.apache.ibatis.annotations.Mapper;
import premium.model.entity.product.Brand;

import java.util.List;

@Mapper
public interface BrandMapper {
    List<Brand> findAll();
}
