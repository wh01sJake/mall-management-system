package com.intelijake.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.intelijake.mall.mapper.ProductMapper;
import com.intelijake.mall.pojo.query.ProductQuery;
import com.intelijake.mall.pojo.vo.ProductVO;
import com.intelijake.mall.service.IProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.intelijake.pojo.Product;
import com.intelijake.pojo.Product;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jake
 * @since 2025-06-11
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private RedisTemplate<String,Object> template;

    @Autowired
    RedissonClient redissonClient;


    @Override
    public IPage<ProductVO> list(ProductQuery productQuery) {

        IPage<ProductVO> page = new Page<>(productQuery.getPage(), productQuery.getLimit());

        productMapper.list(page,productQuery);
        return page;
    }

    @Cacheable(value = "productCache",key = "#id",sync = true)
    @Override
    public Product selectById(Integer id) {
        return productMapper.selectById(id);
    }


    @CacheEvict(value = "productCache",key = "#product.id")
    @Override
    public void update(Product product) {

        productMapper.updateById(product);
    }

/*    @Override
    public Product selectById(Integer id) {

        Product product = (Product) template.opsForValue().get("product:" + id);

        if (ObjectUtils.isEmpty(product)){

            redissonClient.getLock("product_lock_"+id).lock();

            try {
                product = (Product) template.opsForValue().get("product:" + id);

                if (ObjectUtils.isEmpty(product)){
                    product = productMapper.selectById(id);
                    //if existed in db
                    if (!ObjectUtils.isEmpty(product)){

                        template.opsForValue().set("product",product);
                    }
                    // null product, cache null value with an expire date
                    else {
                        template.opsForValue().set("product",new Product(),1, TimeUnit.MINUTES);
                    }
                }
            }
            finally {
                redissonClient.getLock("product_lock_"+id).unlock();
            }
        }
        return product;
    }*/

/*    @Override
    public Product selectById(Integer id) {

        Product product = (Product) template.opsForValue().get("product:" + id);

        if (ObjectUtils.isEmpty(product)){

            synchronized (this){
                product = (Product) template.opsForValue().get("product:" + id);

                if (ObjectUtils.isEmpty(product)){
                    product = productMapper.selectById(id);
                    //if existed in db
                    if (!ObjectUtils.isEmpty(product)){

                        template.opsForValue().set("product",product);
                    }
                    // null product, cache null value with an expire date
                    else {
                        template.opsForValue().set("product",new Product(),1, TimeUnit.MINUTES);
                    }
                }
            }
        }
        return product;
    }*/
}
