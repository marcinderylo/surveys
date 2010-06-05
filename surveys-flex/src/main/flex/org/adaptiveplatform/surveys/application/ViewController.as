package org.adaptiveplatform.surveys.application {
	import mx.collections.ArrayCollection;

	public interface ViewController {
		function goto(view:String, ... arguments):void;
		function get currentView():String;
		function get currentViewPath():ArrayCollection;
	}
}