package org.adaptiveplatform.surveys.application{
	public class RemoteBusinessExceptionParametersUnmarshaller 	{
		public function RemoteBusinessExceptionParametersUnmarshaller()		{
		}
		
		public function split(string:String):Array{
			if(string==null || string==""){
				return [];
			}
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
					if(rawString.charAt(rawString.length-1)=="]"){
						rawString = rawString.substr(0, rawString.length-1);
						nestedCollection.push(rawString);
						aggregating=false;
						results.push(nestedCollection);
						nestedCollection= [];
					}else{
						nestedCollection.push(rawString);	
					}
				}else{
					results.push(rawString);					
				}	
			}
			return results;
		}
	}
}