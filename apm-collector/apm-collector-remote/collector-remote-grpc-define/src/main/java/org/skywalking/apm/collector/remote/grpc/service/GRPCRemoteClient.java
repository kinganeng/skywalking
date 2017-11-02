/*
 * Copyright 2017, OpenSkywalking Organization All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Project repository: https://github.com/OpenSkywalking/skywalking
 */

package org.skywalking.apm.collector.remote.grpc.service;

import io.grpc.stub.StreamObserver;
import org.skywalking.apm.collector.remote.RemoteDataMapping;
import org.skywalking.apm.collector.remote.RemoteDataMappingContainer;
import org.skywalking.apm.collector.remote.grpc.proto.RemoteData;
import org.skywalking.apm.collector.remote.grpc.proto.RemoteMessage;
import org.skywalking.apm.collector.core.data.Data;
import org.skywalking.apm.collector.remote.service.RemoteClient;

/**
 * @author peng-yongsheng
 */
public class GRPCRemoteClient implements RemoteClient {

    private final RemoteDataMappingContainer container;
    private final StreamObserver<RemoteMessage> streamObserver;

    public GRPCRemoteClient(RemoteDataMappingContainer container, StreamObserver<RemoteMessage> streamObserver) {
        this.container = container;
        this.streamObserver = streamObserver;
    }

    @Override public void send(String roleName, Data data, RemoteDataMapping mapping) {
        RemoteData remoteData = (RemoteData)container.get(mapping.ordinal()).serialize(data);
        RemoteMessage.Builder builder = RemoteMessage.newBuilder();
        builder.setWorkerRole(roleName);
        builder.setRemoteData(remoteData);

        streamObserver.onNext(builder.build());
    }
}