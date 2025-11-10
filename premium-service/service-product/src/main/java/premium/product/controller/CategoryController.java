package premium.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import premium.model.entity.product.Category;
import premium.model.vo.common.Result;
import premium.model.vo.common.ResultCodeEnum;
import premium.product.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/product/category")
@CrossOrigin    // 跨域
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 查询所有类别数据，按属性返回
     */
    @GetMapping("findCategoryTree")
    public Result<List<Category>> findCategoryTree() {
        List<Category> categoryList = categoryService.findCategoryTree();
        return Result.build(categoryList, ResultCodeEnum.SUCCESS);
    }
}
