package premium.product.service;

import com.github.pagehelper.PageInfo;
import premium.model.dto.h5.ProductSkuDto;
import premium.model.entity.product.ProductSku;
import premium.model.vo.h5.ProductItemVo;

import java.util.List;

public interface ProductService {
    List<ProductSku> findProductSkuBySale();

    PageInfo<ProductSku> findByPage(Integer page, Integer limit, ProductSkuDto productSkuDto);

    ProductItemVo item(Long skuId);
}
