/*
 * Copyright 2009 Toni Menzel.
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
package org.ops4j.pax.exam.raw.extender.intern;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ops4j.pax.exam.Constants;
import org.ops4j.pax.swissbox.extender.BundleObserver;
import org.ops4j.pax.swissbox.extender.ManifestEntry;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Toni Menzel
 * @since Jan 7, 2010
 */
public class TestBundleObserver implements BundleObserver<ManifestEntry> {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(TestBundleObserver.class);
    /**
     * Holder for regression runner registrations per bundle.
     */
    private final Map<Bundle, Registration> registrations;

    /**
     * Constructor.
     */
    TestBundleObserver() {
        registrations = new HashMap<Bundle, Registration>();
    }

    /**
     * Registers specified regression case as a service.
     */
    @Override
    public void addingEntries(final Bundle bundle, final List<ManifestEntry> manifestEntries) {
        String testExec = null;
        for (ManifestEntry manifestEntry : manifestEntries) {

            if (Constants.PROBE_EXECUTABLE.equals(manifestEntry.getKey())) {
                testExec = manifestEntry.getValue();
                break;
            }
        }
        if (testExec != null) {
            Parser parser = new Parser(bundle.getBundleContext(), testExec, manifestEntries);
            for (Probe p : parser.getProbes()) {
                final ServiceRegistration<?> serviceRegistration = p.register(bundle.getBundleContext());
                registrations.put(bundle, new Registration(p, serviceRegistration));
            }

        }
    }

    /**
     * Unregisters prior registered regression for the service.
     */
    @Override
    public void removingEntries(final Bundle bundle, final List<ManifestEntry> manifestEntries) {
        final Registration registration = registrations.remove(bundle);
        if (registration != null) {
            // Do not unregister as below, because the services are automatically unregistered as
            // soon as the bundle
            // for which the services are registered gets stopped
            // registration.serviceRegistration.unregister();
            LOG.debug("Unregistered testcase [" + registration.probe + "." + "]");
        }
    }

    /**
     * Registration holder.
     */
    private static class Registration {

        final Probe probe;

        public Registration(Probe probe, final ServiceRegistration<?> serviceRegistration) {
            this.probe = probe;
        }
    }

}
