package org.adaptiveplatform.surveys.application {
	import flexunit.framework.TestCase;
	
	import mx.resources.IResourceManager;
	import mx.resources.ResourceBundle;
	import mx.resources.ResourceManager;
	import mx.utils.StringUtil;
	import org.adaptiveplatform.communication.RemoteBusinessExceptionParametersUnmarshaller;
	import org.flexunit.Assert;

	public class RemoteBusinessExceptionParametersUnmarshallerTest{
		[Test]
		public function testSplitNull():void{
			var unmarshaller :RemoteBusinessExceptionParametersUnmarshaller = new RemoteBusinessExceptionParametersUnmarshaller();
			var split:Array = unmarshaller.split(null);
			Assert.assertEquals(split.length, 0);
		}
		[Test]
		public function testSplitEmptyString():void{
			var unmarshaller :RemoteBusinessExceptionParametersUnmarshaller = new RemoteBusinessExceptionParametersUnmarshaller();
			var split:Array = unmarshaller.split("");
			Assert.assertEquals(split.length, 0);
		}
		[Test]
		public function testSplitOneStringArguments():void {
			var unmarshaller :RemoteBusinessExceptionParametersUnmarshaller = new RemoteBusinessExceptionParametersUnmarshaller();
			var split:Array = unmarshaller.split("first");
			Assert.assertEquals(split.length, 1);
			Assert.assertEquals(split[0], "first");
		}
		[Test]
		public function testSplitTwoStringArguments():void {
			var unmarshaller :RemoteBusinessExceptionParametersUnmarshaller = new RemoteBusinessExceptionParametersUnmarshaller();
			var split:Array = unmarshaller.split("first`second");
			Assert.assertEquals(2, split.length);
			Assert.assertEquals( "first", split[0]);
			Assert.assertEquals( "second", split[1]);
		}
		[Test]
		public function testSplitCollectionWithOneString():void{
			var unmarshaller :RemoteBusinessExceptionParametersUnmarshaller = new RemoteBusinessExceptionParametersUnmarshaller();
			var split:Array = unmarshaller.split("[first]");
			Assert.assertEquals(1, split.length);
			Assert.assertEquals( 1,split[0].length);
			Assert.assertEquals("first", split[0][0]);
		}
		[Test]
		public function testSplitStringContainingEmptyParameters():void{
			var unmarshaller :RemoteBusinessExceptionParametersUnmarshaller = new RemoteBusinessExceptionParametersUnmarshaller();
			var split:Array = unmarshaller.split("`second`");
			Assert.assertEquals(3,split.length);
			Assert.assertEquals( "", split[0]);
			Assert.assertEquals( "second", split[1]);
			Assert.assertEquals( "", split[2]);
		}
		[Test]
		public function testSplitComplexCollection():void{
			var unmarshaller :RemoteBusinessExceptionParametersUnmarshaller = new RemoteBusinessExceptionParametersUnmarshaller();
			var split:Array = unmarshaller.split("first`[second`third]`fourth");
			Assert.assertEquals(3,split.length);
			Assert.assertEquals( "first", split[0]);
			Assert.assertEquals( 2, split[1].length);
			Assert.assertEquals( "second", split[1][0]);
			Assert.assertEquals( "third", split[1][1]);
			Assert.assertEquals( "fourth", split[2]);
		}
		[Test]
		public function testResourceManager():void{
			var resources:IResourceManager = ResourceManager.getInstance();
			resources.addResourceBundle(new ResourceBundle("pl_PL", "adaptive"));
			
			var s1:String = resources.getString("adaptive", "language");
			trace(s1);
			var s2:String = resources.getString("adaptive", "asdasd");
			trace(s2);
		}
	}
}
