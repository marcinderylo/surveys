package org.adaptiveplatform.surveys.view.events
{
	public class QuestionTypeEvent extends flash.events.Event
	{
		
		public static const QUESTION_TYPE_EVENT:String = "changeQuestionTypeProperties";
		public var excludesOthers:Boolean;
		public var requiresComments:Boolean;
		
		public function QuestionTypeEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}

	}
}