package premium.manager.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import premium.manager.mapper.CategoryMapper;
import premium.manager.service.CategoryService;
import premium.model.entity.product.Category;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> findcategoryList(int id) {
        // 根据 id 查询分类
        List<Category> categoryList = categoryMapper.selectCategoryByParentId(id);
        // 遍历返回的list集合。有下层 hasChildren = true
        if (categoryList != null) {
            categoryList.forEach(category -> {
                // 判断每个分类是否有下层分类, 并设置 setChildren
                category.setHasChildren(categoryMapper.selectCountByParentId(category.getId()) > 0);
            });
        }

        return categoryList;
    }
}
