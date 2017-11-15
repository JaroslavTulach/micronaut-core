/*
 * Copyright 2017 original authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package org.particleframework.management.endpoint.refresh;

import org.particleframework.context.env.Environment;
import org.particleframework.context.event.ApplicationEventPublisher;
import org.particleframework.management.endpoint.Endpoint;
import org.particleframework.management.endpoint.Write;
import org.particleframework.runtime.context.scope.refresh.RefreshEvent;

import java.util.Map;
import java.util.Set;

/**
 * <p>Exposes an {@link Endpoint} to refresh application state via a {@link RefreshEvent}</p>
 *
 * @see org.particleframework.runtime.context.scope.refresh.RefreshScope
 * @see RefreshEvent
 * @see org.particleframework.runtime.context.scope.Refreshable
 *
 * @author Graeme Rocher
 * @since 1.0
 */
@Endpoint("refresh")
public class RefreshEndpoint {

    private final Environment environment;
    private final ApplicationEventPublisher eventPublisher;

    public RefreshEndpoint(Environment environment, ApplicationEventPublisher eventPublisher) {
        this.environment = environment;
        this.eventPublisher = eventPublisher;
    }

    @Write
    public String[] refresh() {
        Map<String, Object> changes = environment.refreshAndDiff();
        if(!changes.isEmpty()) {
            eventPublisher.publishEvent(new RefreshEvent(changes));
        }
        Set<String> keys = changes.keySet();
        return keys.toArray(new String[keys.size()]);
    }
}
