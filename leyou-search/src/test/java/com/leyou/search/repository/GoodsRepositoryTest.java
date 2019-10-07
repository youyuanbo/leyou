package com.leyou.search.repository;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Spu;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.service.SearchService;
import com.leyou.vo.SpuVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GoodsRepositoryTest {

    @Autowired
    private GoodsRepository repository;

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SearchService searchService;

    @Test
    public void CreateGoodsIndexTest() {
        // templates.createIndex(Goods.class);
        // templates.putMapping(Goods.class);
        // templates.deleteIndex(Goods.class);
    }

    @Test
    public void loadDataTest() {

        //    query spu
        int page = 1;
        int rows = 100;
        int size = 100;

        while (size == 100) {
            PageResult<SpuVo> spuVoPageResult = goodsClient.querySpuByPage(null, true, page, rows);
            List<SpuVo> spuVoList = spuVoPageResult.getItems();
            List<Goods> goodsList = new ArrayList<>();
            spuVoList.forEach(spuVo -> {

                Spu spu = new Spu();
                BeanUtils.copyProperties(spuVo, spu);
                Goods goods = null;
                try {
                    goods = searchService.buildGoods(spu);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                goodsList.add(goods);

            });

            repository.saveAll(goodsList);
            page++;
            size = spuVoList.size();
        }
    }

}