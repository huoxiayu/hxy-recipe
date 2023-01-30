namespace java com.hxy.recipe.thrift

struct Request {
    1: required i64 id;
    2: optional string msg;
}

struct Response {
    1: required i64 id;
    2: optional string msg;
    3: required Request request;
}

struct C {
    1: optional i32 cInt;
    2: optional i64 cLong;
    3: optional list<i32> cIntList;
}

struct B {
    1: optional C c;
    2: optional double bDouble;
    3: optional i64 bLong;
}

struct A {
    1: optional B b;
    2: optional string aStr;
    3: optional bool aBool;
}

struct BigObject {
    1: optional list<A> aList;
}

struct BigObjectContainer {
    1: optional list<BigObject> bigObjectList;
    2: optional map<i32, double> i2d;
}

struct TimeTarget {
    1: optional i32 date;
    2: optional string beginTime;
    3: optional string endTime;
}

service ThriftService {
    Response send(1: Request request)
}