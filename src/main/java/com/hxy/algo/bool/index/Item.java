package com.hxy.algo.bool.index;

import java.util.List;

/**
 * 业务对象，包含一些检索条件
 */
public interface Item {

    String id();

    // clause之间是&关系
    List<Clause> clauseList();

}
