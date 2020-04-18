package com.hxy.recipe.lambda;

import com.hxy.model.Point;
import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExceptionInLambda {

    public static void main(String[] args) {
        List<Point> pointList = new ArrayList<>();
        pointList.add(null);
        try {
            pointList.stream().map(p -> p.getX()).forEach(i -> {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        Utils.sleepInMinutes(5L);
    }

}
