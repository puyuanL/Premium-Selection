package premium.manager.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import premium.common.log.annotation.Log;
import premium.common.log.enums.OperatorType;
import premium.manager.service.BrandService;
import premium.model.entity.product.Brand;
import premium.model.vo.common.Result;
import premium.model.vo.common.ResultCodeEnum;

import java.util.List;

@RestController
@RequestMapping("/admin/product/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 品牌列表
     */
    @GetMapping("/{page}/{limit}")
    public Result list(@PathVariable Integer page, @PathVariable Integer limit) {
        PageInfo<Brand> pageInfo = brandService.findByPage(page, limit);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    /**
     * 品牌添加
     */
    @Log(title = "品牌管理-添加", businessType = 1)
    @PostMapping("/save")
    public Result save(@RequestBody Brand brand) {
        brandService.save(brand);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    /**
     * 品牌修改
     */
    @Log(title = "品牌管理-修改", businessType = 2)
    @PutMapping("/updateById")
    public Result updateById(@RequestBody Brand brand) {
        brandService.updateById(brand);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    /**
     * 品牌删除
     */
    @Log(title = "品牌管理-删除", businessType = 3)
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Long id) {
        brandService.deleteById(id);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    /**
     * 所有品牌
     */
    @GetMapping("/findAll")
    public Result findAll() {
        List<Brand> list = brandService.findAll();
        return Result.build(list, ResultCodeEnum.SUCCESS);
    }
}
