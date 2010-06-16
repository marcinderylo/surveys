package org.adaptiveplatform.surveys.application
{
	import flexunit.framework.TestCase;
	
	import mx.utils.StringUtil;

	/**
	 * Test of the StringUtil from the standard library. 
	 */
	public class StringUtilTest extends TestCase {
		public function StringUtilTest() {
		}
		
		public function testSubstituteCanOmmitParameters():void{
			var result:String = StringUtil.substitute("{0} equals {1}", "zero");
			assertEquals(result, "zero equals {1}");
		}
		
		public function testSubstituteSpecifyToManyPrameters():void{
			var result:String = StringUtil.substitute("{0} equals {1}", "one" ,"two","three");
			assertEquals(result, "one equals two");
		}
		
		public function testSubstituteAcceptsArrayAsParameters():void{
			var result:String = StringUtil.substitute("{0}, {1}, {2}", ["one" ,"two","three"]);
			assertEquals(result, "one, two, three");
		}
		
		public function testSubstituteAcceptsArrayOfArraysAsParameters():void{
			var result:String = StringUtil.substitute("{0}, {1}, {2}", ["one" ,["two","two and a half"],"three"]);
			assertEquals(result, "one, two,two and a half, three");
		}
	}
}