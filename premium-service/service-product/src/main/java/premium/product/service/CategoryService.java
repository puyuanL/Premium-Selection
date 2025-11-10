package premium.product.service;

import premium.model.entity.product.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findFirstCategory();

    List<Category> findCategoryTree();
}
