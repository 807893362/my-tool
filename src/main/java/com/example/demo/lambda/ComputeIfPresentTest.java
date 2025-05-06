package com.example.demo.lambda;

import java.util.HashMap;

/**
 * computeIfPresent() 方法对 hashMap 中指定 key 的值进行重新计算，前提是该 key 存在于 hashMap 中。
 * - key - 键
 * - remappingFunction - 重新映射函数，用于重新计算值
 * - 如果 key 对应的 value 不存在，则返回该 null，如果存在，则返回通过 remappingFunction 重新计算后的值。
 */
public class ComputeIfPresentTest {

    public static void main(String[] args) {
        HashMap<String, Long> hashMap = new HashMap<>();
        hashMap.put("china", 1L);
        if(hashMap.containsKey("china")) {
            hashMap.put("china", hashMap.get("china") + 10);
        }
        System.out.println("test1 old: " + hashMap.toString());

        // after JDK1.8
        hashMap.computeIfPresent("china", (k, v) -> v + 10);
        System.out.println("test1 stream: " + hashMap.toString());
    }


}
