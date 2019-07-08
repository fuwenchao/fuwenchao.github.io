时间分组聚合查询
==================


正常业务逻辑中，会出现大量的数据统计，比如说分组聚合查询，根据天进行数据的统计，记录下es分组聚合查询

::

  {
    "size": 0,
    "aggs": {
      "groupDate": {
        "date_histogram": {
          "field": "create_date",
          "interval": "day",
          "format": "yyyy-MM-dd"
        }
      }
    }
  }


此处使用按天分组，可用的时间间隔表达式：year, quarter, month, week, day, hour, minute, second（年份、季度、月、周、日、小时、分钟、秒）。

interval(时间间隔)的可用表达式：

::

  year（1y）年
  quarter（1q）季度
  month（1M）月份
  week（1w）星期
  day（1d）天
  hour（1h）小时
  minute（1m）分钟
  second（1s）秒


::

  {
    "size": 0,
    "aggs": {
      "groupDate": {
        "date_histogram": {
          "field": "create_date",
          "interval": "90m",
          "format": "yyyy-MM-dd"
        }
      }
    }
  }


此处也可以根据小时分组，1.5h则用分钟来表示（90m） 

上面是官方提供的方法，size属性可以控制是否返回聚合的数据结果集，因为我们设置了 size 参数，所以不会有 hits 搜索结果返回。


::

  {
      "aggregations":{
          "groupDate":{
              "buckets":[
                  {
                      "key_as_string":"2018-08-28",
                      "key":1535430600000,
                      "doc_count":590000
                  },
                  {
                      "key_as_string":"2018-08-28",
                      "key":1535436000000,
                      "doc_count":470000
                  }
              ]
          }
      }
  }


groupDate 聚合是作为 aggregations 字段的一部分被返回的，每一个 key 都与分组条件对应，我这里是根据1.5小时分组，key 将会显示分组的时间，doc_count 字段，将会告诉我们包含此项的文档数量。 


下面是根据官方文档给出的api调用方式:

::

  // 声明where 条件
  BoolQueryBuilder qbs = QueryBuilders.boolQuery();

  AggregationBuilder aggregation = AggregationBuilders.dateHistogram("agg").field("create_time")
          .interval(DateHistogramInterval.DAY).format("yyyy-MM-dd");

  SearchRequestBuilder requestBuilder = client.prepareSearch("user_login_detail")
          .setTypes("login_detail");
  requestBuilder.setQuery(qbs);
  requestBuilder.addAggregation(aggregation);
  SearchResponse response = requestBuilder.execute().actionGet();
  Histogram agg = response.getAggregations().get("agg");

  // For each entry
  for (Histogram.Bucket entry : agg.getBuckets()) {
      String key = StringUtil.nullBlank(entry.getKey());
      String keyAsString = entry.getKeyAsString();
      long docCount = entry.getDocCount();

      System.out.println("key [{" + keyAsString + "}]");
      System.out.println("date [{" + key + "}]");
      System.out.println("doc_count [{" + docCount + "}]");
  }






-----


::

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
  }


-------------


**每个月销售了多少辆车**

::

  GET /cars/transactions/_search?search_type=count
  {
     "aggs": {
        "sales": {
           "date_histogram": {
              "field": "sold",
              "interval": "month", 
              "format": "yyyy-MM-dd" 
           }
        }
     }
  }

在查询中有一个聚合，它为每个月创建了一个桶。它能够告诉我们每个月销售了多少辆车。同时指定了一个额外的格式参数让桶拥有更"美观"的键值。在内部，日期被简单地表示成数值。然而这会让UI设计师生气，因此使用格式参数可以让日期以更常见的格式进行表示。

得到的响应符合预期，但是也有一点意外(看看你能够察觉到)：


::

  {
     ...
     "aggregations": {
        "sales": {
           "buckets": [
              {
                 "key_as_string": "2014-01-01",
                 "key": 1388534400000,
                 "doc_count": 1
              },
              {
                 "key_as_string": "2014-02-01",
                 "key": 1391212800000,
                 "doc_count": 1
              },
              {
                 "key_as_string": "2014-05-01",
                 "key": 1398902400000,
                 "doc_count": 1
              },
              {
                 "key_as_string": "2014-07-01",
                 "key": 1404172800000,
                 "doc_count": 1
              },
              {
                 "key_as_string": "2014-08-01",
                 "key": 1406851200000,
                 "doc_count": 1
              },
              {
                 "key_as_string": "2014-10-01",
                 "key": 1412121600000,
                 "doc_count": 1
              },
              {
                 "key_as_string": "2014-11-01",
                 "key": 1414800000000,
                 "doc_count": 2
              }
           ]
  ...
  }

聚合完整地被表达出来了。你能看到其中有用来表示月份的桶，每个桶中的文档数量，以及漂亮的key_as_string。


**返回空桶**

发现在上面的响应中的奇怪之处了吗？

Yep, that’s right. We are missing a few months! By default, the date_histogram (and histogram too) returns only buckets that have a nonzero document count. 是的，我们缺失了几个月！默认情况下，date_histogram(以及histogram)只会返回文档数量大于0的桶。

这意味着得到的histogram响应是最小的。但是有些时候该行为并不是我们想要的。对于很多应用而言，你需要将得到的响应直接置入到一个图形库中，而不需要任何额外的处理。

因此本质上，我们需要返回所有的桶，哪怕其中不含有任何文档。我们可以设置两个额外的参数来实现这一行为：



::

  GET /cars/transactions/_search?search_type=count
  {
     "aggs": {
        "sales": {
           "date_histogram": {
              "field": "sold",
              "interval": "month",
              "format": "yyyy-MM-dd",
              "min_doc_count" : 0, 
              "extended_bounds" : { 
                  "min" : "2014-01-01",
                  "max" : "2014-12-31"
              }
           }
        }
     }

以上的min_doc_count参数会强制返回空桶，extended_bounds参数会强制返回一整年的数据。

这两个参数会强制返回该年中的所有月份，无论它们的文档数量是多少。min_doc_count的意思很容易懂：它强制返回哪怕为空的桶。

extended_bounds参数需要一些解释。min_doc_count会强制返回空桶，但是默认ES只会返回在你的数据中的最小值和最大值之间的桶。

因此如果你的数据分布在四月到七月，你得到的桶只会表示四月到七月中的几个月(可能为空，如果使用了min_doc_count=0)。为了得到一整年的桶，我们需要告诉ES需要得到的桶的范围。

extended_bounds参数就是用来告诉ES这一范围的。一旦你添加了这两个设置，得到的响应就很容易被图形生成库处理而最终得到下图：


**另外的例子**

我们已经看到过很多次，为了实现更复杂的行为，桶可以嵌套在桶中。为了说明这一点，我们会创建一个用来显示每个季度，所有制造商的总销售额的聚合。同时，我们也会在每个季度为每个制造商单独计算其总销售额，因此我们能够知道哪种汽车创造的收益最多：


::


  GET /cars/transactions/_search?search_type=count
  {
     "aggs": {
        "sales": {
           "date_histogram": {
              "field": "sold",
              "interval": "quarter", 
              "format": "yyyy-MM-dd",
              "min_doc_count" : 0,
              "extended_bounds" : {
                  "min" : "2014-01-01",
                  "max" : "2014-12-31"
              }
           },
           "aggs": {
              "per_make_sum": {
                 "terms": {
                    "field": "make"
                 },
                 "aggs": {
                    "sum_price": {
                       "sum": { "field": "price" } 
                    }
                 }
              },
              "total_sum": {
                 "sum": { "field": "price" } 
              }
           }
        }
     }
  }

可以发现，interval参数被设成了quarter。

得到的响应如下(删除了很多)：

::

  {
  ....
  "aggregations": {
     "sales": {
        "buckets": [
           {
              "key_as_string": "2014-01-01",
              "key": 1388534400000,
              "doc_count": 2,
              "total_sum": {
                 "value": 105000
              },
              "per_make_sum": {
                 "buckets": [
                    {
                       "key": "bmw",
                       "doc_count": 1,
                       "sum_price": {
                          "value": 80000
                       }
                    },
                    {
                       "key": "ford",
                       "doc_count": 1,
                       "sum_price": {
                          "value": 25000
                       }
                    }
                 ]
              }
           },
  ...




-------------


::


          // 查询
          "query": {
              "bool": {
                  "must": [{
                      "range": {
                          "@timestamp": {
                              "gte": 1533556800000,
                              "lte": 1533806520000
                          }
                      }
                  }]
              }
          },
          // 不显示具体的内容
          "size": 0,
          // 聚合
          "aggs": {
              // 自己取的聚合名字
              "group_by_grabTime": {
                  // es提供的时间处理函数
                  "date_histogram": {
                      // 需要聚合分组的字段名称, 类型需要为date, 格式没有要求
                      "field": "@timestamp",
                      // 按什么时间段聚合, 这里是5分钟, 可用的interval在上面给出
                      "interval": "5m",
                      // 设置时区, 这样就相当于东八区的时间
                      "time_zone":"+08:00",
                      // 返回值格式化，HH大写，不然不能区分上午、下午
                      "format": "yyyy-MM-dd HH",   
                      // 为空的话则填充0
                      "min_doc_count": 0,
                      // 需要填充0的范围
                      "extended_bounds": {
                          "min": 1533556800000,
                          "max": 1533806520000
                      }
                  },
                  // 聚合
                  "aggs": {
                      // 自己取的名称
                      "group_by_status": {
                          // es提供
                          "terms": {
                              // 聚合字段名
                              "field": "LowStatusOfPrice"
                          }
                      }
                  }
              }
          }


返回结果:

  {
      "took": 960,
      "timed_out": false,
      "_shards": {
          "total": 5,
          "successful": 5,
          "skipped": 0,
          "failed": 0
      },
      "_clusters": {
          "total": 3,
          "successful": 3,
          "skipped": 0
      },
      "hits": {
          "total": 13494821,
          "max_score": 0,
          "hits": []
      },
      "aggregations": {
          "group_by_grabTime": {
              "buckets": [
                  {
                      "key_as_string": "2018-08-06 12",
                      "key": 1533556800000,
                      "doc_count": 25851,
                      "group_by_status": {
                          "doc_count_error_upper_bound": 0,
                          "sum_other_doc_count": 0,
                          "buckets": [
                              {
                                  "key": "2",
                                  "doc_count": 10804
                              },
                              {
                                  "key": "1",
                                  "doc_count": 7240
                              },
                              {
                                  "key": "4",
                                  "doc_count": 6716
                              },
                              {
                                  "key": "3",
                                  "doc_count": 1091
                              }
                          ]
                      }
                  },
                  {
                      "key_as_string": "2018-08-06 13",
                      "key": 1533562200000,
                      "doc_count": 25282,
                      "group_by_status": {
                          "doc_count_error_upper_bound": 0,
                          "sum_other_doc_count": 0,
                          "buckets": [
                              {
                                  "key": "2",
                                  "doc_count": 10457
                              },
                              {
                                  "key": "1",
                                  "doc_count": 7185
                              },
                              {
                                  "key": "4",
                                  "doc_count": 6696
                              },
                              {
                                  "key": "3",
                                  "doc_count": 944
                              }
                          ]
                      }
                  },
                  .....

                  特别说明

                      "buckets": [
                          {
                              "key_as_string": "2018-08-06 12",
                              "key": 1533556800000,

                  1.关于统计的时间段 
                            2018-08-06 12 统计的是12~13点之间的数据 
                            以此类推 
                            2018-08-06 00 统计的是00~01之间的数据 
                            2018-08-06 23 统计的是23~次日00之间的数据

                  2.关于key 和 key_as_string 
                            key_as_string 不一定完全可信, 即key按照用户当前时间格式化之后不一定等于key_as_string 
                            这是由建立es索引时采用的时区决定的, 用之前最好验证一下, 比如说博主现在正在做的一个项目中, 由于es采用的是ISO8859-1的时间格式, 导致所有时区提前了8个小时, 所以在查询和统计时, 一定要记得补偿这8个小时的数据 
                            可以通过设置时区来解决这个问题
