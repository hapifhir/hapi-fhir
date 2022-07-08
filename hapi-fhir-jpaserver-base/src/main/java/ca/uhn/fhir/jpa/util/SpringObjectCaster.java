
package ca.uhn.fhir.jpa.util;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
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
 * #L%
 */

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

/**
 * Utility to get the Spring proxy object's target object
 */
public class SpringObjectCaster {

    /**
     * Retrieve the Spring proxy object's target object
     * @param proxy
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T getTargetObject(Object proxy, Class<T> clazz) throws Exception {
        while( (AopUtils.isJdkDynamicProxy(proxy))) {
            return clazz.cast(getTargetObject(((Advised)proxy).getTargetSource().getTarget(), clazz));
        }

        return clazz.cast(proxy);
    }
}
