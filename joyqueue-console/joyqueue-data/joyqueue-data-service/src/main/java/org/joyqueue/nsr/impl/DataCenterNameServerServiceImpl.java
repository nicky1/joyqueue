/**
 * Copyright 2019 The JoyQueue Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.joyqueue.nsr.impl;

import com.alibaba.fastjson.JSON;
import org.joyqueue.convert.NsrDataCenterConverter;
import org.joyqueue.model.domain.DataCenter;
import org.joyqueue.model.domain.OperLog;
import org.joyqueue.nsr.DataCenterNameServerService;
import org.joyqueue.nsr.NameServerBase;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by wangxiaofei1 on 2019/1/2.
 */
@Service("dataCenterNameServerService")
public class DataCenterNameServerServiceImpl extends NameServerBase implements DataCenterNameServerService {

    public static final String ADD_DATACENTER="/datacenter/add";
    public static final String UPDATE_DATACENTER="/datacenter/update";
    public static final String REMOVE_DATACENTER="/datacenter/remove";
    public static final String GETBYID_DATACENTER="/datacenter/getById";
    private static final String LIST_DATACENTER="/datacenter/list";

    private NsrDataCenterConverter nsrDataCenterConverter = new NsrDataCenterConverter();

    /**
     * datacenter
     * @return
     * @throws Exception
     */
    @Override
    public List<DataCenter> findAllDataCenter() throws Exception {
        String result = post(LIST_DATACENTER, null);
        List<org.joyqueue.domain.DataCenter> dataCenterList = JSON.parseArray(result).toJavaList(org.joyqueue.domain.DataCenter.class);
        return dataCenterList.stream().map(dataCenter -> nsrDataCenterConverter.revert(dataCenter)).collect(Collectors.toList());
    }

    @Override
    public int add(DataCenter dataCenter) throws Exception {
        org.joyqueue.domain.DataCenter nsrDataCenter = nsrDataCenterConverter.convert(dataCenter);
        String result = postWithLog(ADD_DATACENTER, nsrDataCenter,OperLog.Type.DATA_CENTER.value(),OperLog.OperType.ADD.value(),nsrDataCenter.getId());
        return isSuccess(result);
    }

    @Override
    public int update(DataCenter dataCenter) throws Exception {
        org.joyqueue.domain.DataCenter nsrDataCenter = nsrDataCenterConverter.convert(dataCenter);
        String result1 =  postWithLog(UPDATE_DATACENTER, nsrDataCenter,OperLog.Type.DATA_CENTER.value(),OperLog.OperType.UPDATE.value(),nsrDataCenter.getId());
        return isSuccess(result1);
    }

    @Override
    public int delete(DataCenter dataCenter) throws Exception {
        org.joyqueue.domain.DataCenter nsrConfig = nsrDataCenterConverter.convert(dataCenter);
        String result = postWithLog(REMOVE_DATACENTER, nsrConfig,OperLog.Type.DATA_CENTER.value(),OperLog.OperType.DELETE.value(),nsrConfig.getId());
        return isSuccess(result);
    }

    @Override
    public DataCenter findById(String id) throws Exception {
        String result = post(GETBYID_DATACENTER,id);
        org.joyqueue.domain.DataCenter nsrDataCenter = JSON.parseObject(result, org.joyqueue.domain.DataCenter.class);
        return nsrDataCenterConverter.revert(nsrDataCenter);
    }
}
