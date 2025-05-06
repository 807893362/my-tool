package com.example.demo.lombok;

/**
 * @Getter 和 @Setter
 * @ToString
 * @EqualsAndHashCode
 * @NoArgsConstructor
 * @AllArgsConstructor
 * - 通过 @AllArgsConstructor(access = AccessLevel.PROTECTED) , 可以指定构造器的访问权限
 * - @AllArgsConstructor(access = AccessLevel.PROTECTED, staticName = "test") 生成一个静态生成该类的方法
 * @Slf4j
 * @Data
 * - 相当于同时使用了@Getter、@Setter、@EqualsAndHashCode和@ToString + @RequiredArgsConstructor注解。
 * @Builder
 * - 可以自动生成Builder模式的代码。
 * @RequiredArgsConstructor
 * - 注解则会将类中所有带有@NonNull注解 / org.jetbrains.annotations.NotNull注解的或者带有final修饰的成员变量生成对应的构造方法
 * - 如果所有字段都没有@NonNull注解，那效果同@NoArgsConstructor
 * @Accessors
 * - 这个注解要搭配@Getter与@Setter使用，用来修改默认的setter与getter方法的形式。
 * -- fluent 属性 : 链式的形式 这个特别好用，方法连缀越来越方便了
 * -- chain 属性 : 流式的形式（若无显示指定chain的值，也会把chain设置为true）
 * -- prefix 属性 : 生成指定前缀的属性的getter与setter方法，并且生成的getter与setter方法时会去除前缀
 * @Value
 * - @Value注解和@Data类似，区别在于它会把所有成员变量默认定义为private final修饰，并且不会生成set方法。
 * @Delegate
 * - 被@Delegate注释的属性，会把这个属性类型的公有非静态方法合到当前类
 * @Singular
 * - 只能配合@Builder注解使用, 会对集合生成单个插入的方法
 */
public class ModelLombokTest {
}


