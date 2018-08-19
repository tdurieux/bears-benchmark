/*
 * Copyright 2017 Red Hat Inc.
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
 */
package io.enmasse.systemtest.amqp;

import io.enmasse.systemtest.Endpoint;
import io.vertx.proton.ProtonClientOptions;
import io.vertx.proton.ProtonQoS;

public class AmqpConnectOptions {
    private Endpoint endpoint;
    private TerminusFactory terminusFactory;
    private ProtonQoS qos;
    private ProtonClientOptions protonClientOptions;
    private String username;
    private String password;

    public AmqpConnectOptions() {
    }

    public AmqpConnectOptions(AmqpConnectOptions options) {
        this.endpoint = options.endpoint;
        this.terminusFactory = options.terminusFactory;
        this.qos = options.qos;
        this.protonClientOptions = options.protonClientOptions;
        this.username = options.username;
        this.password = options.password;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public AmqpConnectOptions setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public TerminusFactory getTerminusFactory() {
        return terminusFactory;
    }

    public AmqpConnectOptions setTerminusFactory(TerminusFactory terminusFactory) {
        this.terminusFactory = terminusFactory;
        return this;
    }

    public ProtonQoS getQos() {
        return qos;
    }

    public AmqpConnectOptions setQos(ProtonQoS qos) {
        this.qos = qos;
        return this;
    }

    public ProtonClientOptions getProtonClientOptions() {
        return protonClientOptions;
    }

    public AmqpConnectOptions setProtonClientOptions(ProtonClientOptions protonClientOptions) {
        this.protonClientOptions = protonClientOptions;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public AmqpConnectOptions setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AmqpConnectOptions setPassword(String password) {
        this.password = password;
        return this;
    }

}
