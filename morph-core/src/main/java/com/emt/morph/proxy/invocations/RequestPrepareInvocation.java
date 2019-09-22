package com.emt.morph.proxy.invocations;

import com.emt.morph.PathPropertyResolver;
import com.emt.morph.meta.ImmutableExecutionMeta;
import com.emt.morph.meta.ImmutableParameterMeta;
import com.emt.morph.proxy.Invocation;
import com.emt.morph.proxy.InvocationSession;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RequestPrepareInvocation implements Invocation {

   private final PathPropertyResolver pathPropertyResolver;

   private final Pattern pattern = Pattern.compile("([$][{][\\w]+([.][\\w]+)*[}])");

   private final Pattern urlPattern = Pattern.compile("([{][\\w]*[}])");

   public RequestPrepareInvocation(PathPropertyResolver pathPropertyResolver) {
      this.pathPropertyResolver = pathPropertyResolver;
   }

   @Override
   public Object invoke(Object callee, Method method, Object[] args, InvocationSession chain) throws Throwable {
      String path = chain.getContext()
              .getImmutableExecutionMeta()
              .getPath();

      ImmutableExecutionMeta immutableExecutionMeta
              = chain.getContext().getImmutableExecutionMeta();


      Matcher matcher = pattern.matcher(path);

      while (matcher.find()) {
         String orgValue = matcher.group();
         String key = orgValue.replace("${", "")
                 .replace("}", "").trim();

         String value = pathPropertyResolver.resolve(key);

         if (value == null) {
            //todo change
            throw new RuntimeException("value not found : " + key);
         }

         path = path.replace(orgValue, value);
      }

      Matcher urlMatcher = urlPattern.matcher(path);

      List<ImmutableParameterMeta> pathParamList = immutableExecutionMeta.getMethodParameterMeta()
              .stream().filter(x -> x.getMetaType().equals(ImmutableParameterMeta.MetaType.PATH_PARAM))
              .collect(Collectors.toList());

      while (urlMatcher.find()) {
         String orgValue = urlMatcher.group();
         String key = orgValue.replace("{", "")
                 .replace("}", "").trim();
         boolean status = false;
         for (ImmutableParameterMeta each : pathParamList) {
            if (each.getValue().equals(key)) {
               Object value = null;
               try {
                  value = args[each.getIndex()];
               } catch (Exception e) {
                  value = each.getDefaultValue();
               }

               if (value == null) {
                  value = each.getDefaultValue();
               }

               if (value == null)
                  throw new RuntimeException("query param cannot be null");

               path = path.replace(orgValue, value.toString());
               status = true;

            }
         }
         if (!status)
            throw new RuntimeException("path param not  found");
      }


      List<ImmutableParameterMeta> queryParams = immutableExecutionMeta.getMethodParameterMeta()
              .stream().filter(x -> x.getMetaType().equals(ImmutableParameterMeta.MetaType.QUERY_PARAM))
              .collect(Collectors.toList());

      StringBuilder pathBuilder = new StringBuilder(path);

      for (
              int i = 0; i < queryParams.size(); i++) {
         ImmutableParameterMeta queryParam = queryParams.get(i);
         Object value;
         try {
            value = args[queryParam.getIndex()];
         } catch (Exception e) {
            value = queryParam.getDefaultValue();
         }

         if (value == null) {
            value = queryParam.getDefaultValue();
         }

         if (value == null)
            throw new RuntimeException("query param cannot be null");

         if (i == 0) {
            pathBuilder.append("?");
         } else
            pathBuilder.append("&");

         pathBuilder.append(queryParam.getValue())
                 .append("=")
                 .append(value);

      }
      URI uri = URI.create(pathBuilder.toString());
      chain.getContext()
              .setUri(uri);


      chain.invoke(callee, method, args);
      return null;
   }

}
