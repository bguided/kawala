/**
 * Copyright 2010 Wealthfront Inc. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.kaching.platform.testing;

import static java.lang.String.format;
import static org.junit.runner.Description.createSuiteDescription;
import static org.junit.runner.Description.createTestDescription;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

/**
 * Skeleton to build runners for declarative tests (annotation driven) such as
 * the {@link BadCodeSnippetsRunner}.
 */
public abstract class AbstractDeclarativeTestRunner<A extends Annotation> extends Runner {

  private final Class<?> klass;
  private final Class<? extends Annotation> topLevelAnnotation;
  private final Description suiteDescription;
  private final Description description;

  /**
   * Constructor for implementors. JUnit requires of that runners have a
   * one argument constructor taking the testClass as argument. Typical
   * implementation will look like
   * <pre>
   * public MyDeclarativeTes(Class&lt;?&gt; testClass) {
   *   super(testClass, MyTopLevelAnnotation.class);
   * }
   * </pre>
   * @param testClass the class under test
   * @param annotationClass the annotation class which is the root of the
   *     declarative test
   */
  protected AbstractDeclarativeTestRunner(
      Class<?> testClass, Class<A> annotationClass) {
    this.klass = testClass;
    this.topLevelAnnotation = annotationClass;
    suiteDescription = createSuiteDescription(klass);
    suiteDescription.addChild(description = createTestDescription(klass, "checking"));
  }

  @Override
  public Description getDescription() {
    return suiteDescription;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void run(RunNotifier notifier) {
    new SingleTestExecutor(notifier) {
      @Override
      protected void doWork() throws Exception {
        A annotation = (A) klass.getAnnotation(topLevelAnnotation);
        if (annotation == null) {
          throw new AssertionError(format(
              "missing @%s annotation", topLevelAnnotation.getSimpleName()));
        }
        runTest(annotation);
      }
    }.runSingleTest(description);
  }

  /**
   * Extension point to implement the actual testing.
   */
  abstract protected void runTest(A annotation) throws IOException;

}
