package org.adaptiveplatform.surveys.application {

	import flash.events.EventDispatcher;

	public class DefaultApplicationContext {

		private var _service:ServiceLocator;
		private var _view:ViewController;

		public function DefaultApplicationContext(views:ViewController, services:ServiceLocator) {
			this._service = services;
			this._view = views;
		}

		public function get service():ServiceLocator {
			return _service;
		}

		public function get view():ViewController {
			return _view;
		}
	}
}