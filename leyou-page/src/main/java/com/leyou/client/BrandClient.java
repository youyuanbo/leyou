package com.leyou.client;

import com.leyou.api.BrandAPI;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "item-service")
public interface BrandClient extends BrandAPI {
}
