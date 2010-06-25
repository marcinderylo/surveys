package org.adaptiveplatform.surveys.view.events
{
	import flash.events.Event;
	
	import org.adaptiveplatform.surveys.dto.generated.AnswerTemplateDto;
	
	public class AnswerEvent extends Event
	{

		public static const REQUIRE_COMMENT_ANSWER_EVENT:String = "requireCommentAnswerEvent";		
		public static const ADD_ANSWER_EVENT:String = "addAnswerEvent";
		public static const REMOVE_ANSWER_EVENT:String = "removeAnswerEvent";
		private var _answer:AnswerTemplateDto;
		
		public function AnswerEvent(type:String, answer:AnswerTemplateDto, bubbles:Boolean=false, cancelable:Boolean=false) {
			super(type, bubbles, cancelable);
			_answer = answer;
		}
		
		public function get answer():AnswerTemplateDto{
			return _answer;
		}

	}
}