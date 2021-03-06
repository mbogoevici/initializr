/*
 * Copyright 2012-2016 the original author or authors.
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

package io.spring.initializr.config

import org.junit.Test

import org.springframework.boot.SpringApplication
import org.springframework.mock.env.MockEnvironment

import static org.hamcrest.CoreMatchers.nullValue
import static org.junit.Assert.assertThat
import static org.hamcrest.CoreMatchers.is

/**
 * @author Stephane Nicoll
 */
class CloudfoundryEnvironmentPostProcessorTests {

	private final CloudfoundryEnvironmentPostProcessor postProcessor = new CloudfoundryEnvironmentPostProcessor()
	private final MockEnvironment environment = new MockEnvironment();
	private final SpringApplication application = new SpringApplication()

	@Test
	void parseCredentials() {
		environment.setProperty('vcap.services.stats-index.credentials.uri',
				'http://user:pass@example.com/bar/biz?param=one')
		postProcessor.postProcessEnvironment(environment, application)

		assertThat(environment.getProperty('initializr.stats.elastic.uri'),
				is('http://example.com/bar/biz?param=one'))
		assertThat(environment.getProperty('initializr.stats.elastic.username'), is('user'))
		assertThat(environment.getProperty('initializr.stats.elastic.password'), is('pass'))
	}

	@Test
	void parseNoCredentials() {
		environment.setProperty('vcap.services.stats-index.credentials.uri',
				'http://example.com/bar/biz?param=one')
		postProcessor.postProcessEnvironment(environment, application)

		assertThat(environment.getProperty('initializr.stats.elastic.uri'),
				is('http://example.com/bar/biz?param=one'))
		assertThat(environment.getProperty('initializr.stats.elastic.username'), is(nullValue()))
		assertThat(environment.getProperty('initializr.stats.elastic.password'), is(nullValue()))
	}

	@Test
	void parseNoVcapUri() {
		postProcessor.postProcessEnvironment(environment, application)

		assertThat(environment.getProperty('initializr.stats.elastic.uri'), is(nullValue()))
		assertThat(environment.getProperty('initializr.stats.elastic.username'), is(nullValue()))
		assertThat(environment.getProperty('initializr.stats.elastic.password'), is(nullValue()))
	}

}
