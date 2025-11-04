package premium.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import premium.model.entity.base.ProductUnit;

import java.util.List;

@Mapper
public interface ProductUnitMapper {
    List<ProductUnit> findAll();
}
