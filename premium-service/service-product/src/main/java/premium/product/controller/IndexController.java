package premium.product.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import premium.model.vo.common.Result;
import premium.model.vo.common.ResultCodeEnum;
import premium.model.vo.h5.IndexVo;
import premium.product.service.CategoryService;
import premium.product.service.ProductService;

@Tag(name = "首页接口管理")
@RestController
@RequestMapping("/api/product/index")
public class IndexController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 一级分类数据
     */
    @GetMapping
    public Result<IndexVo> findData() {
        IndexVo indexVo = new IndexVo();
        indexVo.setCategoryList(categoryService.findFirstCategory());
        indexVo.setProductSkuList(productService.findProductSkuBySale());
        return Result.build(indexVo, ResultCodeEnum.SUCCESS);
    }

}
