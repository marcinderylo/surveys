package org.adaptiveplatform.surveys.view.controls
{
	import mx.controls.buttonBarClasses.ButtonBarButton;
	import flash.text.TextLineMetrics;
	import mx.core.IFlexDisplayObject;
	import flash.display.DisplayObject;
	import flash.text.TextFieldAutoSize;
	import mx.core.mx_internal;
	
	use namespace mx_internal;

	public class MultiLineButton extends ButtonBarButton
	{
		public function MultiLineButton()
		{
			super();
		}
		
	override protected function createChildren():void
	{
		if (!textField)
		{
			textField = new NoTruncationUITextField();
			textField.styleName = this;
			addChild( DisplayObject(textField));
		}
		
		super.createChildren();
		textField.multiline = true;
		textField.wordWrap = true;
		textField.autoSize = TextFieldAutoSize.CENTER;
	}

	override protected function measure():void
	{
		var tempIcon:IFlexDisplayObject = getCurrentIcon();
		if (!isNaN(explicitWidth))
		{
			var w:Number = explicitWidth;
			if (tempIcon)
				w -= tempIcon.width + getStyle("horizontalGap") + getStyle("paddingLeft") + getStyle("paddingRight");
			textField.width = w;
		}
		height = textField.height + getStyle("paddingTop") + getStyle("paddingBottom");
		if (!isNaN(explicitHeight))
		{
			var h:Number = explicitHeight;
			if (tempIcon)
				h -= tempIcon.height + getStyle("verticalGap") + getStyle("paddingTop") + getStyle("paddingBottom");
			textField.height = h;
			height = h;
		}
		super.measure();
	
	}

    override public function measureText(s:String):TextLineMetrics
	{
		textField.text = s;
		var lineMetrics:TextLineMetrics = textField.getLineMetrics(0);
		lineMetrics.width = textField.textWidth + 4;
		lineMetrics.height = textField.textHeight + 4;
		return lineMetrics;
	}
	
	override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
		super.updateDisplayList(unscaledWidth, unscaledHeight);
		textField.y = (this.height-textField.height)>>1;
//		height = textField.height + getStyle("paddingTop") + getStyle("paddingBottom");
	}
		
	}
}