package com.hxy.distribution.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

@Slf4j
public class ZooKeeperStart {

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        String zkPath = "/services/app";

        // receive event: WatchedEvent state:SyncConnected type:None path:null
        Watcher watcher = watchEvent -> log.info("receive event: {}", watchEvent);

        ZooKeeper zooKeeper = new ZooKeeper("localhost:2181", 10_000, watcher);

        String path = zooKeeper.create(
            zkPath,
            "Simple App".getBytes(),
            ZooDefs.Ids.OPEN_ACL_UNSAFE,
            CreateMode.EPHEMERAL
        );
        log.info("create path: {}", path);

        byte[] data = zooKeeper.getData(zkPath, false, null);
        log.info("get data: {}", new String(data));

        zooKeeper.close();
        log.info("close");
    }

}
