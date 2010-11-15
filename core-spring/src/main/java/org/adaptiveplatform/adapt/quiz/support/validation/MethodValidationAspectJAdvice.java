package org.adaptiveplatform.adapt.quiz.support.validation;

import java.lang.reflect.Method;
import org.adaptiveplatform.adapt.commons.validation.argsvalidation.MethodCallArgumentsValidator;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.Assert;

/**
 *  Adapter of the {@link MethodCallArgumentsValidator} to AspectJ-compatible component, to be used
 *  as a before advice.
 * 
 *  @author Marcin Derylo <marcinderylo@gmail.com>
 */
public class MethodValidationAspectJAdvice {

    private MethodCallArgumentsValidator validator;

    public MethodValidationAspectJAdvice(MethodCallArgumentsValidator validator) {
        this.validator = validator;
    }

    public void validateMethodArguments(JoinPoint joinPoint) throws Exception {
        MethodSignature methodSignature = extractMethodSignature(joinPoint);
        Method method = methodSignature.getMethod();
        Object[] arguments = joinPoint.getArgs();
        validator.validateArguments(method, arguments);
    }

    private MethodSignature extractMethodSignature(JoinPoint joinPoint) {
        final Signature signature = joinPoint.getSignature();
        Assert.isInstanceOf(MethodSignature.class, signature,
                "Can only validate regular method's arguments");
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return methodSignature;
    }
} 