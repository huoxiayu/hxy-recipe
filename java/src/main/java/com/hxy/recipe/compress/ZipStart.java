package com.hxy.recipe.compress;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ZipUtil;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.hxy.recipe.util.RandomUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ZipStart {

    @AllArgsConstructor
    private static class JsonBean {
        private long id;
        private String name;
        private List<String> content;
    }

    public static void main(String[] args) {
        List<String> contents = new ArrayList<>();
        for (int rand : RandomUtil.randomIntArray(100_0000)) {
            contents.add(String.valueOf(rand));
        }

        String longString = new Gson().toJson(new JsonBean(1L, "json-bean", contents));
        byte[] bytes = longString.getBytes();
        log.info("ori length {}", bytes.length);

        byte[] gzipEncode = ZipUtil.gzip(bytes);
        log.info("gzip encode length {}, compress rate {}", gzipEncode.length, gzipEncode.length * 100 / bytes.length);

        byte[] zlibEncode = ZipUtil.zlib(bytes, 5);
        log.info("zlib encode length {}, compress rate {}", zlibEncode.length, zlibEncode.length * 100 / bytes.length);

        Preconditions.checkArgument(ArrayUtil.equals(bytes, ZipUtil.unGzip(gzipEncode)));
        Preconditions.checkArgument(ArrayUtil.equals(bytes, ZipUtil.unZlib(zlibEncode)));
    }

}
