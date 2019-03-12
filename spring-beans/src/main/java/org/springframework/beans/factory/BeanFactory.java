/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.beans.factory;

import org.springframework.beans.BeansException;

/**
 * The root interface for accessing a Spring bean container.
 * This is the basic client view of a bean container;
 * further interfaces such as {@link ListableBeanFactory} and
 * {@link org.springframework.beans.factory.config.ConfigurableBeanFactory}
 * are available for specific purposes.
 *
 * <p>This interface is implemented by objects that hold a number of bean definitions,
 * each uniquely identified by a String name. Depending on the bean definition,
 * the factory will return either an independent instance of a contained object
 * (the Prototype design pattern), or a single shared instance (a superior
 * alternative to the Singleton design pattern, in which the instance is a
 * singleton in the scope of the factory). Which type of instance will be returned
 * depends on the bean factory configuration: the API is the same. Since Spring
 * 2.0, further scopes are available depending on the concrete application
 * context (e.g. "request" and "session" scopes in a web environment).
 *
 * <p>The point of this approach is that the BeanFactory is a central registry
 * of application components, and centralizes configuration of application
 * components (no more do individual objects need to read properties files,
 * for example). See chapters 4 and 11 of "Expert One-on-One J2EE Design and
 * Development" for a discussion of the benefits of this approach.
 *
 * <p>Note that it is generally better to rely on Dependency Injection
 * ("push" configuration) to configure application objects through setters
 * or constructors, rather than use any form of "pull" configuration like a
 * BeanFactory lookup. Spring's Dependency Injection functionality is
 * implemented using this BeanFactory interface and its subinterfaces.
 *
 * <p>Normally a BeanFactory will load bean definitions stored in a configuration
 * source (such as an XML document), and use the {@code org.springframework.beans}
 * package to configure the beans. However, an implementation could simply return
 * Java objects it creates as necessary directly in Java code. There are no
 * constraints on how the definitions could be stored: LDAP, RDBMS, XML,
 * properties file, etc. Implementations are encouraged to support references
 * amongst beans (Dependency Injection).
 *
 * <p>In contrast to the methods in {@link ListableBeanFactory}, all of the
 * operations in this interface will also check parent factories if this is a
 * {@link HierarchicalBeanFactory}. If a bean is not found in this factory instance,
 * the immediate parent factory will be asked. Beans in this factory instance
 * are supposed to override beans of the same name in any parent factory.
 *
 * <p>Bean factory implementations should support the standard bean lifecycle interfaces
 * as far as possible. The full set of initialization methods and their standard order is:<br>
 * 1. BeanNameAware's {@code setBeanName}<br>
 * 2. BeanClassLoaderAware's {@code setBeanClassLoader}<br>
 * 3. BeanFactoryAware's {@code setBeanFactory}<br>
 * 4. EnvironmentAware's {@code setEnvironment}
 * 5. EmbeddedValueResolverAware's {@code setEmbeddedValueResolver}
 * 6. ResourceLoaderAware's {@code setResourceLoader}
 * (only applicable when running in an application context)<br>
 * 7. ApplicationEventPublisherAware's {@code setApplicationEventPublisher}
 * (only applicable when running in an application context)<br>
 * 8. MessageSourceAware's {@code setMessageSource}
 * (only applicable when running in an application context)<br>
 * 9. ApplicationContextAware's {@code setApplicationContext}
 * (only applicable when running in an application context)<br>
 * 10. ServletContextAware's {@code setServletContext}
 * (only applicable when running in a web application context)<br>
 * 11. {@code postProcessBeforeInitialization} methods of BeanPostProcessors<br>
 * 12. InitializingBean's {@code afterPropertiesSet}<br>
 * 13. a custom init-method definition<br>
 * 14. {@code postProcessAfterInitialization} methods of BeanPostProcessors
 *
 * <p>On shutdown of a bean factory, the following lifecycle methods apply:<br>
 * 1. {@code postProcessBeforeDestruction} methods of DestructionAwareBeanPostProcessors
 * 2. DisposableBean's {@code destroy}<br>
 * 3. a custom destroy-method definition
 *
 *
 * 用于访问Spring bean容器的根接口。这是bean容器的基本客户端视图;其他接口，例如{@link ListableBeanFactory}和{@link org.springframework.beans.factory.config.ConfigurableBeanFactory}可用于特定目的。
 * <p>此接口由包含许多bean定义的对象实现，每个bean定义由String名称唯一标识。根据bean定义，工厂将返回包含对象的独立实例（Prototype设计模式）或单个共享实例（Singleton设计模式的高级替代，其中实例是范围中的单例工厂）。
 * 		将返回哪种类型的实例取决于bean工厂配置：API是相同的。从Spring 2.0开始，根据具体的应用程序上下文（例如Web环境中的“请求”和“会话”范围），可以使用更多的范围。
 * <p>这种方法的重点是BeanFactory是应用程序组件的中央注册表，并集中应用程序组件的配置（例如，不再需要单个对象读取属性文件）。有关此方法的优点的讨论，请参见“Expert One-on-One J2EE设计和开发”的第4章和第11章。
 * <p>请注意，依靠依赖注入（“推送”配置）通过setter或构造函数配置应用程序对象通常更好，而不是像BeanFactory查找一样使用任何形式的“拉”配置。 Spring的依赖注入功能是使用这个BeanFactory接口及其子接口实现的。
 * <p>通常，BeanFactory将加载存储在配置源（例如XML文档）中的bean定义，并使用{@code org.springframework.beans}包来配置bean。但是，实现可以直接在Java代码中直接返回它创建的Java对象。
 * 		对如何存储定义没有限制：LDAP，RDBMS，XML，属性文件等。鼓励实现支持bean之间的引用（依赖注入）。
 * <p>与{@link ListableBeanFactory}中的方法相比，如果这是{@link HierarchicalBeanFactory}，此接口中的所有操作也将检查父工厂。如果在此工厂实例中找不到bean，则会询问直接父工厂。
 * 		此工厂实例中的Bean应该在任何父工厂中覆盖同名的Bean。
 * <p> Bean工厂实现应尽可能支持标准bean生命周期接口。完整的初始化方法及其标准顺序是：<br>
 * 1. BeanNameAware的{@code setBeanName} <br>
 * 2. BeanClassLoaderAware的{@code setBeanClassLoader} <br>
 * 3. BeanFactoryAware的{@code setBeanFactory} <br>
 * 4. EnvironmentAware的{@code setEnvironment}
 * 5. EmbeddedValueResolverAware的{@code setEmbeddedValueResolver}
 * 6. ResourceLoaderAware的{@code setResourceLoader}（仅在应用程序上下文中运行时适用）<br>
 * 7. ApplicationEventPublisherAware的{@code setApplicationEventPublisher}（仅在应用程序上下文中运行时适用）<br>
 * 8. MessageSourceAware的{@code setMessageSource}（仅在应用程序上下文中运行时适用）<br>
 * 9. ApplicationContextAware的{@code setApplicationContext}（仅在应用程序上下文中运行时适用）<br>
 * 10. ServletContextAware的{@code setServletContext}（仅在Web应用程序上下文中运行时适用）<br>
 * 11. {@code postProcessBeforeInitialization} BeanPostProcessors方法<br>
 * 12. InitializingBean的{@code afterPropertiesSet} <br>
 * 13.自定义init方法定义<br>
 * 14. BeanPostProcessors的{@code postProcessAfterInitialization}方法
 * <p>关闭bean工厂时，以下生命周期方法适用：<br>
 * 1. DestructionAwareBeanPostProcessors的{@code postProcessBeforeDestruction}方法
 * 2. DisposableBean的{@code destroy} <br>
 * 3.自定义销毁方法定义
 *
 *
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 13 April 2001
 * @see BeanNameAware#setBeanName
 * @see BeanClassLoaderAware#setBeanClassLoader
 * @see BeanFactoryAware#setBeanFactory
 * @see org.springframework.context.ResourceLoaderAware#setResourceLoader
 * @see org.springframework.context.ApplicationEventPublisherAware#setApplicationEventPublisher
 * @see org.springframework.context.MessageSourceAware#setMessageSource
 * @see org.springframework.context.ApplicationContextAware#setApplicationContext
 * @see org.springframework.web.context.ServletContextAware#setServletContext
 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization
 * @see InitializingBean#afterPropertiesSet
 * @see org.springframework.beans.factory.support.RootBeanDefinition#getInitMethodName
 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization
 * @see DisposableBean#destroy
 * @see org.springframework.beans.factory.support.RootBeanDefinition#getDestroyMethodName
 */
public interface BeanFactory {

	/**
	 * Used to dereference a {@link FactoryBean} instance and distinguish it from
	 * beans <i>created</i> by the FactoryBean. For example, if the bean named
	 * {@code myJndiObject} is a FactoryBean, getting {@code &myJndiObject}
	 * will return the factory, not the instance returned by the factory.
	 *
	 * 用于取消引用{@link FactoryBean}实例，并将其与FactoryBean中的bean <i> created </ i>区分开来。 例如，如果名为{@code myJndiObject}的bean是FactoryBean，
	 * 则获取{@code＆myJndiObject}将返回工厂，而不是工厂返回的实例。
	 *
	 */
	String FACTORY_BEAN_PREFIX = "&";


	/**
	 * Return an instance, which may be shared or independent, of the specified bean.
	 * <p>This method allows a Spring BeanFactory to be used as a replacement for the
	 * Singleton or Prototype design pattern. Callers may retain references to
	 * returned objects in the case of Singleton beans.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the name of the bean to retrieve
	 * @return an instance of the bean
	 * @throws NoSuchBeanDefinitionException if there is no bean definition
	 * with the specified name
	 * @throws BeansException if the bean could not be obtained
	 *
	 *
	 * 返回指定bean的实例，该实例可以是共享的或独立的。
	 * <p>此方法允许Spring BeanFactory用作Singleton或Prototype设计模式的替代品。 在Singleton bean的情况下，调用者可以保留对返回对象的引用。
	 * <p>将别名转换回相应的规范bean名称。 将询问父工厂是否在此工厂实例中找不到bean。
	 * @param 命名要检索的bean的名称
	 * @return bean的一个实例
	 *
	 */
	Object getBean(String name) throws BeansException;

	/**
	 * Return an instance, which may be shared or independent, of the specified bean.
	 * <p>Behaves the same as {@link #getBean(String)}, but provides a measure of type
	 * safety by throwing a BeanNotOfRequiredTypeException if the bean is not of the
	 * required type. This means that ClassCastException can't be thrown on casting
	 * the result correctly, as can happen with {@link #getBean(String)}.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the name of the bean to retrieve
	 * @param requiredType type the bean must match. Can be an interface or superclass
	 * of the actual class, or {@code null} for any match. For example, if the value
	 * is {@code Object.class}, this method will succeed whatever the class of the
	 * returned instance.
	 * @return an instance of the bean
	 * @throws NoSuchBeanDefinitionException if there is no such bean definition
	 * @throws BeanNotOfRequiredTypeException if the bean is not of the required type
	 * @throws BeansException if the bean could not be created
	 *
	 * 返回指定bean的实例，该实例可以是共享的或独立的。
	 * <p>与{@link #getBean（String）}的行为相同，但如果bean不是所需类型，则通过抛出BeanNotOfRequiredTypeException来提供类型安全性的度量。
	 * 		这意味着在正确转换结果时不能抛出ClassCastException，就像{@link #getBean（String）}一样。
	 * <p>将别名转换回相应的规范bean名称。 将询问父工厂是否在此工厂实例中找不到bean。
	 * @param 命名要检索的bean的名称
	 * @param requiredType类型必须匹配的bean。 可以是实际类的接口或超类，也可以是{@code null}以用于任何匹配。 例如，如果值为{@code Object.class}，则此方法将成功返回实例的任何类。
	 * @return bean的一个实例
	 *
	 */
	<T> T getBean(String name, Class<T> requiredType) throws BeansException;

	/**
	 * Return the bean instance that uniquely matches the given object type, if any.
	 * <p>This method goes into {@link ListableBeanFactory} by-type lookup territory
	 * but may also be translated into a conventional by-name lookup based on the name
	 * of the given type. For more extensive retrieval operations across sets of beans,
	 * use {@link ListableBeanFactory} and/or {@link BeanFactoryUtils}.
	 * @param requiredType type the bean must match; can be an interface or superclass.
	 * {@code null} is disallowed.
	 * @return an instance of the single bean matching the required type
	 * @throws NoSuchBeanDefinitionException if no bean of the given type was found
	 * @throws NoUniqueBeanDefinitionException if more than one bean of the given type was found
	 * @throws BeansException if the bean could not be created
	 * @since 3.0
	 * @see ListableBeanFactory
	 *
	 *
	 * 返回唯一匹配给定对象类型的bean实例（如果有）。
	 * <p>此方法进入{@link ListableBeanFactory}按类型查找区域，但也可以根据给定类型的名称转换为传统的按名称查找。 要跨越多组bean进行更广泛的检索操作，请使用{@link ListableBeanFactory}和/或{@link BeanFactoryUtils}。
	 * @param requiredType类型必须匹配; 可以是接口或超类。
	 * {@code null}是不允许的。
	 * @return 匹配所需类型的单个bean的实例
	 *
	 */
	<T> T getBean(Class<T> requiredType) throws BeansException;

	/**
	 * Return an instance, which may be shared or independent, of the specified bean.
	 * <p>Allows for specifying explicit constructor arguments / factory method arguments,
	 * overriding the specified default arguments (if any) in the bean definition.
	 * @param name the name of the bean to retrieve
	 * @param args arguments to use when creating a prototype using explicit arguments
	 * @return an instance of the bean
	 * @throws NoSuchBeanDefinitionException if there is no such bean definition
	 * @throws BeanDefinitionStoreException if arguments have been given but
	 * the affected bean isn't a prototype
	 * @throws BeansException if the bean could not be created
	 * @since 2.5
	 *
	 * 返回指定bean的实例，该实例可以是共享的或独立的。
	 * <p>允许指定显式构造函数参数/工厂方法参数，覆盖bean定义中指定的默认参数（如果有）。
	 * @param 命名要检索的bean的名称
	 * @param args在使用显式参数创建原型时使用的参数
	 *
	 */
	Object getBean(String name, Object... args) throws BeansException;

	/**
	 * Does this bean factory contain a bean definition or externally registered singleton
	 * instance with the given name?
	 * <p>If the given name is an alias, it will be translated back to the corresponding
	 * canonical bean name.
	 * <p>If this factory is hierarchical, will ask any parent factory if the bean cannot
	 * be found in this factory instance.
	 * <p>If a bean definition or singleton instance matching the given name is found,
	 * this method will return {@code true} whether the named bean definition is concrete
	 * or abstract, lazy or eager, in scope or not. Therefore, note that a {@code true}
	 * return value from this method does not necessarily indicate that {@link #getBean}
	 * will be able to obtain an instance for the same name.
	 * @param name the name of the bean to query
	 * @return whether a bean with the given name is present
	 *
	 * 此bean工厂是否包含具有给定名称的bean定义或外部注册的单例实例？
	 * <p>如果给定的名称是别名，它将被转换回相应的规范bean名称。
	 * <p>如果此工厂是分层的，则会询问任何父工厂是否在此工厂实例中找不到该bean。
	 * <p>如果找到与给定名称匹配的bean定义或单例实例，则此方法将返回{@code true}，无论命名bean定义是具体还是抽象，懒惰或渴望，是否在范围内。
	 * 因此，请注意此方法的{@code true}返回值不一定表示{@link #getBean}将能够获取同名的实例。
	 *
	 */
	boolean containsBean(String name);

	/**
	 * Is this bean a shared singleton? That is, will {@link #getBean} always
	 * return the same instance?
	 * <p>Note: This method returning {@code false} does not clearly indicate
	 * independent instances. It indicates non-singleton instances, which may correspond
	 * to a scoped bean as well. Use the {@link #isPrototype} operation to explicitly
	 * check for independent instances.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the name of the bean to query
	 * @return whether this bean corresponds to a singleton instance
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @see #getBean
	 * @see #isPrototype
	 *
	 *
	 * 这个bean是共享单例吗？ 也就是说，{@link #getBean}总会返回相同的实例吗？
	 * <p>注意：此方法返回{@code false}并不能清楚地表明独立实例。 它表示非单例实例，也可以对应于范围内的bean。 使用{@link #isPrototype}操作显式检查独立实例。
	 * <p>将别名转换回相应的规范bean名称。 将询问父工厂是否在此工厂实例中找不到bean。
	 * @param 命名要查询的bean的名称
	 * @return 这个bean是否对应于单例实例
	 *
	 */
	boolean isSingleton(String name) throws NoSuchBeanDefinitionException;

	/**
	 * Is this bean a prototype? That is, will {@link #getBean} always return
	 * independent instances?
	 * <p>Note: This method returning {@code false} does not clearly indicate
	 * a singleton object. It indicates non-independent instances, which may correspond
	 * to a scoped bean as well. Use the {@link #isSingleton} operation to explicitly
	 * check for a shared singleton instance.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the name of the bean to query
	 * @return whether this bean will always deliver independent instances
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @since 2.0.3
	 * @see #getBean
	 * @see #isSingleton
	 *
	 * 这豆是原型吗？ 也就是说，{@link #getBean}总会返回独立实例吗？
	 * <p>注意：返回{@code false}的此方法不能清楚地表示单个对象。 它表示非独立实例，也可以对应于范围内的bean。 使用{@link #isSingleton}操作显式检查共享单例实例。
	 * <p>将别名转换回相应的规范bean名称。 将询问父工厂是否在此工厂实例中找不到bean。
	 * @param 命名要查询的bean的名称
	 * @return 这个bean是否总是提供独立的实例
	 *
	 */
	boolean isPrototype(String name) throws NoSuchBeanDefinitionException;

	/**
	 * Check whether the bean with the given name matches the specified type.
	 * More specifically, check whether a {@link #getBean} call for the given name
	 * would return an object that is assignable to the specified target type.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the name of the bean to query
	 * @param targetType the type to match against
	 * @return {@code true} if the bean type matches,
	 * {@code false} if it doesn't match or cannot be determined yet
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @since 2.0.1
	 * @see #getBean
	 * @see #getType
	 *
	 *
	 * 检查具有给定名称的bean是否与指定的类型匹配。 更具体地说，检查对给定名称的{@link #getBean}调用是否将返回可分配给指定目标类型的对象。
	 * <p>将别名转换回相应的规范bean名称。 将询问父工厂是否在此工厂实例中找不到bean。
	 * @param 命名要查询的bean的名称
	 * @param targetType要匹配的类型
	 * @return {@code true}如果bean类型匹配，{@ code false}如果它不匹配或无法确定
	 *
	 */
	boolean isTypeMatch(String name, Class<?> targetType) throws NoSuchBeanDefinitionException;

	/**
	 * Determine the type of the bean with the given name. More specifically,
	 * determine the type of object that {@link #getBean} would return for the given name.
	 * <p>For a {@link FactoryBean}, return the type of object that the FactoryBean creates,
	 * as exposed by {@link FactoryBean#getObjectType()}.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the name of the bean to query
	 * @return the type of the bean, or {@code null} if not determinable
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @since 1.1.2
	 * @see #getBean
	 * @see #isTypeMatch
	 *
	 * 确定具有给定名称的bean的类型。 更具体地说，确定{@link #getBean}将为给定名称返回的对象类型。
	 * <p>对于{@link FactoryBean}，返回FactoryBean创建的对象类型，由{@link FactoryBean＃getObjectType（）}公开。
	 * <p>将别名转换回相应的规范bean名称。 将询问父工厂是否在此工厂实例中找不到bean。
	 * @param 命名要查询的bean的名称
	 * @return bean的类型，如果不可确定，则返回{@code null}
	 *
	 */
	Class<?> getType(String name) throws NoSuchBeanDefinitionException;

	/**
	 * Return the aliases for the given bean name, if any.
	 * All of those aliases point to the same bean when used in a {@link #getBean} call.
	 * <p>If the given name is an alias, the corresponding original bean name
	 * and other aliases (if any) will be returned, with the original bean name
	 * being the first element in the array.
	 * <p>Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the bean name to check for aliases
	 * @return the aliases, or an empty array if none
	 * @see #getBean
	 *
	 * 返回给定bean名称的别名（如果有）。 在{@link #getBean}调用中使用时，所有这些别名都指向同一个bean。
	 * <p>如果给定的名称是别名，则将返回相应的原始bean名称和其他别名（如果有），原始bean名称是数组中的第一个元素。
	 * <p>将询问父工厂是否在此工厂实例中找不到bean。
	 * @param 命名bean名称以检查别名
	 * @return 别名，如果没有，则返回一个空数组
	 *
	 */
	String[] getAliases(String name);

}
