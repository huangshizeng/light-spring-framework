package com.huang.springframework.mvc.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: hsz
 * @date: 2021/3/25 10:59
 * @description:
 */

@Data
@AllArgsConstructor
public class RequestMappingInfo {

    private String url;

    private String requestMethod;
}
