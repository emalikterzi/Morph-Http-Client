package com.emt.morph;

import com.emt.morph.converter.DefaultJsonMessageConverter;
import com.emt.morph.converter.MessageConverter;
import com.emt.morph.exception.MorphException;
import com.emt.morph.impl.DefaultAuthorityListenerProvider;
import com.emt.morph.impl.HttpResponseCloserImpl;
import com.emt.morph.impl.ReflectionExecutionMetaProvider;
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
import com.emt.morph.http.HttpClientProvider;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class MorphClientServiceImpl implements MorphClient {

   private final boolean logInvocationTime;
   private final boolean debug;
   private final LoadBalancer loadBalancer;
   private final List<MessageConverter<?>> messageConverters;
   private final HttpClientProvider httpClientProvider;
   private final PathPropertyResolver pathPropertyResolver;
   private final List<NameResolver> nameResolvers;

   private final AuthorityListenerProvider authorityListenerProvider;
   private final HttpResponseCloser httpResponseCloser;
   private final MethodExecutionMetaProvider methodExecutionMetaProvider;
   private final LibraryPreInitializer preInitializedMethodExecutionMetaProvider;
   private final List<Invocation> invocations;


   MorphClientServiceImpl(boolean logInvocationTime, boolean debug, LoadBalancer loadBalancer, List<MessageConverter<?>> messageConverters, HttpClientProvider httpClientProvider, PathPropertyResolver pathPropertyResolver, List<NameResolver> nameResolvers) {
      this.logInvocationTime = logInvocationTime;
      this.debug = debug;
      this.loadBalancer = loadBalancer;
      this.messageConverters = messageConverters;
      this.httpClientProvider = httpClientProvider;
      this.pathPropertyResolver = pathPropertyResolver;
      this.nameResolvers = nameResolvers;
      this.httpResponseCloser = new HttpResponseCloserImpl();
      this.methodExecutionMetaProvider = new ReflectionExecutionMetaProvider();
      this.preInitializedMethodExecutionMetaProvider = new LibraryPreInitializer(this.methodExecutionMetaProvider);
      this.authorityListenerProvider = new DefaultAuthorityListenerProvider(this.nameResolvers);
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

   private List<Invocation> buildInvocations() {
      Invocation[] invocations = {new TimerInvocation(logInvocationTime),
              new ExecutionMetaInvocation(preInitializedMethodExecutionMetaProvider),
              new RequestPrepareInvocation(pathPropertyResolver),
              new NameResolverInvocation(authorityListenerProvider, loadBalancer),
              new RequestBodyPrepareInvocation(getConverters()),
              new RequestFinalizeInvocation(getConverters(), httpClientProvider, httpResponseCloser),
              new LoggingInvocation(debug)};
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
