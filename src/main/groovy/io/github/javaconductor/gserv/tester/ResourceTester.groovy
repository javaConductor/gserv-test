package io.github.javaconductor.gserv.tester

import io.github.javaconductor.gserv.GServFactory
import io.github.javaconductor.gserv.GServResource
import io.github.javaconductor.gserv.configuration.GServConfig

/**
 * Created by lcollins on 12/30/2014.
 */
class ResourceTester  extends InstanceTester{
    def ResourceTester(GServResource resource) {
        super(new GServFactory().createGServConfig(resource.actions))
    }
}
