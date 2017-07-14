/*
 * Copyright 2008 Alin Dreghiciu.
 * Copyright 2009-2011 Toni Menzel.
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
package org.ops4j.pax.exam;

import java.io.InputStream;

/**
 * Management of an OSGi framework that can be used as a integration test container. Each container
 * is also a test target.
 * 
 * When constucting TestContainers, it is good practice to not put the parsing part (from Option[])
 * into the implementation. Instead, make the native container construction really simple and tied
 * to the underlying container.
 * 
 * @author Alin Dreghiciu (adreghiciu@gmail.com)
 * @author Toni Menzel (toni@okidokiteam.com)
 * @author Harald Wellmann
 * @since 0.3.0, December 09, 2008
 */
public interface TestContainer {

    /**
     * Starts the test container.
     * 
     * @return this for fluent api
     * 
     * @throws TimeoutException
     *             - if timeout occured and the test container cannot be started
     */
    TestContainer start();

    /**
     * @param stream
     *            stream the content
     * 
     * @return Bundle ID
     */
    long install(InputStream stream);

    /**
     * @param location
     *            update location of the installed stream. (used on bundle.update())
     * @param stream
     *            stream the content
     * 
     * @return Bundle ID
     */
    long install(String location, InputStream stream);

    /**
     * Installs a probe from the given stream. A test container supports at most one probe at a
     * time. The installed probe (if any) can be uninstalled using {@link #uninstallProbe()}.
     * 
     * @param stream
     *            probe bundle
     * 
     * @return bundle ID of probe bundle, or -1 if not in OSGi mode
     */
    long installProbe(InputStream stream);

    /**
     * Uninstalls the current probe. The container keeps running and can be reused with a new probe.
     * 
     * @throws TestContainerException
     *             if no probe installed.
     */
    void uninstallProbe();

    /**
     * @param address
     *            the target to be called.
     * 
     * @throws TestContainerException
     *             exception
     */
    void call(TestAddress address);

    /**
     * Stops the regression container. Implementations should take care of calling cleanup()
     * internally, too.
     * 
     * @return this for fluent api
     * 
     * @throws TimeoutException
     *             - if timeout occured and the regression container cannot be stopped
     */
    TestContainer stop();

    /**
     * @param description
     */
    void runTest(TestDescription description, TestListener listener);
}
