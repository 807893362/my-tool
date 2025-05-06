package com.example.demo.lambda;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * computeIfAbsent() 方法对 hashMap 中指定 key 的值进行重新计算，如果不存在这个 key，则添加到 hashMap 中。
 * - key - 键
 * - remappingFunction - 重新映射函数，用于重新计算值
 * - 如果 key 对应的 value 不存在，则使用获取 remappingFunction 重新计算后的值，并保存为该 key 的 value，否则返回 value。
 */
public class ComputeIfAbsentTest {


    public static void main(String[] args) {
        test1();
        test2();

    }

    private static HashMap<String, Set<String>> test1(){

        HashMap<String, Set<String>> hashMap = new HashMap<>();
        Set<String> set = new HashSet<>();
        set.add("zhangSan");
        hashMap.put("china", set);
        // 判断map中是否存在，如果存在则添加元素到set中，如果不存在则新建set添加到hashMap中
        if(hashMap.containsKey("china")) {
            hashMap.get("china").add("liSi");
        } else {
            Set<String> setTmp = new HashSet<>();
            setTmp.add("liSi");
            hashMap.put("china", setTmp);
        }
        System.out.println("test1 old: " + hashMap.toString());


        // after JDK1.8
        hashMap.computeIfAbsent("china", key -> new HashSet<>()).add("liSi2");
        System.out.println("test1 stream: " + hashMap.toString());
        return hashMap;
    }

    private static Map<String, Map<Long, Set<String>>> test2(){

        Map<String, Map<Long, Set<String>>> hashMap = new HashMap<>();
        if(!hashMap.containsKey("china")){
            hashMap.put("china", new HashMap<>());
        }
        Map<Long, Set<String>> map = hashMap.get("china");
        if(!map.containsKey(1L)) {
            map.put(1L, new HashSet<>());
        }
        map.get(1L).add("hahah");
        System.out.println("test2 old: " + hashMap.toString());


        hashMap.computeIfAbsent("china", key -> new HashMap<>()).computeIfAbsent(1L, key -> new HashSet<>()).add("hahahaha");
        System.out.println("test2 stream: " + hashMap.toString());

        return hashMap;
    }


}
