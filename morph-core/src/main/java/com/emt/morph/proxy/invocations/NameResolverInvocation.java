package com.emt.morph.proxy.invocations;

import com.emt.morph.AbstractListener;
import com.emt.morph.AuthorityListenerProvider;
import com.emt.morph.LoadBalancer;
import com.emt.morph.ImmutableRemoteAddressGroup;
import com.emt.morph.exception.MorphException;
import com.emt.morph.proxy.Invocation;
import com.emt.morph.proxy.InvocationSession;
import com.emt.morph.http.ClientHttpMethod;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.Iterator;

public class NameResolverInvocation implements Invocation {

   private final AuthorityListenerProvider authorityListenerProvider;
   private final LoadBalancer loadBalancer;

   public NameResolverInvocation(AuthorityListenerProvider authorityListenerProvider, LoadBalancer loadBalancer) {
      this.authorityListenerProvider = authorityListenerProvider;
      this.loadBalancer = loadBalancer;
   }

   @Override
   public Object invoke(Object callee, Method method, Object[] args, InvocationSession chain) throws Throwable {
      URI baseUri = chain.getContext().getUri();

      AbstractListener abstractListener = authorityListenerProvider.getListener(baseUri);

      URI selectedUri;

      if (abstractListener == null) {

         selectedUri = baseUri;

      } else {
         Iterator<ImmutableRemoteAddressGroup> remoteAddressGroups = abstractListener.getRemoteAddressGroups();

         ImmutableRemoteAddressGroup remoteAddressGroup = loadBalancer.select(remoteAddressGroups);

         selectedUri = new URIBuilder(baseUri)
                 .setScheme(remoteAddressGroup.getScheme().toString())
                 .setHost(remoteAddressGroup.getHost())
                 .setPort(remoteAddressGroup.getPort())
                 .build();
      }

      chain.getContext()
              .setSelectedUri(selectedUri);

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
            throw new MorphException("wtf: " + clientHttpMethod.toString());
      }

      chain.getContext()
              .setHttpRequestBase(httpRequestBase);

      chain.invoke(callee, method, args);

      return null;
   }


}
