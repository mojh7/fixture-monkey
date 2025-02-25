/*
 * Fixture Monkey
 *
 * Copyright (c) 2021-present NAVER Corp.
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

package com.navercorp.fixturemonkey.api.generator;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.navercorp.fixturemonkey.api.property.DefaultPropertyGenerator;
import com.navercorp.fixturemonkey.api.property.Property;
import com.navercorp.fixturemonkey.api.property.PropertyGenerator;
import com.navercorp.fixturemonkey.api.property.PropertyNameResolver;
import com.navercorp.fixturemonkey.api.property.RootProperty;
import com.navercorp.fixturemonkey.api.type.TypeReference;

class ArbitraryPropertyTest {
	private static final PropertyGenerator DEFAULT_PROPERTY_GENERATOR = new DefaultPropertyGenerator();

	@Test
	void root() {
		// given
		TypeReference<GenericSample<String>> typeReference = new TypeReference<GenericSample<String>>() {
		};
		RootProperty rootProperty = new RootProperty(typeReference.getAnnotatedType());

		// when
		ObjectProperty actual = new ObjectProperty(
			rootProperty,
			PropertyNameResolver.IDENTITY,
			null
		);

		then(actual.isRoot()).isTrue();
		then(actual.getProperty()).isEqualTo(rootProperty);
	}

	@SuppressWarnings("OptionalGetWithoutIsPresent")
	@Test
	void getResolvePropertyName() {
		// given
		TypeReference<GenericSample<String>> typeReference = new TypeReference<GenericSample<String>>() {
		};

		Optional<Property> property = DEFAULT_PROPERTY_GENERATOR.generateChildProperties(
				new RootProperty(typeReference.getAnnotatedType())
			)
			.stream()
			.filter(it -> "name".equals(it.getName()))
			.findFirst();

		// when
		ObjectProperty actual = new ObjectProperty(
			property.get(),
			prop -> "x_" + prop.getName(),
			null
		);

		then(actual.getResolvedPropertyName()).isEqualTo("x_name");
	}

	static class GenericSample<T> {
		private T name;

		public T getName() {
			return this.name;
		}
	}
}
