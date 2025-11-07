package premium.manager.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import premium.common.log.annotation.Log;
import premium.manager.service.CategoryBrandService;
import premium.model.dto.product.CategoryBrandDto;
import premium.model.entity.product.Brand;
import premium.model.entity.product.CategoryBrand;
import premium.model.vo.common.Result;
import premium.model.vo.common.ResultCodeEnum;

import java.util.List;

@RestController
@RequestMapping("/admin/product/categoryBrand")
public class CategoryBrandController {

    @Autowired
    private CategoryBrandService categoryBrandService;

    /**
     * 分类品牌的条件分页查询
     */
    @GetMapping("/{page}/{limit}")
    public Result<PageInfo<CategoryBrand>> findByPage(@PathVariable Integer page,
                                                      @PathVariable Integer limit,
                                                      CategoryBrandDto categoryBrandDto) {
        PageInfo<CategoryBrand> pageInfo = categoryBrandService.findByPage(page, limit, categoryBrandDto);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    /**
     * 分类品牌管理 保存功能
     */
    @Log(title = "品牌分类管理-添加", businessType = 1)
    @PostMapping("/save")
    public Result save(@RequestBody CategoryBrandDto categoryBrandDto) {
        categoryBrandService.save(categoryBrandDto);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    /**
     * 分类品牌管理 修改
     */
    @Log(title = "品牌分类管理-添加", businessType = 2)
    @PutMapping("/updateById")
    public Result updateById(@RequestBody CategoryBrand categoryBrand) {
        categoryBrandService.updateById(categoryBrand);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    /**
     * 分类品牌管理 删除
     */
    @Log(title = "品牌分类管理-添加", businessType = 3)
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Long id) {
        categoryBrandService.deleteById(id);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    /**
     * 根据分类id查询对应的品牌数据
     */
    @GetMapping("/findBrandByCategoryId/{categoryId}")
    public Result findBrandByCategoryId(@PathVariable Long categoryId) {
        List<Brand> list = categoryBrandService.findBrandByCategoryId(categoryId);
        return Result.build(list, ResultCodeEnum.SUCCESS);
    }

}
