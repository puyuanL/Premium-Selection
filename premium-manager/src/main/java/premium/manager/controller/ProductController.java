package premium.manager.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import premium.common.log.annotation.Log;
import premium.manager.service.ProductService;
import premium.model.dto.product.ProductDto;
import premium.model.entity.product.Product;
import premium.model.vo.common.Result;
import premium.model.vo.common.ResultCodeEnum;

@RestController
@RequestMapping("/admin/product/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 商品列表
     */
    @GetMapping("/{page}/{limit}")
    public Result<PageInfo<Product>> findByPage(@PathVariable Integer page,
                                                @PathVariable Integer limit,
                                                ProductDto productDto) {
        PageInfo<Product> pageInfo = productService.findByPage(page, limit, productDto);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    /**
     * 商品添加
     */
    @Log(title = "商品管理-添加", businessType = 1)
    @PostMapping("/save")
    public Result save(@RequestBody Product product) {
        productService.save(product);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    /**
     * 根据商品id查询商品信息
     */
    @GetMapping("/getById/{id}")
    public Result<Product> getById(@PathVariable Long id) {
        Product product = productService.getById(id);
        return Result.build(product, ResultCodeEnum.SUCCESS);
    }

    /**
     * 保存修改的数据
     */
    @Log(title = "商品管理-修改", businessType = 2)
    @PutMapping("/updateById")
    public Result update(@RequestBody Product product) {
        productService.update(product);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    /**
     * 商品删除
     */
    @Log(title = "商品管理-删除", businessType = 3)
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Long id) {
        productService.deleteById(id);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    /**
     * 商品审核
     */
    @Log(title = "商品管理-审核", businessType = 0)
    @GetMapping("/updateAuditStatus/{id}/{auditStatus}")
    public Result updateAuditStatus(@PathVariable Long id, @PathVariable Integer auditStatus) {
        productService.updateAuditStatus(id, auditStatus);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }

    /**
     * 商品上下架
     */
    @Log(title = "商品管理-上下架", businessType = 0)
    @GetMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        productService.updateStatus(id, status);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }
}
