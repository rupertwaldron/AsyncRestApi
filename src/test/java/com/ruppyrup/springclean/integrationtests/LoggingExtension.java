package com.ruppyrup.springclean.integrationtests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;


@Slf4j
public class LoggingExtension implements BeforeAllCallback, AfterAllCallback, TestInstancePostProcessor, BeforeEachCallback, AfterEachCallback, BeforeTestExecutionCallback, AfterTestExecutionCallback, ParameterResolver {
  @Override
  public void afterAll(final ExtensionContext context) throws Exception {
    log.info("after all :: " + context);
  }

  @Override
  public void afterEach(final ExtensionContext context) throws Exception {
    log.info("after :: " + context);
  }

  @Override
  public void afterTestExecution(final ExtensionContext context) throws Exception {
    log.info("after Test execution :: " + context);
  }

  @Override
  public void beforeAll(final ExtensionContext context) throws Exception {
    log.info("before :: " + context);
  }

  @Override
  public void beforeEach(final ExtensionContext context) throws Exception {
    log.info("before each :: " + context);
  }

  @Override
  public void beforeTestExecution(final ExtensionContext context) throws Exception {
    log.info("before test execution :: " + context);
  }

  @Override
  public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {
    return true;
  }

  @Override
  public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {
    log.info("resolve Parameter :: " + parameterContext);
    return null;
  }

  @Override
  public void postProcessTestInstance(final Object testInstance, final ExtensionContext context) throws Exception {
    log.info("postProcessTestInstance :: " + testInstance);
  }
}
