package com.leyou.client;

import com.leyou.api.SpecificationAPI;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "item-service")
public interface SpecificationClient extends SpecificationAPI {
}
