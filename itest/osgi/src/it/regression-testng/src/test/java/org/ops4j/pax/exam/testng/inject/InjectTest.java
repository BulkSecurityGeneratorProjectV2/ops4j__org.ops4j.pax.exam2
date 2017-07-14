/*
 * Copyright 2012 Harald Wellmann
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
package org.ops4j.pax.exam.testng.inject;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import javax.inject.Inject;

import org.ops4j.pax.exam.sample9.pde.HelloService;
import org.ops4j.pax.exam.testng.listener.PaxExam;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(PaxExam.class)
public class InjectTest {

    @Inject
    private HelloService helloService;

    @BeforeMethod
    public void before() {
        System.out.println("**** before Method");
    }

    @Test
    public void getInjectedService() {
        assertNotNull(helloService);
        assertEquals(helloService.getMessage(), "Hello Pax!");
    }

    @Test
    public void method2() {
        System.out.println("method2");
    }
}
