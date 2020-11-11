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

struct TimeTarget {
    1: optional i32 date;
    2: optional string beginTime;
    3: optional string endTime;
}

service ThriftService {
    Response send(1: Request request)
}