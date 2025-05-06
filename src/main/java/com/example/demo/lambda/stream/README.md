# Stream
Stream，就是JDK8又依托于函数式编程特性为集合类库做的一个类库，它其实就是jdk提供的函数式接口的最佳实践。
它能让我们通过lambda表达式更简明扼要的以流水线的方式去处理集合内的数据，可以很轻松的完成诸如：过滤、分组、收集、归约这类操作。
## 其中Stream的操作大致分为两类:
- 中间型操作 stream
  - API	        |功能说明	                                |无状态操作
    filter()	按照条件过滤符合要求的元素， 返回新的stream流。	是
    map()	将已有元素转换为另一个对象类型，一对一逻辑，返回新的stream流	是
    peek()	对stream流中的每个元素进行逐个遍历处理，返回处理后的stream流	是
    flatMap()	将已有元素转换为另一个对象类型，一对多逻辑，即原来一个元素对象可能会转换为1个或者多个新类型的元素，返回新的stream流	是
    limit()	仅保留集合前面指定个数的元素，返回新的stream流	否
    skip()	跳过集合前面指定个数的元素，返回新的stream流	否
    concat()	将两个流的数据合并起来为1个新的流，返回新的stream流	否
    distinct()	对Stream中所有元素进行去重，返回新的stream流	否
    sorted()	对stream中所有的元素按照指定规则进行排序，返回新的stream流	否
    takeWhile()	JDK9新增，传入一个断言参数当第一次断言为false时停止，返回前面断言为true的元素。	否
    dropWhile()	JDK9新增，传入一个断言参数当第一次断言为false时停止，删除前面断言为true的元素。	否
- 终结型操作 result
  - API	    |功能说明
    count()	返回stream处理后最终的元素个数
    max()	返回stream处理后的元素最大值
    min()	返回stream处理后的元素最小值
    findFirst()	找到第一个符合条件的元素时则终止流处理
    findAny()	找到任何一个符合条件的元素时则退出流处理，这个对于串行流时与findFirst相同，对于并行流时比较高效，任何分片中找到都会终止后续计算逻辑
    anyMatch()	返回一个boolean值，类似于isContains(),用于判断是否有符合条件的元素
    allMatch()	返回一个boolean值，用于判断是否所有元素都符合条件
    noneMatch()	返回一个boolean值， 用于判断是否所有元素都不符合条件
    *collect()	将流转换为指定的类型，通过Collectors进行指定
    *reduce()	将一个Stream中的所有元素反复结合起来，得到一个结果
    toArray()	将流转换为数组
    iterator()	将流转换为Iterator对象
    foreach()	无返回值，对元素进行逐个遍历，然后执行给定的处理逻辑