package premium.product.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import premium.model.entity.product.Category;
import premium.product.mapper.CategoryMapper;
import premium.product.service.CategoryService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> findFirstCategory() {
        return categoryMapper.findFirstCategory();
    }

    @Override
    public List<Category> findCategoryTree() {
        List<Category> allCategoryList = categoryMapper.findAll();
        Map<Long, List<Category>> categoryMap = new HashMap<>();
        for (Category category : allCategoryList) {
            Long parentId = category.getParentId();
            if (!categoryMap.containsKey(parentId)) {
                categoryMap.put(parentId, new ArrayList<>());
            }
            categoryMap.get(parentId).add(category);
        }
        return buildTree(categoryMap, 0L);
    }

    /**
     * build category tree
     */
    public List<Category> buildTree(Map<Long, List<Category>> categoryMap, Long parentId) {
        if (!categoryMap.containsKey(parentId)) {
            return null;
        }
        List<Category> currentCategoryLevel = categoryMap.get(parentId);
        for (Category category : currentCategoryLevel) {
            category.setChildren(buildTree(categoryMap, category.getId()));
        }
        return currentCategoryLevel;
    }
}
