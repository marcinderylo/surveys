package org.adaptiveplatform.surveys.dto.generated {

	/**
	 * NOTE: This file is autogenerated, but will ONLY be created if it doesn't already exist.
	 */	
	[Bindable]
	[RemoteClass(alias="org.adaptiveplatform.surveys.dto.QuestionTemplateDto")]
    public class QuestionTemplateDto extends QuestionTemplateDtoBase{
    
    	public function QuestionTemplateDto(){
		}
	
		public function requiredComment():Boolean {
			if(type.equals(QuestionTypeEnum.OPEN)){
				return true;
			}
			for each (var answer:AnswerTemplateDto in answers) {
				if(answer.requiresComment){
					return true;
				}
			}
			return false;
		}
	}
}
