package premium.feign.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import premium.model.dto.product.SkuSaleDto;
import premium.model.entity.product.ProductSku;

import java.util.List;

@FeignClient(value = "service-product")
public interface ProductFeignClient {

    @GetMapping("/api/product/getBySkuId/{skuId}")
    ProductSku getBySkuId(@PathVariable("skuId") Long skuId);

    /**
     * 更新商品sku销量和库存
     */
    @PostMapping("/api/product/updateSkuSaleNum")
    Boolean updateSkuSaleAndStockNum(@RequestBody List<SkuSaleDto> skuSaleDtoList);

}
