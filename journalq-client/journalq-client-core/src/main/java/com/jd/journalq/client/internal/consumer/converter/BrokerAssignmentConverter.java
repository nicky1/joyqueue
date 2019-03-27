package com.jd.journalq.client.internal.consumer.converter;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jd.journalq.client.internal.consumer.coordinator.domain.BrokerAssignment;
import com.jd.journalq.client.internal.consumer.coordinator.domain.BrokerAssignments;
import com.jd.journalq.client.internal.consumer.coordinator.domain.PartitionAssignment;
import com.jd.journalq.client.internal.metadata.domain.PartitionMetadata;
import com.jd.journalq.client.internal.metadata.domain.TopicMetadata;
import com.jd.journalq.network.domain.BrokerNode;

import java.util.List;
import java.util.Map;

/**
 * BrokerAssignmentConverter
 * author: gaohaoxiang
 * email: gaohaoxiang@jd.com
 * date: 2018/12/17
 */
public class BrokerAssignmentConverter {

    public static BrokerAssignments convertBrokerAssignments(TopicMetadata topicMetadata) {
        BrokerAssignments result = new BrokerAssignments();
        Map<BrokerNode, List<Short>> brokerPartitions = Maps.newHashMap();

        for (PartitionMetadata partitionMetadata : topicMetadata.getPartitions()) {
            if (partitionMetadata.getLeader() == null) {
                continue;
            }
            List<Short> brokerPartitionList = brokerPartitions.get(partitionMetadata.getLeader());
            if (brokerPartitionList == null) {
                brokerPartitionList = Lists.newLinkedList();
                brokerPartitions.put(partitionMetadata.getLeader(), brokerPartitionList);
            }
            brokerPartitionList.add(partitionMetadata.getId());
        }

        List<BrokerAssignment> assignments = Lists.newArrayListWithCapacity(brokerPartitions.size());
        for (Map.Entry<BrokerNode, List<Short>> brokerEntry : brokerPartitions.entrySet()) {
            assignments.add(new BrokerAssignment(brokerEntry.getKey(), new PartitionAssignment(brokerEntry.getValue())));
        }

        result.setAssignments(assignments);
        return result;
    }

    public static BrokerAssignments convertTopicrAssignments(TopicMetadata topicMetadata) {
        List<BrokerAssignment> assignments = Lists.newArrayListWithCapacity(topicMetadata.getPartitions().size());
        for (PartitionMetadata partitionMetadata : topicMetadata.getPartitions()) {
            if (partitionMetadata.getLeader() == null) {
                continue;
            }
            assignments.add(new BrokerAssignment(partitionMetadata.getLeader(),
                    new PartitionAssignment(Lists.newArrayList(partitionMetadata.getId()))));
        }
        BrokerAssignments brokerAssignments = new BrokerAssignments();
        brokerAssignments.setAssignments(assignments);
        return brokerAssignments;
    }
}