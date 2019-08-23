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
package io.chubao.joyqueue.handler.routing.command.topic;

import com.jd.laf.binding.annotation.Value;
import com.jd.laf.web.vertx.annotation.Body;
import com.jd.laf.web.vertx.annotation.Path;
import com.jd.laf.web.vertx.response.Response;
import com.jd.laf.web.vertx.response.Responses;
import io.chubao.joyqueue.handler.annotation.PageQuery;
import io.chubao.joyqueue.handler.error.ConfigException;
import io.chubao.joyqueue.handler.routing.command.NsrCommandSupport;
import io.chubao.joyqueue.model.PageResult;
import io.chubao.joyqueue.model.Pagination;
import io.chubao.joyqueue.model.QPageQuery;
import io.chubao.joyqueue.model.domain.Broker;
import io.chubao.joyqueue.model.domain.PartitionGroupReplica;
import io.chubao.joyqueue.model.domain.TopicPartitionGroup;
import io.chubao.joyqueue.model.query.QBroker;
import io.chubao.joyqueue.model.query.QPartitionGroupReplica;
import io.chubao.joyqueue.service.BrokerService;
import io.chubao.joyqueue.service.PartitionGroupReplicaService;
import io.chubao.joyqueue.service.TopicPartitionGroupService;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 主题队列-Broker分组 处理器
 * Created by wylixiaobin on 2018-10-19
 */
public class PartitionGroupReplicaCommand extends NsrCommandSupport<PartitionGroupReplica, PartitionGroupReplicaService, QPartitionGroupReplica> {
    @Value(nullable = false)
    private BrokerService brokerService;
    @Value(nullable = false)
    private TopicPartitionGroupService topicPartitionGroupService;

    @Path("search")
    public Response pageQuery(@PageQuery QPageQuery<QPartitionGroupReplica> qPageQuery) throws Exception {
        List<Broker> brokers = Collections.emptyList();
        QPartitionGroupReplica query = qPageQuery.getQuery();

        if (query.getTopic() != null) {
            TopicPartitionGroup partitionGroup = topicPartitionGroupService.findByTopicAndGroup(query.getTopic().getNamespace().getCode(), query.getTopic().getCode(), query.getGroupNo());
            if (partitionGroup != null) {
                for (Integer brokerId : partitionGroup.getReplicas()) {
                    Broker broker = brokerService.findById(brokerId);
                    if (broker != null) {
                        brokers.add(broker);
                    }
                }
            }
        }

        Pagination pagination = qPageQuery.getPagination();
        pagination.setTotalRecord(brokers.size());

        PageResult<Broker> result = new PageResult();
        result.setPagination(pagination);
        result.setResult(brokers);
        return Responses.success(result.getPagination(), result.getResult());
    }

    @Path("searchBrokerToScale")
    public Response toScaleSearch(@PageQuery QPageQuery<QPartitionGroupReplica> qPageQuery) throws Exception {
        QPartitionGroupReplica query = qPageQuery.getQuery();
        List<PartitionGroupReplica> partitionGroupReplicas = service.getByTopic(query.getTopic().getCode(), query.getNamespace().getCode());

        QPageQuery<QBroker> brokerQuery = new QPageQuery(qPageQuery.getPagination(), new QBroker());
        brokerQuery.getQuery().setKeyword(qPageQuery.getQuery().getKeyword());
        PageResult<Broker> brokerPage = brokerService.search(brokerQuery);

        if (CollectionUtils.isNotEmpty(partitionGroupReplicas) && CollectionUtils.isNotEmpty(brokerPage.getResult())) {
            List<Broker> brokers = new ArrayList<>();
            for (Broker broker : brokerPage.getResult()) {
                boolean isMatch = false;
                for (PartitionGroupReplica partitionGroupReplica : partitionGroupReplicas) {
                    if (partitionGroupReplica.getBrokerId() == broker.getId()) {
                        isMatch = true;
                        break;
                    }
                }
                if (!isMatch) {
                    brokers.add(broker);
                }
            }
        }

        return Responses.success(brokerPage.getPagination(), brokerPage.getResult());
    }

    @Path("searchBrokerToAddNew")
    public Response toAddNewPartitionGroupSearch(@PageQuery QPageQuery<QPartitionGroupReplica> qPageQuery) throws Exception {
        QPageQuery<QBroker> brokerQuery = new QPageQuery(qPageQuery.getPagination(), new QBroker());
        PageResult<Broker> brokerPage = brokerService.search(brokerQuery);
        return Responses.success(brokerPage.getPagination(), brokerPage.getResult());
    }

    @Override
    @Path("add")
    public Response add(@Body PartitionGroupReplica model) throws Exception {
        TopicPartitionGroup group = topicPartitionGroupService.findByTopicAndGroup(model.getNamespace().getCode(),
                model.getTopic().getCode(),model.getGroupNo());
        if(group.getElectType().equals(TopicPartitionGroup.ElectType.raft.type())) {
            model.setRole(PartitionGroupReplica.ROLE_DYNAMIC);
        }
        else model.setRole(PartitionGroupReplica.ROLE_SLAVE);
        int count = service.addWithNameservice(model,group);
        if (count <= 0) {
            throw new ConfigException(addErrorCode());
        }
        return Responses.success(model);
    }

    @Path("delete")
    public Response delete(@Body PartitionGroupReplica partitionGroupReplica) throws Exception {
        PartitionGroupReplica replica = service.findById(partitionGroupReplica.getId());
        int count = service.removeWithNameservice(replica,topicPartitionGroupService.findByTopicAndGroup(
                replica.getNamespace().getCode(),replica.getTopic().getCode(),replica.getGroupNo()));
        if (count <= 0) {
            throw new ConfigException(deleteErrorCode());
        }
        return Responses.success(replica);
    }

    @Path("leader")
    public Response leaderChange(@Body PartitionGroupReplica model) throws Exception {
        TopicPartitionGroup topicPartitionGroup = new TopicPartitionGroup();
        topicPartitionGroup.setTopic(model.getTopic());
        topicPartitionGroup.setNamespace(model.getNamespace());
        topicPartitionGroup.setLeader(model.getBrokerId());
        topicPartitionGroup.setGroupNo(model.getGroupNo());
        topicPartitionGroup.setOutSyncReplicas(model.getOutSyncReplicas());
        int count = topicPartitionGroupService.leaderChange(topicPartitionGroup);
        if (count<=0) {
            throw new ConfigException(updateErrorCode());
        }
        return Responses.success();
    }
}
