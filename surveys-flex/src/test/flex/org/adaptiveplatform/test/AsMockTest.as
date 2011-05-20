package org.adaptiveplatform.test {
import org.adaptiveplatform.surveys.application.*;

import asmock.framework.SetupResult;
import asmock.integration.flexunit.IncludeMocksRule;

import org.flexunit.asserts.assertEquals;

public class AsMockTest {

    [Rule]
    public var mocks:IncludeMocksRule = new IncludeMocksRule([
        AuthenticationService
    ]);

    [Test]
    public function asMockShouldWork():void {
        var service:AuthenticationService =
                AuthenticationService(mocks.repository.createStub(AuthenticationService));

        SetupResult.forCall(service.inRole("USER")).returnValue(true);
        mocks.repository.replay(service);

        assertEquals(true, service.inRole("USER"));
    }
}
}
