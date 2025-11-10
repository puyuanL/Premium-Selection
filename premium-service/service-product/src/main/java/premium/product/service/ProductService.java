package premium.product.service;

import premium.model.entity.product.ProductSku;

import java.util.List;

public interface ProductService {
    List<ProductSku> findProductSkuBySale();
}
