package com.emt.morph.zookeeper.resolver;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.curator.framework.CuratorFramework;

import com.emt.morph.ImmutableRemoteAddressGroup;
import com.emt.morph.exception.MorphException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionLikeType;

public class DefaultZookeeperResolver extends AbstractZookeeperResolver
{

    private final ObjectMapper objectMapper;


    public DefaultZookeeperResolver(CuratorFramework client, String serviceZookeeperPath, ObjectMapper objectMapper)
    {
        super(client, serviceZookeeperPath);
        this.objectMapper = objectMapper;
    }

    @Override
    List<ImmutableRemoteAddressGroup> parseChildData(byte[] childData) throws MorphException
    {
        CollectionLikeType collectionLikeType = objectMapper.getTypeFactory()
                .constructCollectionLikeType(List.class, RemoteAddressGroup.class);
        try
        {
            List<RemoteAddressGroup> remoteAddressGroups = objectMapper.readValue(childData, collectionLikeType);
            return remoteAddressGroups.stream()
                    .map(RemoteAddressGroup::toImmutableRemoteAddressGroup).collect(Collectors.toList());
        }
        catch (IOException e)
        {
            throw new MorphException(e);
        }
    }

}
