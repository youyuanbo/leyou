package com.leyou.service;

import com.leyou.client.BrandClient;
import com.leyou.client.CategoryClient;
import com.leyou.client.GoodsClient;
import com.leyou.client.SpecificationClient;
import com.leyou.item.pojo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PageService {

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private TemplateEngine templateEngine;

    public Map<String, Object> loadModel(Long spuId) {
        // 查询spu,spu包含skus、skuDetail
        Spu spu = goodsClient.querySpuById(spuId);

        List<Sku> skus = spu.getSkuList();

        SpuDetail detail = spu.getSpuDetail();

        // 查询brand
        Brand brand = brandClient.queryBrandById(spu.getBrandId());

        // 查询商品分类
        List<Category> categories = categoryClient.queryCategoryListByCategoryIdList(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));

        // 查询规格参数
        List<SpecGroup> specs = specificationClient.querySpecGroupAndSpecParamByCategoryId(spu.getCid3());

        Map<String, Object> attributes = new HashMap<>();

        attributes.put("spu", spu);
        // attributes.put("subTitle", spu.getSubTitle());
        attributes.put("skus", skus);
        attributes.put("spuDetail", detail);
        attributes.put("brand", brand);
        attributes.put("categories", categories);
        attributes.put("specs", specs);

        return attributes;
    }

    public void createHtml(Long spuId){

        // 上下文
        Context context = new Context();
        context.setVariables(loadModel(spuId));

        // 输出流
        File file = new File("G:\\", spuId + ".html");

        if (file.exists()){
            file.delete();
        }

        try {
            PrintWriter printWriter = new PrintWriter(file, "UTF-8");
            templateEngine.process( "item", context, printWriter);
        } catch (FileNotFoundException e) {
            log.error("生成静态页面异常", e);
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void deleteHtml(Long spuId) {
        // 输出流
        File file = new File("G:\\", spuId + ".html");
        if (file.exists()){
            file.delete();
        }
    }
}
