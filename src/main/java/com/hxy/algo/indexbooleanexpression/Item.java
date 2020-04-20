package com.hxy.algo.indexbooleanexpression;

import java.util.List;

/**
 * 业务对象，可能包含一些检索条件
 */
public interface Item {

    List<Clause> clauseList();

}
