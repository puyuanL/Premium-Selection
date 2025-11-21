package premium.product.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import premium.model.dto.h5.ProductSkuDto;
import premium.model.dto.product.SkuSaleDto;
import premium.model.entity.product.ProductSku;
import premium.model.vo.common.Result;
import premium.model.vo.common.ResultCodeEnum;
import premium.model.vo.h5.ProductItemVo;
import premium.product.service.ProductService;

import java.util.List;

@RestController
@RequestMapping(value="/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 商品的条件分页查询
     */
    @GetMapping(value = "/{page}/{limit}")
    public Result<PageInfo<ProductSku>> findByPage(@PathVariable Integer page,
                                                   @PathVariable Integer limit,
                                                   ProductSkuDto productSkuDto) {
        PageInfo<ProductSku> pageInfo = productService.findByPage(page, limit, productSkuDto);
        return Result.build(pageInfo , ResultCodeEnum.SUCCESS) ;
    }

    /**
     * 商品详情
     */
    @GetMapping(value = "/item/{skuId}")
    public Result<ProductItemVo> item(@PathVariable Long skuId) {
        ProductItemVo productItemVo = productService.item(skuId);
        return Result.build(productItemVo, ResultCodeEnum.SUCCESS);
    }

    /**
     * 远程调用，根据skuId返回sku信息
     */
    @GetMapping("/getBySkuId/{skuId}")
    public ProductSku getBuSkuId(@PathVariable Long skuId) {
        return productService.getBySkuId(skuId);
    }

    /**
     * 远程调用，根据订单信息更新商品销量和库存
     */
    @PostMapping("/updateSkuSaleNum")
    public Boolean updateSkuSaleAndStockNum(@RequestBody List<SkuSaleDto> skuSaleDtoList) {
        return productService.updateSkuSaleAndStockNum(skuSaleDtoList);
    }
}
