package premium.manager.service;

import com.github.pagehelper.PageInfo;
import premium.model.dto.product.CategoryBrandDto;
import premium.model.entity.product.CategoryBrand;

public interface CategoryBrandService {
    PageInfo<CategoryBrand> findByPage(Integer page, Integer limit,CategoryBrandDto categoryBrandDto);

    void save(CategoryBrandDto categoryBrandDto);

    void updateById(CategoryBrand categoryBrand);

    void deleteById(Long id);
}
