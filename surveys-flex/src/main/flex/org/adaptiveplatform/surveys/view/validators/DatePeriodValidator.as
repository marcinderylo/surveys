package org.adaptiveplatform.surveys.view.validators
{
	import mx.validators.ValidationResult;
	import mx.validators.Validator;
	
	public class DatePeriodValidator extends Validator
	{
		public function DatePeriodValidator()
		{
			super();
		}
		
		private var results:Array; 

        [Bindable] 
        public var startDate:String = new String(); 

        [Bindable] 
        public var errorMessage:String; 

        override protected function doValidation(value:Object):Array { 

            results = []; 
            results = super.doValidation(value); 
            
            var beginDate:String = String(startDate);
            var endDate:String = String(value);

            // Return if there are errors. 
            if (results.length > 0) 
                return results; 

			if(beginDate=='' || endDate==''){
				return results;
			}
           	var sDate:Number = Date.parse(beginDate);
           	var fDate:Number = Date.parse(endDate);
           	if( sDate > fDate ){
            	errorMessage = errorMessage == null ? "Start date should be before end date!" :errorMessage;
            	results.push(new ValidationResult(true, null, "", errorMessage));
            }
            return results; 
        }


	}
}