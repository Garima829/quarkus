package io.quarkus.commandmode;

import org.assertj.core.api.Assertions;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusProdModeTest;

public class NamedMainTestCase {
    @RegisterExtension
    static final QuarkusProdModeTest config = new QuarkusProdModeTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
                    .addAsManifestResource("application.properties", "microprofile-config.properties")
                    .addClasses(JavaMain.class, HelloWorldNonDefault.class, NamedMain.class))
            .setApplicationName("run-exit")
            .overrideConfigKey("quarkus.package.main-class", "somename")
            .setApplicationVersion("0.1-SNAPSHOT")
            .setExpectExit(true)
            .setRun(true);

    @Test
    public void testRun() {
        Assertions.assertThat(config.getStartupConsoleOutput()).contains("Hello Named");
        Assertions.assertThat(config.getExitCode()).isEqualTo(100);
    }

}
