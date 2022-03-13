# Apache AGE utils
This repository contains utility classes to be used with Apache AGE and MyBatis.

## AgtypeWrapper
The `AgtypeWrapper` is the main object inside this library and it is used to map input parameters for MyBatis Mappers.

Example usage:
```java
FindItemParams params = new FindItemParams();
params.setCode(code);
GraphItem<ItemProperties> item = itemsMapper.findItemByParams(AgtypeWrapper.from(params));

GraphItem<ItemProperties> item = itemsMapper.findItemByCode(AgtypeWrapper.from("code"));
```

Example Cypher query parameter mapping:
```sql
SELECT * FROM cypher('${schema}', $$
    MATCH (i:Item ${params})
    RETURN id(i), properties(i)
$$) AS (id agtype, properties agtype);
```

## GraphItem
The `GraphItem` is a utility class to wrap the result type of graph query with the node `id` and the generic `properties`.
You can define your custom properties mapping object and pass it as the generic type inside the GraphItem definition.

```java
GraphItem<ItemProperties> item = itemsMapper.findItemByCode(AgtypeWrapper.from("code"));
long id = item.getId();
ItemProperties props = item.getProperties();
```

## Building the library
To build the current library, in addition to the dependencies listed inside the build.gradle script you need to do the following:
- clone the [Apache AGE repository](https://github.com/apache/incubator-age)
- build the java driver from the drivers/jdbc directory inside the repository
- copy the output jar file into the libs folder of this repository