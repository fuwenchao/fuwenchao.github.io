ES基础
========


START
----------

- 安装



- Elasticsearch配置


::

  qc-hermes-es-1：
  cluster.name: qc-hermes-search-cluster
  node.name: qc-hermes-es-1     #同hostname cat /etc/hostname
  path.data: /data/elasticsearch/data
  path.logs: /data/logs/elasticsearch
  path.plugins: /data/elasticsearch/plugins
  path.scripts: /data/elasticsearch/scripts
  bootstrap.mlockall: true
  network.host: 192.168.0.10   #本机IP地址
  http.port: 9200
  discovery.zen.ping.unicast.hosts: ["qc-hermes-es-1:9200", "qc-hermes-es-2:9200"]
  node.master: true
  node.data: true
  index.number_of_shards: 8
  index.number_of_replicas: 1


- 服务启动


  es01:9200

插件安装
--------------

- head

- kibana

- Marvel

- logstash

- graph

- 分词 IK analyzer




数据实时同步
------------

ES <-> MYSQL

ES <-> HIVE

ES <-> ORACLE

ES <-> logstash


ES操作
--------

- 增  PUT

- 删  DELETE

- 改  POST

- 查  GET

- HEAD 获取对象的基础信息

.. image:: ./images/es_mapping








{
    "query": {
        "match" : {
            "acct_no" : "6228130200496659"
        }
    },
    "size": 6,
    "from": 0,
   
    "highlight": {
        "fields" : {
            "rowkey" : {}
        }
    }
}




"tx_timestamp": "20180310 10:07:34",



{
    "query": {
        "range" : {
            "tx_timestamp" : {
                "gte" : "20180310 10:07:34",
                "lte" :  "20180311 10:07:34",
                "format": "yyyyMMdd HH:mm:ss"
            }
        }
    },
    "_source": [ "tx_timestamp" ]
}








"aggs" : {
                "articles_over_time" : {
                    "date_histogram" : {
                        "field" : "CreateTime",
                        "format": "MM-dd",
                        "interval" : "day"
                    }
}



{
  "size": 0,
  "aggs" : {
     "all_interests" : {
        "terms" : {
           "script" : "String he=new SimpleDateFormat('HH').format(new Date(doc['tx_timestamp'].value))",
           "order" : { "_term" : "desc" }
        }
     }
  }
}




建议采用date histogram聚合，格式如下：
"aggs" : {
                "articles_over_time" : {
                    "date_histogram" : {
                        "field" : "CreateTime",
                        "format": "MM-dd",
                        "interval" : "day"
                    }
}
其中interval设置日期间隔，format设置日期格式，具体可以参考官方文档：
https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-bucket-datehistogram-aggregation.html








{
  "size": 0,
  "aggs": {
    "groupDate": {
      "date_histogram": {
        "field": "tx_timestamp",
        "interval": "hour",
        "format": "HH"
      }
    }
  }
     "aggs":{
        "avg_count":{
          "avg":{"field": "doc_count"}
        }
      }
}





{
  "size": 0,
  "aggs": {
    "groupDate": {
      "date_histogram": {
        "field": "tx_timestamp",
        "interval": "hour",
        "format": "HH"
      }
    },
  },
      "aggs":{
        "avg_count":{
          "avg":{"field": "doc_count"}
        }
      }
    
}





{
  "size": 0,
  "aggs": {
    "groupDate": {
      "date_histogram": {
        "field": "tx_timestamp",
        "interval": "hour",
        "format": "HH"
      },
      "aggs":{
        "terms":{"field":"key"},
        "avg_count":{
          "avg":{"field": "doc_count"}
        }
      }
    }
  }
}



{
  "size": 0,
  "aggs": {
    "groupDate": {
      "date_histogram": {
        "field": "tx_timestamp",
        "format": "HH"
      }

    }
  }
}



