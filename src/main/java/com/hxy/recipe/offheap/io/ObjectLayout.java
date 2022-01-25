package com.hxy.recipe.offheap.io;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.JsonUtil;
import com.hxy.recipe.util.RunnableUtil;
import com.hxy.recipe.util.Utils;
import jdk.internal.misc.Unsafe;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.util.RamUsageEstimator;
import org.springframework.util.Assert;
import sun.nio.ch.DirectBuffer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * IDEA Perferences -> Compiler -> Java Compiler add below line:
 * --add-opens=java.base/jdk.internal.misc=ALL-UNNAMED
 */
@Slf4j
public class ObjectLayout {

    private static final int SIZE = 1000_0000;
    private static final int OBJ_SIZE = 33;
    private static final boolean GET_STRING = false;
    private static final int LOOP_TIMES = 100;

    @SneakyThrows
    public static void main(String[] args) {
        Bean bean = new Bean();
        int outerInt = 100;
        bean.setOuterInt(outerInt);
        long outerLong = 200L;
        bean.setOuterLong(outerLong);
        boolean outerBool = true;
        bean.setOuterBool(outerBool);
        double outerDouble = 300D;
        bean.setOuterDouble(outerDouble);
        String outerString = "outer-string";
        log.info("outerString.getBytes().length -> {}", outerString.getBytes().length);
        bean.setOuterString(outerString);
        InnerBean innerBean = new InnerBean();
        bean.setInnerBean(innerBean);
        int innerInt = 10000;
        innerBean.setInnerInt(innerInt);
        String innerString = "inner-string";
        log.info("innerString.getBytes().length -> {}", innerString.getBytes().length);
        innerBean.setInnerString(innerString);
        log.info("bean -> {}", JsonUtil.toJson(bean));

        List<Bean> beanList = IntStream.range(0, SIZE)
                .mapToObj(i -> bean)
                .collect(Collectors.toList());
        log.info(RamUsageEstimator.humanSizeOf(beanList));

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        int size = beanList.size();
        int stringBaseIdx = OBJ_SIZE * size;
        int stringIdx = 0;
        for (Bean b : beanList) {
            dos.writeInt(b.getOuterInt()); // 4
            dos.writeLong(b.getOuterLong()); // 8
            dos.writeBoolean(b.isOuterBool()); // 1
            dos.writeDouble(b.getOuterDouble()); // 8
            dos.writeInt(b.getInnerBean().getInnerInt()); // 4

            // outer string
            dos.writeInt(stringBaseIdx + stringIdx); // 4
            stringIdx += (4 + b.getOuterString().getBytes().length);

            // inner string
            dos.writeInt(stringBaseIdx + stringIdx); // 4
            stringIdx += (4 + b.getInnerBean().getInnerString().getBytes().length);
        }

        for (Bean b : beanList) {
            // outer string
            byte[] outerStringBytes = b.getOuterString().getBytes();
            dos.writeInt(outerStringBytes.length);
            dos.write(outerStringBytes);

            // inner string
            byte[] innerStringBytes = b.getInnerBean().getInnerString().getBytes();
            dos.writeInt(innerStringBytes.length);
            dos.write(innerStringBytes);
        }

        byte[] bytes = bos.toByteArray();
        log.info("bytes.length -> {}", bytes.length);

        String fileName = "./binary_list";
        File file = new File(fileName);
        boolean delete = file.delete();
        log.info("delete -> {}", delete);

        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.write(bytes);
        raf.getChannel().force(true);
        raf.close();

        log.info("begin");

        RandomAccessFile read = new RandomAccessFile(file, "r");
        long length = file.length();
        log.info("file length -> {}", length);

        MappedByteBuffer mbb = read.getChannel().map(
                FileChannel.MapMode.READ_ONLY, 0, length
        );
        log.info("pos -> {}, limit -> {}, cap -> {}", mbb.position(), mbb.limit(), mbb.capacity());

        long normalAccess = BenchmarkUtil.singleRun(RunnableUtil.loopRunnable(() -> {
            for (int i = 0; i < SIZE; i++) {
                Bean b = beanList.get(i);
                Assert.isTrue(outerInt == b.getOuterInt(), "eq");
                Assert.isTrue(outerLong == b.getOuterLong(), "eq");
                Assert.isTrue(outerBool == b.isOuterBool(), "eq");
                Assert.isTrue(outerDouble == b.getOuterDouble(), "eq");
                Assert.isTrue(outerString.equals(b.getOuterString()), "eq");
                Assert.isTrue(innerInt == b.getInnerBean().getInnerInt(), "eq");
                Assert.isTrue(innerString.equals(b.getInnerBean().getInnerString()), "eq");
            }
        }, LOOP_TIMES));
        log.info("normalAccess -> {}", normalAccess);

        long mmapAccess = BenchmarkUtil.singleRun(RunnableUtil.loopRunnable(() -> {
            for (int i = 0; i < SIZE; i++) {
                int outerInt_ = getOuterInt(mbb, i);
                Assert.isTrue(outerInt == outerInt_, "eq");

                long outerLong_ = getOuterLong(mbb, i);
                Assert.isTrue(outerLong == outerLong_, "eq");

                boolean outerBool_ = getOuterBool(mbb, i);
                Assert.isTrue(outerBool == outerBool_, "eq");

                double outerDouble_ = getOuterDouble(mbb, i);
                Assert.isTrue(outerDouble == outerDouble_, "eq");

                int innerInt_ = getInnerInt(mbb, i);
                Assert.isTrue(innerInt == innerInt_, "eq");

                if (GET_STRING) {
                    String outerString_ = getOuterString(mbb, i);
                    Assert.isTrue(outerString.equals(outerString_), "eq");

                    String innerString_ = getInnerString(mbb, i);
                    Assert.isTrue(innerString.equals(innerString_), "eq");
                }
            }
        }, LOOP_TIMES));
        log.info("mmapAccess -> {}", mmapAccess);

        DirectBuffer dbb = (DirectBuffer) mbb;
        long address = dbb.address();
        log.info("address -> {}", address);

        Unsafe unsafe = Utils.getUnsafe();
        long unsafeAccess = BenchmarkUtil.singleRun(RunnableUtil.loopRunnable(() -> {
            for (int i = 0; i < SIZE; i++) {
                int outerInt_ = getOuterInt(unsafe, address, i);
                Assert.isTrue(outerInt == outerInt_, "eq");

                long outerLong_ = getOuterLong(unsafe, address, i);
                Assert.isTrue(outerLong == outerLong_, "eq");

                boolean outerBool_ = getOuterBool(unsafe, address, i);
                Assert.isTrue(outerBool == outerBool_, "eq");

                double outerDouble_ = getOuterDouble(unsafe, address, i);
                Assert.isTrue(outerDouble == outerDouble_, "eq");

                int innerInt_ = getInnerInt(unsafe, address, i);
                Assert.isTrue(innerInt == innerInt_, "eq");

                if (GET_STRING) {
                    String outerString_ = getOuterString(unsafe, address, i);
                    Assert.isTrue(outerString.equals(outerString_), "eq");

                    String innerString_ = getInnerString(unsafe, address, i);
                    Assert.isTrue(innerString.equals(innerString_), "eq");
                }
            }
        }, LOOP_TIMES));
        log.info("unsafeAccess -> {}", unsafeAccess);
    }

    private static int getOuterInt(MappedByteBuffer mbb, int index) {
        return mbb.getInt(index * OBJ_SIZE);
    }

    private static long getOuterLong(MappedByteBuffer mbb, int index) {

        return mbb.getLong(index * OBJ_SIZE + 4);
    }

    private static boolean getOuterBool(MappedByteBuffer mbb, int index) {
        return mbb.get(index * OBJ_SIZE + 12) != 0;
    }

    private static double getOuterDouble(MappedByteBuffer mbb, int index) {
        return mbb.getDouble(index * OBJ_SIZE + 13);
    }

    private static int getInnerInt(MappedByteBuffer mbb, int index) {
        return mbb.getInt(index * OBJ_SIZE + 21);
    }

    private static String getOuterString(MappedByteBuffer mbb, int index) {
        int offset = mbb.getInt(index * OBJ_SIZE + 25);
        int length = mbb.getInt(offset);
        byte[] bytes = new byte[length];
        int baseOffset = offset + 4;
        for (int i = 0; i < length; i++) {
            bytes[i] = mbb.get(baseOffset + i);
        }
        return new String(bytes);
    }

    private static String getInnerString(MappedByteBuffer mbb, int index) {
        int offset = mbb.getInt(index * OBJ_SIZE + 29);
        int length = mbb.getInt(offset);
        byte[] bytes = new byte[length];
        int baseOffset = offset + 4;
        for (int i = 0; i < length; i++) {
            bytes[i] = mbb.get(baseOffset + i);
        }
        return new String(bytes);
    }

    private static int getOuterInt(Unsafe unsafe, long address, int index) {
        return unsafe.getInt(address + (long) index * OBJ_SIZE);
    }

    private static long getOuterLong(Unsafe unsafe, long address, int index) {
        return unsafe.getLong(address + (long) index * OBJ_SIZE + 4);
    }

    private static boolean getOuterBool(Unsafe unsafe, long address, int index) {
        return unsafe.getByte(address + (long) index * OBJ_SIZE + 12) != 0;
    }

    private static double getOuterDouble(Unsafe unsafe, long address, int index) {
        return unsafe.getDouble(address + (long) index * OBJ_SIZE + 13);
    }

    private static int getInnerInt(Unsafe unsafe, long address, int index) {
        return unsafe.getInt(address + (long) index * OBJ_SIZE + 21);
    }

    private static String getOuterString(Unsafe unsafe, long address, int index) {
        return getString(unsafe, address, (long) index * OBJ_SIZE + 25);
    }

    private static String getInnerString(Unsafe unsafe, long address, int index) {
        return getString(unsafe, address, (long) index * OBJ_SIZE + 29);
    }

    private static String getString(Unsafe unsafe, long address, long stringOffset) {
        int offset = unsafe.getInt(address + stringOffset);
        int length = unsafe.getInt(address + offset);
        byte[] bytes = new byte[length];
        long baseOffset = address + offset + 4;
        for (int i = 0; i < length; i++) {
            bytes[i] = unsafe.getByte(baseOffset + i);
        }
        return new String(bytes);
    }

}
