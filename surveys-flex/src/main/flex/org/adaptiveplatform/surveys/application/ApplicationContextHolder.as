package org.adaptiveplatform.surveys.application {
	import org.adaptiveplatform.surveys.application.mock.ServiceLocatorMock;

	public class ApplicationContextHolder {
		private static var _context:DefaultApplicationContext;

		private var view:ViewController;

		public static function initializeMockContext(view:ViewController):void {
			initialize(new DefaultApplicationContext(view, new ServiceLocatorMock()));
		}

		public static function initializeRemoteContext(view:ViewController, channelName:String, channelUrl:String):void {
			var services:ServiceLocator = new ServiceLocatorRemote(channelName, channelUrl);
			initialize(new DefaultApplicationContext(view, services));
		}


		private static function initialize(applicationContext:DefaultApplicationContext):void {
			if (_context != null) {
				throw new Error("context already initialized");
			}
			_context = applicationContext;
		}

		public static function get context():DefaultApplicationContext {
			if (_context == null) {
				throw new Error("context not initialized");
			}
			return _context;
		}
	}
}