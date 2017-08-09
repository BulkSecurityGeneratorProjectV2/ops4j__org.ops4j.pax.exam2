/*
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
package org.ops4j.pax.exam.container.eclipse.impl.sources.p2repository;

import java.io.IOException;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import org.ops4j.pax.exam.container.eclipse.EclipseBundleOption;
import org.ops4j.pax.exam.container.eclipse.impl.ArtifactInfo;
import org.ops4j.pax.exam.container.eclipse.impl.BundleArtifactInfo;
import org.ops4j.pax.exam.container.eclipse.impl.sources.AbstractEclipseBundleSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The bundles of a P2 repro
 * 
 * @author Christoph Läubrich
 *
 */
public class P2BundleSource extends AbstractEclipseBundleSource<P2Bundle> {

    private static final Logger LOG = LoggerFactory.getLogger(P2BundleSource.class);

    @Override
    protected EclipseBundleOption getArtifact(BundleArtifactInfo<P2Bundle> info)
        throws IOException {
        return new P2EclipseBundleOption(info);
    }

    public void addBundles(String reproName, Iterable<ArtifactInfo<URL>> artifacts) {
        for (ArtifactInfo<URL> info : artifacts) {
            if (add(new LazyBundleArtifactInfo(info, new P2Bundle(info.getContext(), reproName)))) {
                LOG.debug("Add bundle {}:{}:{}",
                    new Object[] { info.getId(), info.getVersion(), reproName });
            }
        }
    }

    private static final class LazyBundleArtifactInfo extends BundleArtifactInfo<P2Bundle> {

        private boolean fragment;
        private boolean singleton;
        private boolean loaded;

        public LazyBundleArtifactInfo(ArtifactInfo<URL> info, P2Bundle p2Bundle) {
            super(info.getId(), info.getVersion(), false, false, p2Bundle);
        }

        @Override
        public boolean isFragment() {
            load();
            return fragment;
        }

        private synchronized void load() {
            if (!loaded) {
                URL url = getContext().getUrl();
                LOG.debug("Load bundle {}...", url);
                try {
                    try (JarInputStream jar = new JarInputStream(P2Cache.open(url))) {
                        Manifest mf = jar.getManifest();
                        if (mf != null) {
                            Attributes manifest = mf.getMainAttributes();
                            fragment = isFragment(manifest);
                            singleton = isSingleton(manifest);
                        }
                    }
                }
                catch (IOException e) {
                    throw new RuntimeException("loading bundle info from url " + url + " failed",
                        e);
                }
                loaded = true;
            }
        }

        @Override
        public boolean isSingleton() {
            load();
            return singleton;
        }

    }

}
