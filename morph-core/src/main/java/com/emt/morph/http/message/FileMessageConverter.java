package com.emt.morph.http.message;

import com.emt.morph.annotation.api.message.api.FormBodyPartProvider;
import com.emt.morph.http.message.api.HttpMessageConverter;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.FormBodyPartBuilder;
import org.apache.http.entity.mime.content.FileBody;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URLConnection;

public class FileMessageConverter implements HttpMessageConverter<File, Object>, FormBodyPartProvider<FormBodyPart, File> {

    @Override
    public HttpEntity write(File file) {
        ContentType type = resolveContentType(file.getName());
        if (type == null) {
            type = ContentType.DEFAULT_BINARY;
        }
        return new FileEntity(file, type);
    }

    @Override
    public Object read(Method method, ContentType type, InputStream inputStream) throws Exception {
        return null;
    }

    @Override
    public String[] supportsMimeType() {
        return new String[0];
    }

    @Override
    public boolean supportsRead() {
        return false;
    }

    @Override
    public FormBodyPart createFormBodyPart(String name, File file) {
        ContentType type = resolveContentType(file.getName());

        if (type == null) {
            type = ContentType.DEFAULT_BINARY;
        }

        FileBody fileBody = new FileBody(file, type, file.getName());

        return FormBodyPartBuilder
                .create()
                .setName(name)
                .setBody(fileBody)
                .build();
    }

    private ContentType resolveContentType(String fileName) {
        try {
            String mimeType = URLConnection.guessContentTypeFromName(fileName);
            return ContentType.create(mimeType);
        } catch (Exception e) {
            return null;
        }
    }
}
