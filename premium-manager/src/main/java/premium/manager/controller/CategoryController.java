package premium.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import premium.manager.service.CategoryService;
import premium.model.entity.product.Category;
import premium.model.vo.common.Result;
import premium.model.vo.common.ResultCodeEnum;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/product/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分类列表方法，每次查询一层数据
     */
    @GetMapping("/findCategoryList/{id}")
    public Result findCategoryList(@PathVariable int id) {
        List<Category> categoryList = categoryService.findcategoryList(id);
        return Result.build(categoryList, ResultCodeEnum.SUCCESS);
    }
}
