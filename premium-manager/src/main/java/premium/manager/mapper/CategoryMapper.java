package premium.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import premium.model.entity.product.Category;
import premium.model.vo.product.CategoryExcelVo;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> selectCategoryByParentId(int id);

    int selectCountByParentId(Long id);

    List<Category> findAll();

    void batchInsert(List<CategoryExcelVo> categoryList);
}
