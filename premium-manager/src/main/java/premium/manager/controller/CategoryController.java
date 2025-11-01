package premium.manager.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

    /**
     * Excel Category 导出: 文件导出 + 文件下载
     */
    @GetMapping("/exportData")
    public void exportData(HttpServletResponse response) {
        categoryService.exportData(response);
    }

    /**
     * Excel Category 导入
     */
    @PostMapping("/importData")
    public Result importMapping(@RequestParam("file") MultipartFile file) {
        categoryService.importData(file);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
}
