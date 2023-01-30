package com.hxy.recipe.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class PartitionUtil {

    public static void main(String[] args) {
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        int max = 5;
        for (int i = 1; i <= max; i++) {
            System.out.println("split -> " + i);

            System.out.println("Lists.partition -> " + Lists.partition(list, i));

            System.out.println("My.partition -> " + myPartition(list, i));

            System.out.println("<------------>");
        }
    }

    public static <T> List<List<T>> myPartition(List<T> list, int partitions) {
        int size = CollectionUtils.size(list);
        Preconditions.checkState(size >= partitions);
        List<List<T>> listOfList = new ArrayList<>();
        for (int i = 0; i < partitions; i++) {
            listOfList.add(new ArrayList<>());
        }
        for (int i = 0; i < list.size(); i++) {
            listOfList.get(i % partitions).add(list.get(i));
        }
        return listOfList;
    }

}
