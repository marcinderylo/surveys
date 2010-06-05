package org.adaptiveplatform.surveys.view.components.combocheck {
	
	import flash.events.Event;

	public class ComboCheckEvent extends Event {
		
		public static const COMBO_CHECKED:String = "comboChecked";
		public var obj:Object;
		
		public function ComboCheckEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false) {
			super(type, bubbles, cancelable);
		}
		
	}

}