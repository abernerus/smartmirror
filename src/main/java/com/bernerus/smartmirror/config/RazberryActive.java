package com.bernerus.smartmirror.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by FRLU7457 on 2017-02-22.
 */
public class RazberryActive implements Condition{
    @Value("${razberry.active}")
    private boolean razberryActive;
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return razberryActive;
    }
}
