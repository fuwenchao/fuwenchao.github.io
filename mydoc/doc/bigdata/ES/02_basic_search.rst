es 基础查询语法
==================

前期准备
-------------

1. 准备数据

2. 创建索引

3. 批量导入


查询方式
-----------

1. 全文检索

  GET /bookdb_index/book/_search?q=guide

=

::

  {
      "query": {
          "multi_match" : {
              "query" : "guide",
              "fields" : ["_all"]
          }
      }
  }

  解读：使用multi_match关键字代替match关键字，作为对多个字段运行相同查询的方便的简写方式。 fields属性指定要查询的字段，在这种情况下，我们要对文档中的所有字段进行查询。


2. 特定字段检索

  GET /bookdb_index/book/_search?q=title:in action


::

  POST /bookdb_index/book/_search
  {
      "query": {
          "match" : {
              "title" : "in action"
          }
      },
      "size": 2,
      "from": 0,
      "_source": [ "title", "summary", "publish_date" ],
      "highlight": {
          "fields" : {
              "title" : {}
          }
      }
  }



    然而，full body的DSL为您提供了创建更复杂查询的更多灵活性（我们将在后面看到）以及指定您希望的返回结果。 在下面的示例中，我们指定要返回的结果数、偏移量（对分页有用）、我们要返回的文档字段以及属性的高亮显示。 
  结果数的表示方式：size; 
  偏移值的表示方式：from; 
  指定返回字段 的表示方式 ：_source; 
  高亮显示 的表示方式 ：highliaght。


3. 多字段检索

::

  POST /bookdb_index/book/_search
  {
      "query": {
          "multi_match" : {
              "query" : "elasticsearch guide",
              "fields": ["title", "summary"]
          }
      }
  }


4. Bool检索

bool查询接受”must”参数（等效于AND），一个must_not参数（相当于NOT）或者一个should参数（等同于OR）。


例如，如果我想在标题中搜索一本名为“Elasticsearch”或“Solr”的书，AND由“clinton gormley”创作，但NOT由“radu gheorge”创作：


::


  POST /bookdb_index/book/_search
  {
      "query": {
          "bool": {
              "must": {
                  "bool" : { "should": [
                        { "match": { "title": "Elasticsearch" }},
                        { "match": { "title": "Solr" }} ] }
              },
              "must": { "match": { "authors": "clinton gormely" }},
              "must_not": { "match": {"authors": "radu gheorge" }}
          }
      }
  }

5. Fuzzy 模糊检索

6. Wildcard Query 通配符检索

7. 正则表达式检索

8. 匹配短语检索

::

  匹配短语查询要求查询字符串中的所有词都存在于文档中，按照查询字符串中指定的顺序并且彼此靠近。

  默认情况下，这些词必须完全相邻，但您可以指定偏离值（slop value)，该值指示在仍然考虑文档匹配的情况下词与词之间的偏离值。


9. 匹配词组前缀检索

10. Term/Terms检索（指定字段检索）


term查询用于查找指定字段中包含指定分词的文件，只有当查询分词和文档中的分词精确匹配时才被检索到。


注意:是分词精确匹配

11. 过滤检索（Filtered query）5.0版本已不再存在，不必关注。

过滤的查询允许您过滤查询的结果。 如下的例子，我们在标题或摘要中查询名为“Elasticsearch”的图书，但是我们希望将结果过滤到只有20个或更多评论的结果。


::

  POST /bookdb_index/book/_search
  {
      "query": {
          "filtered": {
              "query" : {
                  "multi_match": {
                      "query": "elasticsearch",
                      "fields": ["title","summary"]
                  }
              },
              "filter": {
                  "range" : {
                      "num_reviews": {
                          "gte": 20
                      }
                  }
              }
          }
      },
      "_source" : ["title","summary","publisher", "num_reviews"]
  }


更新：已筛选的查询已推出的Elasticsearch 5.X版本中移除，有利于布尔查询。 这是与上面重写的使用bool查询相同的示例。 返回的结果是完全一样的。


::

  POST /bookdb_index/book/_search
  {
      "query": {
          "bool": {
              "must" : {
                  "multi_match": {
                      "query": "elasticsearch",
                      "fields": ["title","summary"]
                  }
              },
              "filter": {
                  "range" : {
                      "num_reviews": {
                          "gte": 20
                      }
                  }
              }
          }
      },
      "_source" : ["title","summary","publisher", "num_reviews"]
  }


12. 多个过滤器检索（Multiple Filters）5.x不再支持，无需关注。

多个过滤器可以通过使用布尔过滤器进行组合。

在下一个示例中，过滤器确定返回的结果必须至少包含20个评论，不得在2015年之前发布，并且应该由oreilly发布。



::

  POST /bookdb_index/book/_search
  {
      "query": {
          "filtered": {
              "query" : {
                  "multi_match": {
                      "query": "elasticsearch",
                      "fields": ["title","summary"]
                  }
              },
              "filter": {
                  "bool": {
                      "must": {
                          "range" : { "num_reviews": { "gte": 20 } }
                      },
                      "must_not": {
                          "range" : { "publish_date": { "lte": "2014-12-31" } }
                      },
                      "should": {
                          "term": { "publisher": "oreilly" }
                      }
                  }
              }
          }
      },
      "_source" : ["title","summary","publisher", "num_reviews", "publish_date"]
  }


  [Results]
  "hits": [
        {
          "_index": "bookdb_index",
          "_type": "book",
          "_id": "1",
          "_score": 0.5955761,
          "_source": {
            "summary": "A distibuted real-time search and analytics engine",
            "publisher": "oreilly",
            "num_reviews": 20,
            "title": "Elasticsearch: The Definitive Guide",
            "publish_date": "2015-02-07"
          }
        }
      ]


13. Function 得分：Field值因子（ Function Score: Field Value Factor）

14. Function得分：脚本得分（ Function Score: Script Scoring ）