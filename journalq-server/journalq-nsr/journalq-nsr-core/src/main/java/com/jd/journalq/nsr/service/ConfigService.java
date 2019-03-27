package com.jd.journalq.nsr.service;

import com.jd.journalq.domain.Config;
import com.jd.journalq.nsr.model.ConfigQuery;


/**
 * @author wylixiaobin
 * Date: 2018/9/4
 */
public interface ConfigService extends DataService<Config, ConfigQuery, String> {

    /**
     * 根据分组和Key获取配置
     *
     * @param group
     * @param key
     * @return
     */
    Config getByGroupAndKey(String group, String key);

    /**
     * 添加配置
     *
     * @param config
     */
    void add(Config config);

    /**
     * 更新配置
     *
     * @param config
     */

    void update(Config config);

    /**
     * 删除配置
     *
     * @param config
     */
    void remove(Config config);
}