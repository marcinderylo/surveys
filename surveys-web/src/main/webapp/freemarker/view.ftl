<html>
<body>
surveys count: ${surveys?size}
<div class="surveysList">
<#list surveys as survey>
	<div class="survey">
	Survey: ${survey_index+1}</br>
	<#list survey.questions as question>
		<div class="question">
		${question.number}, ${question.text}
		<#list question.answers as answer>
			<div class="answer">
				[${answer.selected?string}] ${answer.number}. ${answer.text}  
			</div>
		</#list>
		${question.comment!""}
		</div>
	</#list>
	</div>
</#list>
</div>
</body>
</html>