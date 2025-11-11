package premium.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import premium.model.entity.product.Brand;
import premium.model.vo.common.Result;
import premium.model.vo.common.ResultCodeEnum;
import premium.product.service.BrandService;

import java.util.List;

@RestController
@RequestMapping("/api/product/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 获取全部品牌
     */
    @GetMapping("/findAll")
    public Result<List<Brand>> findAll() {
        List<Brand> list = brandService.findAll();
        return Result.build(list, ResultCodeEnum.SUCCESS);
    }

}
