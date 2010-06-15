package org.adaptiveplatform.surveys.application {
	import flexunit.framework.TestCase;
	
	import mx.resources.IResourceManager;
	import mx.resources.ResourceBundle;
	import mx.resources.ResourceManager;
	
	public class RemoteBusinessExceptionParametersUnmarshallerTest extends TestCase	{
		
		
		public function testSplitOneStringArguments():void {
			var unmarshaller :RemoteBusinessExceptionParametersUnmarshaller = new RemoteBusinessExceptionParametersUnmarshaller();
			var split:Array = unmarshaller.split("first");
			assertEquals(split.length, 1);
			assertEquals(split[0], "1first");
		}
		
		public function testSplitTwoStringArguments():void {
			var unmarshaller :RemoteBusinessExceptionParametersUnmarshaller = new RemoteBusinessExceptionParametersUnmarshaller();
			var split:Array = unmarshaller.split("first`second");
			assertEquals(2, split.length);
			assertEquals( "first", split[0]);
			assertEquals( "second", split[1]);
		}
		
		public function testSplitCollectionWithOneString():void{
			var unmarshaller :RemoteBusinessExceptionParametersUnmarshaller = new RemoteBusinessExceptionParametersUnmarshaller();
			var split:Array = unmarshaller.split("[first]");
			assertEquals(1, split.length);
			assertEquals( 1,split[0].length);
			assertEquals("first", split[0][0]);
		}
		
		public function testSplitStringContainingEmptyParameters():void{
			var unmarshaller :RemoteBusinessExceptionParametersUnmarshaller = new RemoteBusinessExceptionParametersUnmarshaller();
			var split:Array = unmarshaller.split("`second`");
			assertEquals(3,split.length);
			assertEquals( "", split[0]);
			assertEquals( "second", split[1]);
			assertEquals( "", split[2]);
		}
		
		public function testSplitComplexCollection():void{
			var unmarshaller :RemoteBusinessExceptionParametersUnmarshaller = new RemoteBusinessExceptionParametersUnmarshaller();
			var split:Array = unmarshaller.split("first`[second`third]`fourth");
			assertEquals(3,split.length);
			assertEquals( "first", split[0]);
			assertEquals( 2, split[1].length);
			assertEquals( "second", split[1][0]);
			assertEquals( "third", split[1][1]);
			assertEquals( "fourth", split[2]);
		}
		
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
