package com.wheel.rpc.notify.impl.zookeeper;

import org.I0Itec.zkclient.IZkDataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.wheel.rpc.core.model.ServiceGovernanceModel;

/**
 * 结点数据变化的watcher
 * 
 * 1:"服务治理"相关参数的变化
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月4日 上午11:18:19
 */
public class ZkNodeDataChangeWatcher implements IZkDataListener {
    
    public static final Logger LOG = LoggerFactory.getLogger(ZkNodeDataChangeWatcher.class);
    
    private ZookeeperNotify notify;
    
    public ZkNodeDataChangeWatcher(ZookeeperNotify notifyArgs) {
        this.notify = notifyArgs;
    }
    
    @Override
    public void handleDataChange(String dataPath, Object data) throws Exception {
        ServiceGovernanceModel serviceGovernanceModel = JSON.parseObject(String.valueOf(data), ServiceGovernanceModel.class);
        notify.serviceParamUpdate(notify.getServiceName(), serviceGovernanceModel);
    }

    @Override
    public void handleDataDeleted(String dataPath) throws Exception {
        LOG.info("Method : {}, dataPath : {}", "handleDataDeleted", dataPath);
    }
    
}
