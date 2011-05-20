package org.adaptiveplatform.test {
import mx.collections.ArrayCollection;

import org.flexunit.asserts.assertEquals;
import org.flexunit.asserts.assertNull;
import org.flexunit.asserts.assertTrue;
import org.mockito.integrations.any;
import org.mockito.integrations.argThat;
import org.mockito.integrations.given;
import org.mockito.integrations.useArgument;
import org.mockito.integrations.verify;

[RunWith("org.mockito.integrations.flexunit4.MockitoClassRunner")]
public class MockitoTest {

    [Mock(type="org.adaptiveplatform.surveys.application.TestClass2")]
    public var mockie:TestClass2;

    [Mock(type="mx.collections.ArrayCollection")]
    public var arrayCollectionMock:ArrayCollection;

    [Test]
    public function shouldVerifyMockInvocation():void {
        // when
        mockie.argumentless();
        // then
        verify().that(mockie.argumentless());
    }

      [Test]
    public function shouldMockMethodInvocation():void {
        // when
         given(mockie.method2()).willReturn(true);
        // then
        assertTrue(mockie.method2());
    }
      [Test]
    public function shouldMockMethodWithArgument():void {
        // when
         given(mockie.method3(any())).willReturn("abc");
        // then
        assertEquals("abc", mockie.method3("dddd"));
    }

    [Test]
    public function testShouldDemoStubbingAPI():void {
        // stubbing - before execution
        given(arrayCollectionMock.getItemAt(1)).willReturn("A");

        // let's pretend the following is executed within SUT:
        // this call returns 'A' because it was stubbed
        assertEquals("A", arrayCollectionMock.getItemAt(1));

        // this call returns null because list.getItemAt(2) was not stubbed
        assertNull(arrayCollectionMock.getItemAt(2));
    }

    public function MockitoTest() {
    }
}
}
