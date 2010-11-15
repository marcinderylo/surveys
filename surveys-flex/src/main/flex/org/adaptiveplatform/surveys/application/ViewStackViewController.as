package org.adaptiveplatform.surveys.application {
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	import mx.containers.ViewStack;
	import mx.events.IndexChangedEvent;
	import mx.rpc.events.ResultEvent;
	
	import org.adaptiveplatform.surveys.view.View;

	public class ViewStackViewController implements ViewController {

		private var viewStack:ViewStack;

		private var history:ArrayCollection = new ArrayCollection();

		private var initializationArguments:Array;

		private var _currentCategory:String = "som category";
		private var _currentView:String;

		public function ViewStackViewController(viewStack:ViewStack) {
			this.viewStack = viewStack;
			viewStack.addEventListener(Event.CHANGE, onViewChange);
		}

		public function goto(view:String, ... arguments):void {
			if (view == View.BACK) {
				if (history.length < 2) {
					return;
				}
				history.removeItemAt(history.length - 1);
				view = String(history.getItemAt(history.length - 1));
			}
			for (var index:int = 0; index < viewStack.childDescriptors.length; index++) {
				if (viewStack.childDescriptors[index].id == view) {
					viewStack.selectedIndex = index;
					initializationArguments = arguments;
					_currentView = view;
					history.addItem(view);
					return;
				}
			}
			throw new Error("view " + view + " not found");
		}
		
		public function gotoOnSuccess(view:String, ... arguments):Function{
			return function(... ignoredParameters):void {
				ContextFactory.context.view.goto(view, arguments);
			};
		}
		
		public function get currentView():String {
			return _currentView;
		}

		public function get currentViewPath():ArrayCollection {
			return new ArrayCollection([_currentCategory, _currentView]);
		}

		private function onViewChange(event:IndexChangedEvent):void {
			if (initializationArguments != null) {
				viewStack.selectedChild.data = initializationArguments;
				initializationArguments = null;
			}
		}
	}
}