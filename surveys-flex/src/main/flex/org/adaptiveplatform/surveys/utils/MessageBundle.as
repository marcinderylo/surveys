package org.adaptiveplatform.surveys.utils {
import flash.events.Event;
import flash.events.EventDispatcher;
import flash.events.IEventDispatcher;
import flash.utils.Proxy;
import flash.utils.flash_proxy;

import mx.resources.IResourceManager;
import mx.utils.ObjectProxy;

use namespace flash_proxy;

[Bindable("change")]
dynamic public class MessageBundle extends Proxy implements IEventDispatcher {
    private var eventDispatcher:IEventDispatcher;

    private var resourceManager:IResourceManager;
    private var bundleName:String;
    private var prefix:String="";

    public function MessageBundle(resourceManager:IResourceManager, bundleName:String, prefix:String=null) {
        eventDispatcher=new EventDispatcher(this);
        this.resourceManager=resourceManager;
        this.bundleName=bundleName;
        if (prefix) {
            this.prefix=prefix;
        }

        resourceManager.addEventListener("change", function(event:Event):void {
            dispatchEvent(event);
        });
    }

    public function string(key:String):String {
        const bundleKey:String=prefix + key;
        var localizedString:String=resourceManager.getString(bundleName, bundleKey);
        if (localizedString) {
            return localizedString;
        } else {
            trace("Localization error: key \"" + bundleKey + "\" not found in bundle \"" + bundleName + "\"");
            return "##" + bundleKey + "##";
        }
    }

    flash_proxy override function getProperty(name:*):* {
        return string(name);
    }

    public function hasEventListener(type:String):Boolean {
        return eventDispatcher.hasEventListener(type);
    }

    public function willTrigger(type:String):Boolean {
        return eventDispatcher.willTrigger(type);
    }

    public function addEventListener(type:String, listener:Function, useCapture:Boolean=false, priority:int=0.0, useWeakReference:Boolean=false):void {
        eventDispatcher.addEventListener(type, listener, useCapture, priority, useWeakReference);
    }

    public function removeEventListener(type:String, listener:Function, useCapture:Boolean=false):void {
        eventDispatcher.removeEventListener(type, listener, useCapture);
    }

    public function dispatchEvent(event:Event):Boolean {
        return eventDispatcher.dispatchEvent(event);
    }
}
}