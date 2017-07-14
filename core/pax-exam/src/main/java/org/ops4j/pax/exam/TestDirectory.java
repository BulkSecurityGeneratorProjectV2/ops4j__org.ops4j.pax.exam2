package org.ops4j.pax.exam;

import java.net.URI;

/**
 * A singleton directory which keeps track of all tests in a suite. A test is a single method of a
 * given test class in a given configuration. In particular, individual test methods may occur
 * multiple times in different configurations.
 * <p>
 * A test address is a unique identifier for a test. A probe lists the addresses of all tests it
 * contains.
 * <p>
 * This directory maps test addresses to test instantiation instructions which enable a probe
 * invoker to actually build a test method invocation from the test address.
 * <p>
 * In addition, this directory contains an access point URI which may be used for accessing a remote
 * container. At the moment, this is only used by the Servlet Bridge in Java EE mode: In this case,
 * the access point corresponds to the context root of the probe web application.
 *
 * @author Harald Wellmann
 * @since 3.0.0
 */
public class TestDirectory {

    private static final TestDirectory INSTANCE = new TestDirectory();

    private URI accessPoint;

    private TestDirectory() {
    }

    public static TestDirectory getInstance() {
        return INSTANCE;
    }

    public URI getAccessPoint() {
        return accessPoint;
    }

    public void setAccessPoint(URI accessPoint) {
        this.accessPoint = accessPoint;
    }
}
