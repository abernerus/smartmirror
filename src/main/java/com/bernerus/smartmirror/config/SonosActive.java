package com.bernerus.smartmirror.config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by FRLU7457 on 2017-02-22.
 */
public class SonosActive implements org.springframework.context.annotation.Condition {
    private static final Logger log = LoggerFactory.getLogger(SonosActive.class);

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return Boolean.valueOf(conditionContext.getEnvironment().getProperty("eyesensor.active"));
    }
}
