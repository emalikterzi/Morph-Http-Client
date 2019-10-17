package com.emt.morph;

import com.emt.morph.converter.DefaultJsonMessageConverter;
import com.emt.morph.converter.MessageConverter;
import com.emt.morph.exception.MorphException;
import com.emt.morph.factory.HttpClientProviderFactory;
import com.emt.morph.factory.LoadBalancerFactory;
import com.emt.morph.factory.NameResolverFactory;
import com.emt.morph.factory.impl.DefaultMorphFactory;
import com.emt.morph.http.HttpClientProvider;
import com.emt.morph.impl.DefaultAuthorityListenerProvider;
import com.emt.morph.impl.DefaultHttpClientProvider;
import com.emt.morph.impl.HttpResponseCloserImpl;
import com.emt.morph.impl.ReflectionExecutionMetaProvider;
import com.emt.morph.impl.SelectFirstLoadBalancer;
import com.emt.morph.proxy.ExecutionHandler;
import com.emt.morph.proxy.Invocation;
import com.emt.morph.proxy.invocations.ExecutionMetaInvocation;
import com.emt.morph.proxy.invocations.LoggingInvocation;
import com.emt.morph.proxy.invocations.NameResolverInvocation;
import com.emt.morph.proxy.invocations.RequestBodyPrepareInvocation;
import com.emt.morph.proxy.invocations.RequestFinalizeInvocation;
import com.emt.morph.proxy.invocations.RequestPrepareInvocation;
import com.emt.morph.proxy.invocations.TimerInvocation;
import com.emt.morph.utils.Asserts;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public final class MorphClientServiceImpl implements MorphClient {

   private final static DefaultMorphFactory DEFAULT_MORPH_FACTORY = new DefaultMorphFactory();

   private final HttpClientProviderFactory httpClientProviderFactory;
   private final LoadBalancerFactory loadBalancerFactory;
   private final NameResolverFactory nameResolverFactory;

   private final Set<Class<? extends NameResolver>> nameResolvers;
   private final Set<Class<? extends LoadBalancer>> loadBalancers;
   private final Set<Class<? extends HttpClientProvider>> httpClientProviders;
   //
   private final List<MessageConverter<?>> messageConverters;
   private final PathPropertyResolver pathPropertyResolver;


   private final AuthorityListenerProvider authorityListenerProvider;
   private final HttpResponseCloser httpResponseCloser;
   private final MethodExecutionMetaProvider methodExecutionMetaProvider;
   private final LibraryPreInitializer preInitializedMethodExecutionMetaProvider;
   private final List<Invocation> invocations;

   private final List<NameResolver> nameResolverInstances = new ArrayList<>();
   private final List<LoadBalancer> loadBalancerInstances = new ArrayList<>();
   private final List<HttpClientProvider> httpClientProviderInstances = new ArrayList<>();
   private final Executor executorPool = Executors.newFixedThreadPool(4);

   public MorphClientServiceImpl(HttpClientProviderFactory httpClientProviderFactory, LoadBalancerFactory loadBalancerFactory, NameResolverFactory nameResolverFactory, Set<Class<? extends NameResolver>> nameResolvers, Set<Class<? extends LoadBalancer>> loadBalancers, Set<Class<? extends HttpClientProvider>> httpClientProviders, List<MessageConverter<?>> messageConverters, PathPropertyResolver pathPropertyResolver) {
      this.httpClientProviderFactory =
              Objects.isNull(httpClientProviderFactory) ? DEFAULT_MORPH_FACTORY : httpClientProviderFactory;
      this.loadBalancerFactory = Objects.isNull(loadBalancerFactory) ? DEFAULT_MORPH_FACTORY : loadBalancerFactory;
      this.nameResolverFactory = Objects.isNull(nameResolverFactory) ? DEFAULT_MORPH_FACTORY : nameResolverFactory;
      this.nameResolvers = nameResolvers;
      this.loadBalancers = loadBalancers;
      this.httpClientProviders = httpClientProviders;
      this.messageConverters = messageConverters;
      this.pathPropertyResolver = pathPropertyResolver;

      this.addDefaults();
      this.factoryOperations();

      this.httpResponseCloser = new HttpResponseCloserImpl();
      this.methodExecutionMetaProvider = new ReflectionExecutionMetaProvider();
      this.preInitializedMethodExecutionMetaProvider = new LibraryPreInitializer(this.methodExecutionMetaProvider);
      this.authorityListenerProvider = new DefaultAuthorityListenerProvider(this.nameResolverInstances, this.executorPool);
      this.invocations = buildInvocations();
      this.startShutDownListener();
   }

   @Override
   @SuppressWarnings("unchecked")

   public <T> T morph(Class<T> tClass) {
      Asserts.notNull(tClass, "Class Must be Provided");
      try {
         preInitializedMethodExecutionMetaProvider.initializeAndValidateMetaData(tClass);
         return (T) Proxy.newProxyInstance(
                 tClass.getClassLoader(),
                 new Class[]{tClass},
                 new ExecutionHandler(invocations));
      } catch (Exception e) {
         throw new MorphException(String.format("Initialize Failed For Class [ %s ]", tClass.getName()), e);
      }
   }

   private void factoryOperations() {
      this.nameResolverInstances.addAll(this.nameResolvers.stream()
              .map(this.nameResolverFactory::createNameResolver).collect(Collectors.toList()));

      this.httpClientProviderInstances.addAll(this.httpClientProviders.stream()
              .map(this.httpClientProviderFactory::createHttpClientProvider).collect(Collectors.toList()));

      this.loadBalancerInstances.addAll(this.loadBalancers.stream()
              .map(this.loadBalancerFactory::createLoadBalancer).collect(Collectors.toList()));
   }

   private void addDefaults() {
      this.httpClientProviders.add(DefaultHttpClientProvider.class);
      this.loadBalancers.add(SelectFirstLoadBalancer.class);
   }

   private List<Invocation> buildInvocations() {
      Invocation[] invocations = {
              new TimerInvocation(),
              new ExecutionMetaInvocation(preInitializedMethodExecutionMetaProvider),
              new RequestPrepareInvocation(pathPropertyResolver),
              new NameResolverInvocation(authorityListenerProvider, loadBalancerInstances),
              new RequestBodyPrepareInvocation(getConverters()),
              new RequestFinalizeInvocation(getConverters(), httpClientProviderInstances, httpResponseCloser),
              new LoggingInvocation()
      };
      return Arrays.asList(invocations);
   }

   private void startShutDownListener() {
      Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdown()));
   }

   private void shutdown() {
      this.httpResponseCloser.closeAll();
   }


   private List<MessageConverter<?>> getConverters() {
      List<MessageConverter<?>> converters = new ArrayList<>(getDefaultConverters());
      converters.addAll(this.messageConverters);
      return converters;
   }


   private List<MessageConverter<?>> getDefaultConverters() {
      List<MessageConverter<?>> messageConverters = new ArrayList<>();
      messageConverters.add(new DefaultJsonMessageConverter());
      return messageConverters;
   }

   @Override
   protected void finalize() throws Throwable {
      this.shutdown();
   }
}
