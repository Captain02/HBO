package io.renren.modules.banner.service;

import io.renren.common.entity.PageData;

import java.util.List;

public interface BannerService {
    List<PageData> getBanner(PageData pageData) throws Exception;
}
