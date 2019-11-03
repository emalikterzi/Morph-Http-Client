package com.emtdev.morph.proxy.invocations;

import com.emtdev.morph.AbstractListener;
import com.emtdev.morph.AuthorityListenerProvider;
import com.emtdev.morph.ImmutableRemoteAddressGroup;
import com.emtdev.morph.LoadBalancer;
import com.emtdev.morph.exception.MorphException;
import com.emtdev.morph.http.ClientHttpMethod;
import com.emtdev.morph.meta.ImmutableExecutionMeta;
import com.emtdev.morph.proxy.Invocation;
import com.emtdev.morph.proxy.InvocationSession;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class NameResolverInvocation implements Invocation {

   private final AuthorityListenerProvider authorityListenerProvider;
   private final List<LoadBalancer> loadBalancerList;

   public NameResolverInvocation(AuthorityListenerProvider authorityListenerProvider, List<LoadBalancer> loadBalancerList) {
      this.authorityListenerProvider = authorityListenerProvider;
      this.loadBalancerList = loadBalancerList;
   }

   @Override
   public Object invoke(Object callee, Method method, Object[] args, InvocationSession chain) throws Throwable {
      URI baseUri = chain.getContext().getUri();

      AbstractListener abstractListener = authorityListenerProvider.getListener(baseUri);

      URI selectedUri;

      if (abstractListener == null) {

         selectedUri = baseUri;

      } else {

         ImmutableExecutionMeta immutableExecutionMeta = chain.getContext().getImmutableExecutionMeta();
         Iterator<ImmutableRemoteAddressGroup> remoteAddressGroups = abstractListener.getRemoteAddressGroups();
         LoadBalancer loadBalancer = findSuitableLoadBalancer(immutableExecutionMeta);
         ImmutableRemoteAddressGroup remoteAddressGroup = loadBalancer.select(remoteAddressGroups);

         selectedUri = new URIBuilder(baseUri)
                 .setScheme(remoteAddressGroup.getScheme().toString())
                 .setHost(remoteAddressGroup.getHost())
                 .setPort(remoteAddressGroup.getPort())
                 .build();

         chain.getContext()
                 .setSelectedLoadBalancer(loadBalancer);

         chain.getContext()
                 .setSelectedAddressGroup(remoteAddressGroup);
      }


      ClientHttpMethod clientHttpMethod = chain.getContext()
              .getImmutableExecutionMeta().getClientHttpMethod();

      HttpRequestBase httpRequestBase;

      switch (clientHttpMethod) {
         case GET:
            httpRequestBase = new HttpGet(selectedUri);
            break;
         case PUT:
            httpRequestBase = new HttpPut(selectedUri);
            break;
         case POST:
            httpRequestBase = new HttpPost(selectedUri);
            break;
         case DELETE:
            httpRequestBase = new HttpDelete(selectedUri);
            break;
         default:
            throw new MorphException("Invalid Http Method: " + clientHttpMethod.toString());
      }

      chain.getContext()
              .setHttpRequestBase(httpRequestBase);

      chain.invoke(callee, method, args);
      return null;
   }

   private LoadBalancer findSuitableLoadBalancer(ImmutableExecutionMeta immutableExecutionMeta) {
      Class tClass = immutableExecutionMeta.getLoadBalancer();
      Optional<LoadBalancer> loadBalancer = this.loadBalancerList.stream().filter(x -> x.getClass().equals(tClass))
              .findFirst();

      if (!loadBalancer.isPresent())
         throw new MorphException("No suitable LoadBalancer For Class :  " + tClass.getName());

      return loadBalancer.get();
   }


}
