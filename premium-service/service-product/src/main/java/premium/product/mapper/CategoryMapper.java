package premium.product.mapper;

import org.apache.ibatis.annotations.Mapper;
import premium.model.entity.product.Category;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> findFirstCategory();

    List<Category> findAll();
}
