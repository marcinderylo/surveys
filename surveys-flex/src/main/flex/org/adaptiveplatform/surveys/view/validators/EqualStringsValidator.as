package org.adaptiveplatform.surveys.view.validators
{
	import mx.validators.ValidationResult;
	import mx.validators.Validator;
	
	public class EqualStringsValidator extends Validator
	{
		public function EqualStringsValidator()
		{
			super();
		}
		
        private var results:Array; 

        [Bindable] 
        public var repeatedValue:String = new String(); 

        [Bindable] 
        public var notEqualErrorMsg:String; 

        override protected function doValidation(value:Object):Array { 

            results = []; 
            results = super.doValidation(value); 

            // Return if there are errors. 
            if (results.length > 0) 
                return results; 

			if(String(value).length == 0){
				return results;
			}
            if(repeatedValue == String(value)) 
            {
	            return results; 
            }else{
            	notEqualErrorMsg = notEqualErrorMsg == null ? "Values not equal!" :notEqualErrorMsg;
            	results.push(new ValidationResult(true, null, "", notEqualErrorMsg));
                return results; 
			}
        }

	}
}