package org.adaptiveplatform.surveys.view.components.questions
{
	import flash.events.Event;
	
	public class RequireCommentEvent extends Event
	{
		
		public static const REQUIRE_COMMENT_CHECKED:String = "requireCommentChecked";
		public var requireComment:Boolean;
		
		public function RequireCommentEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false) {
			super(type, bubbles, cancelable);
		}
		
	}
}