package com.leyou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class SearchService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private ElasticsearchRepository repository;

    @Autowired
    private ElasticsearchTemplate template;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Goods buildGoods(Spu spu) throws IOException {

        // 创建goods对象
        Goods goods = new Goods();

        // 查询品牌
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());

        // 查询分类名称
        List<String> names = this.categoryClient.queryCategoryNameByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));

        // 查询spu下的所有sku
        List<Sku> skus = this.goodsClient.querySkuListBySpuId(spu.getId());
        Set<Long> prices = new HashSet<>();
        List<Map<String, Object>> skuMapList = new ArrayList<>();
        // 遍历skus，获取价格集合
        skus.forEach(sku -> {
            prices.add(sku.getPrice());
            Map<String, Object> skuMap = new HashMap<>();
            skuMap.put("id", sku.getId());
            skuMap.put("title", sku.getTitle());
            skuMap.put("price", sku.getPrice());
            skuMap.put("image", StringUtils.isNotBlank(sku.getImages()) ? StringUtils.split(sku.getImages(), ",")[0] : "");
            skuMapList.add(skuMap);
        });

        // 查询出所有的搜索规格参数
        List<SpecParam> params = this.specificationClient.querySpecParamsByGroupId(null, spu.getCid3(), null, true);
        // 查询spuDetail。获取规格参数值
        SpuDetail spuDetail = this.goodsClient.querySpuDetailById(spu.getId());
        // 获取通用的规格参数
        Map<Long, Object> genericSpecMap = MAPPER.readValue(spuDetail.getGenericSpec(), new TypeReference<Map<Long, Object>>() {
        });
        // 获取特殊的规格参数
        Map<Long, List<Object>> specialSpecMap = MAPPER.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<Object>>>() {
        });
        // 定义map接收{规格参数名，规格参数值}
        Map<String, Object> paramMap = new HashMap<>();
        params.forEach(param -> {
            // 判断是否通用规格参数
            if (param.getGeneric()) {
                // 获取通用规格参数值
                String value = genericSpecMap.get(param.getId()).toString();
                // 判断是否是数值类型
                if (param.getNumeric()) {
                    // 如果是数值的话，判断该数值落在那个区间
                    value = chooseSegment(value, param);
                }
                // 把参数名和值放入结果集中
                paramMap.put(param.getName(), value);
            } else {
                paramMap.put(param.getName(), specialSpecMap.get(param.getId()));
            }
        });

        // 设置参数
        goods.setId(spu.getId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setBrandId(spu.getBrandId());
        goods.setCreateTime(spu.getCreateTime());
        goods.setSubTitle(spu.getSubTitle());
        goods.setAll(spu.getTitle() + brand.getName() + StringUtils.join(names, " "));
        goods.setPrice(prices);
        goods.setSkus(MAPPER.writeValueAsString(skuMapList));
        goods.setSpecs(paramMap);

        return goods;
    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    public PageResult<Goods> search(SearchRequest request) {

        int page = request.getPage() - 1;
        int size = request.getSize();
        // 创建查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 结果过滤
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "subTitle", "skus"}, null));
        // 分页
        queryBuilder.withPageable(PageRequest.of(page, size));
        // 过滤
        QueryBuilder basicQuery = buildBasicQuery(request);
        queryBuilder.withQuery(basicQuery);
        // 聚合分类
        String categoryAggName = "category_agg_name";
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        // 聚合品牌
        String brandAggName = "brand_agg_name";
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));
        // 查询
        // Page result = repository.search(queryBuilder.build());
        AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);
        // 解析结果
        //分页结果
        long total = result.getTotalElements();
        int totalPage = result.getTotalPages();
        List<Goods> goodsList = result.getContent();
        //聚合结果
        Aggregations aggregations = result.getAggregations();
        //从聚合结果中获取分类
        List<Category> categoryList = parseCategoryAgg(aggregations.get(categoryAggName));
        //从聚合结果中获取品牌
        List<Brand> brandList = parseBrandAgg(aggregations.get(brandAggName));

        // 完成规格参数聚合
        List<Map<String, Object>> specs = null;

        if (categoryList != null && categoryList.size() == 1) {
            // 商品分类数量为1，表明当前只有一类商品
            specs = buildSpecificationAgg(categoryList.get(0).getId(), basicQuery);
        }

        return new SearchResult(total, totalPage, goodsList, categoryList, brandList, specs);

    }

    private QueryBuilder buildBasicQuery(SearchRequest request) {
        //创建布尔查询
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // 查询条件
        queryBuilder.must(QueryBuilders.matchQuery("all", request.getKey()));
        Map<String, String> map = request.getFilter();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();

            if (!"cid3".equals(key) && !"brandId".equals(key)) {
                key = "specs." + key + ".keyword";
            }

            String value = entry.getValue();
            queryBuilder.filter(QueryBuilders.termQuery(key, value));
        }
        return queryBuilder;
    }

    private List<Map<String, Object>> buildSpecificationAgg(Long cid, QueryBuilder basicQuery) {

        List<Map<String, Object>> specs = new ArrayList<>();

        // 查询需要聚合的规格参数
        List<SpecParam> specParams = specificationClient.querySpecParamsByGroupId(null, cid, null, true);
        // 聚合
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 带上查询条件
        queryBuilder.withQuery(basicQuery);
        specParams.forEach(specParam -> {
            String name = specParam.getName();
            queryBuilder.addAggregation(AggregationBuilders.terms(name).field("specs." + name + ".keyword"));
        });
        // 获取结果
        AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);
        // 解析结果
        Aggregations aggregations = result.getAggregations();
        specParams.forEach(specParam -> {
            // 规格参数名称
            String name = specParam.getName();
            StringTerms terms = aggregations.get(name);
            // 获取桶中的key
            List<String> options = terms.getBuckets().stream().map(StringTerms.Bucket::getKeyAsString).collect(Collectors.toList());

            // 准备map
            Map<String, Object> map = new HashMap<>();
            map.put("K", name);
            map.put("options", options);
            specs.add(map);
        });

        return specs;
    }

    private List<Category> parseCategoryAgg(LongTerms longTerms) {
        List<Long> ids = longTerms.getBuckets().stream().map(bucket -> bucket.getKeyAsNumber().longValue()).collect(Collectors.toList());
        return categoryClient.queryCategoryListByCategoryIdList(ids);
    }

    private List<Brand> parseBrandAgg(LongTerms longTerms) {
        List<Long> ids = longTerms.getBuckets().stream().map(bucket -> bucket.getKeyAsNumber().longValue()).collect(Collectors.toList());
        return brandClient.queryBrandListByBidList(ids);
    }

    public void createOrUpdateIndex(Long spuId) {
        Spu spu = goodsClient.querySpuById(spuId);
        //构建goods
        Goods goods = null;
        try {
            goods = this.buildGoods(spu);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 存入索引库
        repository.save(goods);
    }

    public void deleteIndex(Long spuId) {
        repository.deleteById(spuId);
    }
}



