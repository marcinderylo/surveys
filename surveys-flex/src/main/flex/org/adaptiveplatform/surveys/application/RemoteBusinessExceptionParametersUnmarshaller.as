package org.adaptiveplatform.surveys.application{
	public class RemoteBusinessExceptionParametersUnmarshaller 	{
		public function RemoteBusinessExceptionParametersUnmarshaller()		{
		}
		
		public function split(string:String):Array{
			var rawStrings:Array =string.split("`");
			var results:Array = [];
			var nestedCollection:Array = [];
			var aggregating:Boolean = false;
			
			for each(var rawString :String in rawStrings){
				if(rawString.charAt(0) == "["){
					aggregating =true;
					rawString = rawString.substr(1);
				}
				if(aggregating){
					nestedCollection.push(rawString);
				}else{
					results.push(rawString);					
				}
				if(rawString.charAt(rawString.length-1)=="]"){
					var lastAddedElement:String = nestedCollection[nestedCollection.length-1];
					nestedCollection[nestedCollection.length-1] = lastAddedElement.substr(0,lastAddedElement.length-1);
					aggregating= false;
					results.push(nestedCollection);
					nestedCollection= [];
				}
			}
			return results;
		}
	}
}