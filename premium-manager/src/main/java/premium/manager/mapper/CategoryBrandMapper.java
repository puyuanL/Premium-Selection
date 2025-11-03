package premium.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import premium.model.dto.product.CategoryBrandDto;
import premium.model.entity.product.CategoryBrand;

import java.util.List;

@Mapper
public interface CategoryBrandMapper {
    List<CategoryBrand> findByPage(CategoryBrandDto categoryBrandDto);

    void save(CategoryBrandDto categoryBrandDto);

    void updateById(CategoryBrand categoryBrand);

    void deleteById(Long id);
}
