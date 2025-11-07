package premium.manager.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import premium.manager.mapper.CategoryMapper;
import premium.manager.mapper.ProductDetailsMapper;
import premium.manager.mapper.ProductMapper;
import premium.manager.mapper.ProductSkuMapper;
import premium.manager.service.ProductService;
import premium.model.dto.product.ProductDto;
import premium.model.entity.product.Product;
import premium.model.entity.product.ProductDetails;
import premium.model.entity.product.ProductSku;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private ProductDetailsMapper productDetailsMapper;

    @Override
    public PageInfo<Product> findByPage(Integer page, Integer limit, ProductDto productDto) {
        PageHelper.startPage(page, limit);
        List<Product> list = productMapper.findByPage(productDto);
        return new PageInfo<>(list);
    }

    @Transactional
    @Override
    public void save(Product product) {
        // 保存基本信息
        product.setStatus(0);
        product.setAuditStatus(0);
        productMapper.save(product);

        // 获取商品sku列表组合，保存sku信息      product_sku
        List<ProductSku> productSkuList = product.getProductSkuList();
        for (int i = 0; i < productSkuList.size(); i++) {
            ProductSku productSku = productSkuList.get(i);

            productSku.setSkuCode(product.getId() + "_" + i);
            productSku.setSkuName(product.getName() + productSku.getSkuSpec());
            productSku.setProductId(product.getId());
            productSku.setSaleNum(0);
            productSku.setStatus(0);

            productSkuMapper.save(productSku);
        }


        // 保存商品详情数据                    product_details
        ProductDetails productDetails = new ProductDetails();
        productDetails.setProductId(product.getId());
        productDetails.setImageUrls(product.getDetailsImageUrls());
        productDetailsMapper.save(productDetails);
    }

    @Override
    public Product getById(Long id) {
        // 根据id查询商品详细信息
        Product product = productMapper.findProductById(id);
        // 根据商品id查询商品SKU信息
        List<ProductSku> productSkuList = productSkuMapper.findProductSkuById(id);
        product.setProductSkuList(productSkuList);
        // 根据id查询商品详情数据
        ProductDetails productDetails = productDetailsMapper.findDetailsById(id);
        product.setDetailsImageUrls(productDetails.getImageUrls());
        return product;
    }

    @Transactional
    @Override
    public void update(Product product) {
        // update product
        productMapper.updateById(product);
        // update product_sku
        List<ProductSku> productSkuList = product.getProductSkuList();
        for (ProductSku productSku : productSkuList) {
            productSkuMapper.updateById(productSku);
        }
        // update product_details
        String detailsImageUrls = product.getDetailsImageUrls();
        ProductDetails productDetails = productDetailsMapper.findDetailsById(product.getId());
        productDetails.setImageUrls(detailsImageUrls);
        productDetailsMapper.updateById(productDetails);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        productMapper.deleteById(id);
        productSkuMapper.deleteByProductId(id);
        productDetailsMapper.deleteByProductId(id);
    }

    @Override
    public void updateAuditStatus(Long id, Integer auditStatus) {
        Product product = new Product();
        product.setId(id);
        if(auditStatus == 1) {
            product.setAuditStatus(1);
            product.setAuditMessage("审批通过");
        } else {
            product.setAuditStatus(-1);
            product.setAuditMessage("审批不通过");
        }
        productMapper.updateById(product);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Product product = new Product();
        product.setId(id);
        if(status == 1) {
            product.setStatus(1);
        } else {
            product.setStatus(-1);
        }
        productMapper.updateById(product);
    }
}
