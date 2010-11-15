package org.adaptiveplatform.surveys
{
	import org.adaptiveplatform.communication.Enum;
	
	public class QuestionStyleEnum extends Enum
	{
		public static const FOUR_TOGGLE:QuestionStyleEnum = new QuestionStyleEnum("FOUR_TOGGLE");
		public static const SINGLE_TYPE:QuestionStyleEnum = new QuestionStyleEnum("SINGLE_TYPE");

		public function QuestionStyleEnum(name:String = null) {
			super(name);
		}
	}
}