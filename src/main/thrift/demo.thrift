namespace java com.hxy.recipe.rpc.thrift

struct Request {
    1: required i64 id;
    2: optional string msg;
}

struct Response {
    1: required i64 id;
    2: optional string msg;
    3: required Request request;
}

service ThriftService {
    Response send(1: Request request)
}