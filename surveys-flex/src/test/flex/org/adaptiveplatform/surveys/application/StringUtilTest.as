package org.adaptiveplatform.surveys.application
{
	import flexunit.framework.TestCase;
	import org.flexunit.Assert;
	import mx.utils.StringUtil;

	/**
	 * Test of the StringUtil from the standard library. 
	 */
	public class StringUtilTest {
		public function StringUtilTest() {
		}
		[Test]
		public function testSubstituteCanOmmitParameters():void{
			var result:String = StringUtil.substitute("{0} equals {1}", "zero");
			Assert.assertEquals(result, "zero equals {1}");
		}
		[Test]
		public function testSubstituteSpecifyToManyPrameters():void{
			var result:String = StringUtil.substitute("{0} equals {1}", "one" ,"two","three");
			Assert.assertEquals(result, "one equals two");
		}
		[Test]
		public function testSubstituteAcceptsArrayAsParameters():void{
			var result:String = StringUtil.substitute("{0}, {1}, {2}", ["one" ,"two","three"]);
			Assert.assertEquals(result, "one, two, three");
		}
		[Test]
		public function testSubstituteAcceptsArrayOfArraysAsParameters():void{
			var result:String = StringUtil.substitute("{0}, {1}, {2}", ["one" ,["two","two and a half"],"three"]);
			Assert.assertEquals(result, "one, two,two and a half, three");
		}
	}
}