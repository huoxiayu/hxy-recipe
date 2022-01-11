package com.hxy.recipe.gc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * -server
 * -Xverify:none
 * -XX:+AlwaysPreTouch
 * -XX:ParallelGCThreads=4
 * -XX:ConcGCThreads=2
 * -XX:MaxMetaspaceSize=512m
 * -XX:MetaspaceSize=128m
 * -Xms2048m
 * -Xmx2048m
 * -Xlog:gc*:file=/Users/hxy/log/gc-%t.log:time,level,tags,tid::filecount=1,filesize=64m
 * -XX:ReservedCodeCacheSize=512m
 * -XX:+UseG1GC
 * -XX:SoftRefLRUPolicyMSPerMB=50
 * -XX:CICompilerCount=4
 * -XX:+HeapDumpOnOutOfMemoryError
 * -XX:-OmitStackTraceInFastThrow
 * -ea
 * -Dsun.io.useCanonCaches=false
 * -Djdk.http.auth.tunneling.disabledSchemes=""
 * -Djdk.attach.allowAttachSelf=true
 * -Djdk.module.illegalAccess.silent=true
 * -Dkotlinx.coroutines.debug=off
 * -XX:ErrorFile=/Users/hxy/log/java_error_in_idea_%p.log
 * -XX:HeapDumpPath=/Users/hxy/log/java_error_in_idea.hprof
 */
public class GCStart {

    public static void main(String[] args) {
        List<String> strList = new ArrayList<>();
        int times = 100000;
        while (times-- > 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(50L);
            } catch (InterruptedException ignore) {

            }

            int size = ThreadLocalRandom.current().nextInt(1000);
            if (size < 100) {
                strList.clear();
            }

            for (int i = 0; i < size; i++) {
                strList.add(UUID.randomUUID().toString());
            }

            if (size < 10) {
                System.out.println(strList.size());
            }
        }
    }

}
