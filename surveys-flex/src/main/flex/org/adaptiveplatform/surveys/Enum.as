package org.adaptiveplatform.surveys {
	import flash.utils.getQualifiedClassName;

	public class Enum {
		public var name:String;

		public function Enum(name:String) {
			this.name = name;
		}

		public function equals(other:Enum):Boolean {
			return other.name == name && getQualifiedClassName(this) == getQualifiedClassName(other);
		}

		public function toString():String {
			return name;
		}
	}
}