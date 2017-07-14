/*
 * Copyright 2011 Harald Wellmann.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.exam.forked;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;
import org.ops4j.pax.exam.ProbeInvoker;
import org.ops4j.pax.exam.TestDescription;
import org.ops4j.pax.exam.TestListener;

public class ProbeInvokerReflectionTest {

    public static class MyProbeInvoker implements ProbeInvoker {

        @Override
        public void runTest(TestDescription description, TestListener listener) {
            // not used
        }

        @Override
        public void runTestClass(String description) {
            System.out.println("runTestClass() invoked");
        }
    }

    @Test
    public void callInvokerByReflection() throws SecurityException, NoSuchMethodException,
        IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Object target = new MyProbeInvoker();
        Method method = target.getClass().getMethod("runTestClass", String.class);
        method.invoke(target, "");
    }
}
