package com.hxy.ml.tensorflow;

import com.google.common.base.Preconditions;
import org.tensorflow.DataType;
import org.tensorflow.Graph;
import org.tensorflow.Output;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.Tensors;

import java.util.Arrays;
import java.util.List;

public class TFStart {

    public static void main(String[] args) {
        try (Graph g = new Graph(); Session s = new Session(g)) {


            // construct a graph to add two float Tensors, using placeholders.
            Output<?> x = g.opBuilder("Placeholder", "x")
                    .setAttr("dtype", DataType.FLOAT)
                    .build()
                    .output(0);

            Output<?> y = g.opBuilder("Placeholder", "y")
                    .setAttr("dtype", DataType.FLOAT)
                    .build()
                    .output(0);

            // necessary
            Output<?> z = g.opBuilder("Add", "z")
                    .addInput(x)
                    .addInput(y)
                    .build()
                    .output(0);

            // execute the graph multiple times, each time with a different value of x and y
            float[] X = new float[]{1, 2, 3};
            float[] Y = new float[]{4, 5, 6};
            for (int i = 0; i < X.length; i++) {
                Tensor<Float> tx = Tensors.create(X);
                Tensor<Float> ty = Tensors.create(Y);
                List<Tensor<?>> tz = s.runner()
                        .feed("x", tx)
                        .feed("y", ty)
                        .fetch("z")
                        .run();
                System.out.println("x: " + Arrays.toString(tx.copyTo(new float[3])));
                System.out.println("y: " + Arrays.toString(ty.copyTo(new float[3])));
                Preconditions.checkArgument(tz.size() == 1);
                System.out.println("z: " + Arrays.toString(tz.get(0).copyTo(new float[3])));
            }
        }
    }
}
