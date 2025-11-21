package premium.product.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import premium.model.dto.h5.ProductSkuDto;
import premium.model.dto.product.SkuSaleDto;
import premium.model.entity.product.Product;
import premium.model.entity.product.ProductDetails;
import premium.model.entity.product.ProductSku;
import premium.model.vo.h5.ProductItemVo;
import premium.product.mapper.ProductDetailsMapper;
import premium.product.mapper.ProductMapper;
import premium.product.mapper.ProductSkuMapper;
import premium.product.service.ProductService;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private ProductDetailsMapper productDetailsMapper;

    @Override
    public List<ProductSku> findProductSkuBySale() {
        return productSkuMapper.findProductSkuBySaleSale();
    }

    @Override
    public PageInfo<ProductSku> findByPage(Integer page, Integer limit, ProductSkuDto productSkuDto) {
        PageHelper.startPage(page, limit);
        List<ProductSku> list = productSkuMapper.findByPage(productSkuDto);
        return new PageInfo<>(list);
    }

    @Override
    public ProductItemVo item(Long skuId) {
        ProductSku productSku = productSkuMapper.getBySkuId(skuId);
        Product product = productMapper.getByProductId(productSku.getProductId());
        ProductDetails productDetails = productDetailsMapper.getByProductId(productSku.getProductId());

        // build sku map
        Map<String, Object> skuSpecValueMap = new HashMap<>();
        List<ProductSku> productSkuList = productSkuMapper.findByProductId(productSku.getProductId());
        productSkuList.forEach(item -> {
            skuSpecValueMap.put(item.getSkuSpec(), item.getId());
        });

        // build image list
        String[] imageUrls = productDetails.getImageUrls().split(",");
        List<String> imageUrlList = Arrays.asList(imageUrls);

        // build slider url list
        String[] slidersUrls = product.getSliderUrls().split(",");
        List<String> slidersUrlList = Arrays.asList(slidersUrls);

        // build spec value list
        String specValues = product.getSpecValue();
        JSONArray specValueList = JSON.parseArray(specValues);

        // build vo
        ProductItemVo productItemVo = new ProductItemVo();
        productItemVo.setProduct(product);
        productItemVo.setProductSku(productSku);
        productItemVo.setSkuSpecValueMap(skuSpecValueMap);
        productItemVo.setDetailsImageUrlList(imageUrlList);
        productItemVo.setSliderUrlList(slidersUrlList);
        productItemVo.setSpecValueList(specValueList);

        return productItemVo;
    }

    @Override
    public ProductSku getBySkuId(Long skuId) {
        return productSkuMapper.getBySkuId(skuId);
    }

    // Todo 解决库存超卖问题
    @Override
    public Boolean updateSkuSaleAndStockNum(List<SkuSaleDto> skuSaleDtoList) {
        if (!CollectionUtils.isEmpty(skuSaleDtoList)) {
            for (SkuSaleDto skuSaleDto : skuSaleDtoList) {
                productSkuMapper.updateSkuSaleAndStockNum(skuSaleDto);
            }
        }
        return true;
    }
}
