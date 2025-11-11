package premium.product.service.Impl;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import premium.model.entity.product.Category;
import premium.product.mapper.CategoryMapper;
import premium.product.service.CategoryService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 查询一级分类
     * 启动Redis缓存
     */
    @Cacheable(value = "category", key = "'first'")
    @Override
    public List<Category> findFirstCategory() {
//        // search in redis
//        String categoryFirstJson = redisTemplate.opsForValue().get("category:first");
//        if (StringUtils.hasText(categoryFirstJson)) {
//            return JSON.parseArray(categoryFirstJson, Category.class);
//        }
        List<Category> categoryList = categoryMapper.findFirstCategory();
//        redisTemplate.opsForValue().set(
//                "category:first", JSON.toJSONString(categoryList),
//                7, TimeUnit.DAYS
//        );

        return categoryList;
    }

    // Redis Key: category::all
    @Cacheable(value = "category", key = "'all'")
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
