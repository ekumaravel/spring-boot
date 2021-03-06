/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.actuate.audit;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link AuditEventsJmxEndpointExtension}.
 *
 * @author Andy Wilkinson
 */
public class AuditEventsJmxEndpointExtensionTests {

	private final AuditEventRepository repository = mock(AuditEventRepository.class);

	private final AuditEventsJmxEndpointExtension extension = new AuditEventsJmxEndpointExtension(
			new AuditEventsEndpoint(this.repository));

	private final AuditEvent event = new AuditEvent("principal", "type",
			Collections.singletonMap("a", "alpha"));

	@Test
	public void eventsCreatedAfter() {
		OffsetDateTime now = OffsetDateTime.now();
		given(this.repository.find(null, now.toInstant(), null))
				.willReturn(Collections.singletonList(this.event));
		List<AuditEvent> result = this.extension.eventsAfter(now).getEvents();
		assertThat(result).isEqualTo(Collections.singletonList(this.event));
	}

	@Test
	public void eventsWithPrincipalAndDateAfter() {
		OffsetDateTime now = OffsetDateTime.now();
		given(this.repository.find("Joan", now.toInstant(), null))
				.willReturn(Collections.singletonList(this.event));
		List<AuditEvent> result = this.extension.eventsWithPrincipalAndAfter("Joan", now)
				.getEvents();
		assertThat(result).isEqualTo(Collections.singletonList(this.event));
	}

}
