package com.emt.morph.proxy.invocations;

import com.emt.morph.http.message.MessageConverterUtils;
import com.emt.morph.http.message.api.HttpMessageConverter;
import com.emt.morph.proxy.Invocation;
import com.emt.morph.proxy.InvocationSession;
import com.emt.morph.utils.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.ContentType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

public class RequestExecutionInvocation implements Invocation {

    private final MessageConverterUtils messageConverterUtils;
    private final HttpClient httpClient;

    public RequestExecutionInvocation(MessageConverterUtils messageConverterUtils, HttpClient httpClient) {
        this.messageConverterUtils = messageConverterUtils;
        this.httpClient = httpClient;
    }

    @Override
    public Object invoke(Object callee, Method method, Object[] args, InvocationSession chain) throws Throwable {

        if (chain.getContext().getConfig().isDisableHttpRequest()) {
            return null;
        }

        HttpResponse httpResponse = httpClient.execute(chain.getContext().getHttpRequest());

        try {
            HttpEntity httpEntity = httpResponse.getEntity();

            if (httpEntity != null) {
                ContentType type = ContentType.get(httpEntity);

                String mimeType = null;

                if (StringUtils.isNotEmpty(type.getMimeType())) {
                    mimeType = type.getMimeType();
                }

                if (StringUtils.isEmpty(mimeType)) {
                    mimeType = chain.getContext().getMethodAnnotationConfig()
                            .getProduces();
                }

                ReturnStrategy returnStrategy = determineResponseStrategy(method);
                ByteArrayOutputStream byteArrayOutputStream;
                switch (returnStrategy) {
                    case CONVERTER:
                        HttpMessageConverter<?, ?> converter = messageConverterUtils.findSuitableConverter(mimeType);
                        return converter.read(method, type, httpEntity.getContent());
                    case BYTE_ARRAY:
                        byteArrayOutputStream = allocateInputStream(httpEntity.getContent());
                        return byteArrayOutputStream.toByteArray();
                    case INPUT_STREAM:
                        byteArrayOutputStream = allocateInputStream(httpEntity.getContent());
                        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                }
                return null;
            }
        } finally {
            finalizeHttpResponse(httpResponse);
        }

        chain.invoke(callee, method, args);
        return null;
    }


    /**
     * this methods fucks heap space
     *
     * @return
     */
    private ByteArrayOutputStream allocateInputStream(InputStream inputStream) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int len;
        byte[] block = new byte[1024];

        while ((len = inputStream.read(block)) != -1) {
            byteArrayOutputStream.write(block, 0, len);
        }

        return byteArrayOutputStream;
    }

    private void finalizeHttpResponse(HttpResponse httpResponse) {
        if (httpResponse == null) {
            return;
        }
        if (httpResponse instanceof CloseableHttpResponse) {
            CloseableHttpResponse c = (CloseableHttpResponse) httpResponse;

            try {
                c.close();
            } catch (Exception e) {
                //do nothing
            }
        }
    }

    private ReturnStrategy determineResponseStrategy(Method type) {
        return ReturnStrategy.CONVERTER;
    }

    private enum ReturnStrategy {
        CONVERTER,
        INPUT_STREAM,
        BYTE_ARRAY
    }
}
