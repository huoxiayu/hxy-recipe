package com.hxy.recipe.tungsten;

// Reference On Databricks
// https://www.databricks.com/glossary/tungsten
// https://www.zhaolipan.com/2021/02/07/Spark-Tungsten/
/**
 *  Tungsten Project Includes These Initiatives:
 *  1. Memory Management and Binary Processing: leveraging application semantics to manage memory explicitly and eliminate the overhead of JVM object model and garbage collection
 *  2. Cache-aware computation: algorithms and data structures to exploit memory hierarchy
 *  3. Code generation: using code generation to exploit modern compilers and CPUs
 *  4. No virtual function dispatches: this reduces multiple CPU calls which can have a profound impact on performance when dispatching billions of times.
 *  5. Intermediate data in memory vs CPU registers: Tungsten Phase 2 places intermediate data into CPU registers. This is an order of magnitudes reduction in the number of cycles to obtain data from the CPU registers instead of from memory
 *  6. Loop unrolling and SIMD: Optimize Apache Spark’s execution engine to take advantage of modern compilers and CPUs’ ability to efficiently compile and execute simple for loops (as opposed to complex function call graphs).
 **/
// 火山模型
// catalyst优化器
public class TungstenStart {

    public static void main(String[] args) {

    }

}
