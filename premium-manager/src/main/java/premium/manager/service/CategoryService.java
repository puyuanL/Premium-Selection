package premium.manager.service;

import premium.model.entity.product.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findcategoryList(int id);
}
