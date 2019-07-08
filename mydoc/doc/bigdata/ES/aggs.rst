桶聚合
=========


初始化数据
----------------

为了满足桶聚合多样性需求，修改文档如下。

::


  DELETE my-index

  PUT my-index

  PUT my-index/persion/1
  {
    "name":"张三",
    "age":27,
    "gender":"男",
    "salary":15000,
    "dep":"bigdata"
  }

  PUT my-index/persion/2
  {
    "name":"李四",
    "age":26,
    "gender":"女",
    "salary":15000,
    "dep":"bigdata"
  }

  PUT my-index/persion/3
  {
    "name":"王五",
    "age":26,
    "gender":"男",
    "salary":17000,
    "dep":"AI"
  }
  PUT my-index/persion/4
  {
    "name":"刘六",
    "age":27,
    "gender":"女",
    "salary":18000,
    "dep":"AI"
  }

  PUT my-index/persion/5
  {
    "name":"程裕强",
    "age":31,
    "gender":"男",
    "salary":20000,
    "dep":"bigdata"
  }
  PUT my-index/persion/6
  {
    "name":"hadron",
    "age":30,
    "gender":"男",
    "salary":20000,
    "dep":"AI"
  }


Bucket aggregations
--------------------------


  https://www.elastic.co/guide/en/elasticsearch/reference/6.1/search-aggregations-bucket.html 

  在页面右下角可以看到各类具体的Bucket聚合连接 



Terms Aggregation
^^^^^^^^^^^^^^^^^^^^^^^^^^^


Terms聚合用于分组聚合。 

**【例子】根据薪资水平进行分组，统计每个薪资水平的人数**

请求:

::

  GET my-index/_search
  {
    "size": 0, 
    "aggs": {
      "group_count": {
        "terms": {"field": "salary"}
      }
    }
  }


返回:

::

  {
    "took": 7,
    "timed_out": false,
    "_shards": {
      "total": 5,
      "successful": 5,
      "skipped": 0,
      "failed": 0
    },
    "hits": {
      "total": 6,
      "max_score": 0,
      "hits": []
    },
    "aggregations": {
      "group_count": {
        "doc_count_error_upper_bound": 0,
        "sum_other_doc_count": 0,
        "buckets": [
          {
            "key": 15000,
            "doc_count": 2
          },
          {
            "key": 20000,
            "doc_count": 2
          },
          {
            "key": 17000,
            "doc_count": 1
          },
          {
            "key": 18000,
            "doc_count": 1
          }
        ]
      }
    }
  }


**【例子】统计上面每个分组的平均年龄**

请求:

::

  GET my-index/_search
  {
    "size": 0, 
    "aggs": {
      "group_count": {
        "terms": {"field": "salary"},
        "aggs":{
          "avg_age":{
            "avg":{"field": "age"}
          }
        }
      }
    }
  }

返回:

::

  {
    "took": 4,
    "timed_out": false,
    "_shards": {
      "total": 5,
      "successful": 5,
      "skipped": 0,
      "failed": 0
    },
    "hits": {
      "total": 6,
      "max_score": 0,
      "hits": []
    },
    "aggregations": {
      "group_count": {
        "doc_count_error_upper_bound": 0,
        "sum_other_doc_count": 0,
        "buckets": [
          {
            "key": 15000,
            "doc_count": 2,
            "avg_age": {
              "value": 26.5
            }
          },
          {
            "key": 20000,
            "doc_count": 2,
            "avg_age": {
              "value": 30.5
            }
          },
          {
            "key": 17000,
            "doc_count": 1,
            "avg_age": {
              "value": 26
            }
          },
          {
            "key": 18000,
            "doc_count": 1,
            "avg_age": {
              "value": 27
            }
          }
        ]
      }
    }
  }


**统计每个部门的人数**

请求:

::

  GET my-index/_search
  {
    "size": 0, 
    "aggs": {
      "group_count": {
        "terms": {"field": "dep"}
      }
    }
  }

响应:

::

  {
    "error": {
      "root_cause": [
        {
          "type": "illegal_argument_exception",
          "reason": "Fielddata is disabled on text fields by default. Set fielddata=true on [dep] in order to load fielddata in memory by uninverting the inverted index. Note that this can however use significant memory. Alternatively use a keyword field instead."
        }
      ],
      "type": "search_phase_execution_exception",
      "reason": "all shards failed",
      "phase": "query",
      "grouped": true,
      "failed_shards": [
        {
          "shard": 0,
          "index": "my-index",
          "node": "cNWkQjt9SzKFNtyx8IIu-A",
          "reason": {
            "type": "illegal_argument_exception",
            "reason": "Fielddata is disabled on text fields by default. Set fielddata=true on [dep] in order to load fielddata in memory by uninverting the inverted index. Note that this can however use significant memory. Alternatively use a keyword field instead."
          }
        }
      ]
    },
    "status": 400
  }


根据错误提示”Fielddata is disabled on text fields by default. Set fielddata=true on [dep] in order to load fielddata in memory by uninverting the inverted index. Note that this can however use significant memory. Alternatively use a keyword field instead.”可知，需要开启fielddata参数。只需要设置某个字段"fielddata": true即可。 

此外，根据官方文档提示se the my_field.keyword field for aggregations, sorting, or in scripts，可以尝试my_field.keyword格式用于聚合操作。



::


  GET my-index/_search
  {
    "size": 0, 
    "aggs": {
      "group_count": {
        "terms": {"field": "dep.keyword"}
      }
    }
  }


Filter Aggregation
^^^^^^^^^^^^^^^^^^^^^^^^^^^


Filter聚合用于过滤器聚合，把满足过滤器条件的文档分到一组。

**【例子】计算男人的平均年龄**



也就是统计gender字段包含关键字“男”的文档的age平均值。

::

  GET my-index/_search
  {
    "size": 0, 
    "aggs": {
      "group_count": {
        "filter": {
          "term":{"gender": "男"}
        },
        "aggs":{
          "avg_age":{
            "avg":{"field": "age"}
          }
        }
      }
    }
  }
 

 返回:

 ::

   {
    "took": 2,
    "timed_out": false,
    "_shards": {
      "total": 5,
      "successful": 5,
      "skipped": 0,
      "failed": 0
    },
    "hits": {
      "total": 6,
      "max_score": 0,
      "hits": []
    },
    "aggregations": {
      "group_count": {
        "doc_count": 4,
        "avg_age": {
          "value": 28.5
        }
      }
    }
  }


Filters Aggregation
^^^^^^^^^^^^^^^^^^^^^^^^^^^^

**【例子】统计body字段包含”error”和包含”warning”的文档数**

::

  PUT /logs/message/_bulk?refresh
  { "index" : { "_id" : 1 } }
  { "body" : "warning: page could not be rendered" }
  { "index" : { "_id" : 2 } }
  { "body" : "authentication error" }
  { "index" : { "_id" : 3 } }
  { "body" : "warning: connection timed out" }

  GET logs/_search
  {
    "size": 0,
    "aggs" : {
      "messages" : {
        "filters" : {
          "filters" : {
            "errors" :   { "match" : { "body" : "error"   }},
            "warnings" : { "match" : { "body" : "warning" }}
          }
        }
      }
    }
  }


返回:

::


  {
    "took": 54,
    "timed_out": false,
    "_shards": {
      "total": 5,
      "successful": 5,
      "skipped": 0,
      "failed": 0
    },
    "hits": {
      "total": 3,
      "max_score": 0,
      "hits": []
    },
    "aggregations": {
      "messages": {
        "buckets": {
          "errors": {
            "doc_count": 1
          },
          "warnings": {
            "doc_count": 2
          }
        }
      }
    }
  }


**【例子】统计男女员工的平均年龄**

::

  GET my-index/_search
  {
    "size": 0, 
    "aggs": {
      "group_count": {
        "filters":{
          "filters": [
            {"match":{"gender": "男"}},
            {"match":{"gender": "女"}}
          ]
        },
        "aggs":{
          "avg_age":{
              "avg":{"field": "age"}
          }
        }
      }
    }
  }

返回:

::

  {
    "took": 5,
    "timed_out": false,
    "_shards": {
      "total": 5,
      "successful": 5,
      "skipped": 0,
      "failed": 0
    },
    "hits": {
      "total": 6,
      "max_score": 0,
      "hits": []
    },
    "aggregations": {
      "group_count": {
        "buckets": [
          {
            "doc_count": 4,
            "avg_age": {
              "value": 28.5
            }
          },
          {
            "doc_count": 2,
            "avg_age": {
              "value": 26.5
            }
          }
        ]
      }
    }
  }




Range Aggregation
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^


from..to区间范围是[from,to),也就是说包含from点，不包含to点 


**【例子】查询薪资在[0,10000),[10000,20000),[2000,+无穷大)三个范围的员工数**

::

  GET my-index/_search
  {
    "size": 0, 
    "aggs": {
      "group_count": {
        "range": {
          "field": "salary",
          "ranges": [
              {"to": 10000},
              {"from": 10000,"to":20000},  
              {"from": 20000}
          ]
        }
      }
    }
  }


**【例子】查询发布日期在2016-12-01之前、2016-12-01至2017-01-01、2017-01-01之后三个时间区间的文档数**


::

  GET website/_search
  {
    "size": 0, 
    "aggs": {
      "group_count": {
        "range": {
          "field": "postdate",
          "format":"yyyy-MM-dd",
          "ranges": [
              {"to": "2016-12-01"},
              {"from": "2016-12-01","to":"2017-01-01"},  
              {"from": "2017-01-01"}
          ]
        }
      }
    }
  }



Date Range聚合
^^^^^^^^^^^^^^^^^^^^^


https://www.elastic.co/guide/en/elasticsearch/reference/6.1/search-aggregations-bucket-daterange-aggregation.html 


A range aggregation that is dedicated for date values. The main difference between this aggregation and the normal range aggregation is that the from and to values can be expressed in Date Math expressions, and it is also possible to specify a date format by which the from and to response fields will be returned. Note that this aggregation includes the from value and excludes the to value for each range. 

专用于日期值的范围聚合。 这种聚合和正常范围聚合的主要区别在于，起始和结束值可以在日期数学表达式中表示，
并且还可以指定返回起始和结束响应字段的日期格式。 

请注意，此聚合包含from值并排除每个范围的值。


**【例子】计算一年前之前发表的博文数和从一年前以来发表的博文总数**

::

  GET website/_search
  {
    "size": 0, 
    "aggs": {
      "group_count": {
        "range": {
          "field": "postdate",
          "format":"yyyy-MM-dd",
          "ranges": [
              {"to": "now-12M/M"},
              {"from": "now-12M/M"}
          ]
        }
      }
    }
  }


Missing聚合
^^^^^^^^^^^^^

基于字段数据的单桶集合，创建当前文档集上下文中缺少字段值（实际上缺少字段或设置了配置的NULL值）的所有文档的桶。 此聚合器通常会与其他字段数据存储桶聚合器（如范围）一起使用，以返回由于缺少字段数据值而无法放置在其他存储桶中的所有文档的信息。


::

  PUT my-index/persion/7
  {
    "name":"test",
    "age":30,
    "gender":"男"
  }
  PUT my-index/persion/8
  {
    "name":"abc",
    "age":28,
    "gender":"女"
  }
  PUT my-index/persion/9
  {
    "name":"xyz",
    "age":32,
    "gender":"男",
    "salary":null,
    "dep":null
  }


salary字段缺少的文档


::

  GET my-index/_search
  {
    "size": 0, 
    "aggs": {
      "noDep_count": {
        "missing": {"field": "salary"}
      }
    }
  }


返回:

::

  {
    "took": 29,
    "timed_out": false,
    "_shards": {
      "total": 5,
      "successful": 5,
      "skipped": 0,
      "failed": 0
    },
    "hits": {
      "total": 9,
      "max_score": 0,
      "hits": []
    },
    "aggregations": {
      "noDep_count": {
        "doc_count": 3
      }
    }
  }


children聚合
^^^^^^^^^^^^^^^^^

  A special single bucket aggregation that selects child documents that have the specified type, as defined in a join field. 

  一个特殊的单桶集合，用于选择具有指定类型的子文档，如join字段中定义的。


  ......









实战
-------


数据

::

  {"index":{"_id":"1"}}
  {"username":"aoa","money":1000,"age":32,"city":"nanjing"}
  {"index":{"_id":"2"}}
  {"username":"bob","money":2000,"age":15,"city":"nanjing"}
  {"index":{"_id":"3"}}
  {"username":"coc","money":3000,"age":15,"city":"beijing"}
  {"index":{"_id":"4"}}
  {"username":"dod","money":4000,"age":15,"city":"beijing"}
  {"index":{"_id":"5"}}
  {"username":"eoe","money":5000,"age":15,"city":"shanghai"}
  {"index":{"_id":"6"}}
  {"username":"fof","money":6000,"age":15,"city":"shanghai"}


curl -XPOST "127.0.0.1:9200/te/user/_bulk?pretty" --data-binary @user.json



**1.查询city不是nanjing的，且年龄是32或者15的user，且只显示username和city字段**

::

  POST http://127.0.0.1:9200/te/user/_search
  {
    "_source":["username","city"],
    "query":{
      "bool":{
        "must":[
          {
            "terms":{
              "age":[32,15]
            }
          }
        ],
        "must_not":{
          "term":{
            "city":"nanjing"
          }
        }
      }
    }
  }


**2.查询年龄是15且money最少的user的money数**

::

  POST http://127.0.0.1:9200/te/user/_search
  {
    "query":{
      "term":{
        "age":15
      }
    },
    "aggs":{
      "my_agg_name":{
        "min":{
          "field":"money"
        }
      }
    }
  }


 注意，返回结果在"aggregations"里，不在"hits"里，"hits"里返回的结果对应"query"里的条件。

以下是返回结果：

::

  {
    ...
    "aggregations":{
      "my_agg_name":{
        "value":2000
      }
    }
  }

**3.查询所有user的money的平均值**

::

  POST http://127.0.0.1:9200/te/user/_search
  {
    "aggs":{
      "my_agg_name":{
        "avg":{
          "field":"money"
        }
      }
    }
  }

**4.查询money范围为(-无穷,1000)、[1000, 3000)、[3000, +无穷)这3个区间的user数**

::

  -- 分组(桶)聚合-范围聚合

  POST http://127.0.0.1:9200/te/user/_search
  {
    "aggs":{
      "my_agg_name":{
        "range":{
          "field":"money",
          "ranges":[
            {"to":1000},
            {"from":1000,"to":3000},
            {"from":3000}
          ]
        }
      }
    }
  }



  {
    ...
    "aggregations":{
      "my_agg_name":{
        "buckets":[
          {
            "key":"*-1000.0",
            "to":1000,
            "to_as_string":"1000.0",
            "doc_count":0
          },
          {
            "key":"1000.0-3000.0",
            "from":1000,
            "from_as_string":"1000.0",
            "to":3000,
            "to_as_string":"3000.0",
            "doc_count":2
          },
          {
            "key":"3000.0-*",
            "from":3000,
            "from_as_string":"3000.0",
            "doc_count":4
          }
        ]
      }
    }
  }


 **5.查询money范围为(-无穷,1000)、[1000, 3000)、[3000, +无穷)这3个区间各自的user的money的总和**


::

  --管道聚合 piple -平均分组聚合

  POST http://127.0.0.1:9200/te/user/_search
  {
    "aggs":{
      "my_agg_name":{
        "range":{
          "field":"money",
          "ranges":[
            {"to":1000},
            {"from":1000,"to":3000},
            {"from":3000}
          ]
        },
        "aggs":{
          "my_agg_name_child":{
            "sum":{"field":"money"}
          }
        }
      },
      "my_agg_name_2":{
        "avg_bucket":{"buckets_path":"my_agg_name>my_agg_name_child"}
      }
    }
  }


  从返回结果看，
  my_agg_name是桶聚合("my_agg_name"下有"buckets")，
  my_agg_name_2是度量聚合("my_agg_name_2"下有"value")。
  以下是返回结果：

  {
    ...
    "aggregations":{
      "my_agg_name":{
        "buckets":[
          {
            "key":"*-1000.0",
            "to":1000,
            "to_as_string":"1000.0",
            "doc_count":0,
            "my_agg_name_child":{
              "value":0
            }
          },
          {
            "key":"1000.0-3000.0",
            "from":1000,
            "from_as_string":"1000.0",
            "to":3000,
            "to_as_string":"3000.0",
            "doc_count":2,
            "my_agg_name_child":{
              "value":3000
            }
          },
          {
            "key":"3000.0-*",
            "from":3000,
            "from_as_string":"3000.0",
            "doc_count":4,
            "my_agg_name_child":{
              "value":18000
            }
          }
        ]
      },
      "my_agg_name_2":{
        "value":10500   # (18000 + 3000)/2
      }
    }
  }
