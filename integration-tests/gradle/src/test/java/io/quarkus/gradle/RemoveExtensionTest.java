package io.quarkus.gradle;

import io.quarkus.devtools.commands.CreateProject;
import io.quarkus.devtools.project.BuildTool;
import io.quarkus.devtools.project.codegen.SourceType;
import io.quarkus.platform.tools.config.QuarkusPlatformConfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

public class RemoveExtensionTest extends QuarkusGradleWrapperTestBase{
    private File projectRoot;

    @BeforeEach
    void setUp(@TempDir File projectRoot) {
        this.projectRoot = projectRoot;
    }

    @Test
    public void testRemoveExtension() throws IOException, InterruptedException {
        createProject(SourceType.JAVA);

        BuildResult firstBuild = runGradleWrapper(projectRoot,"quarkusBuild","addExtension", "--extensions=hibernate-orm");
        assertThat(firstBuild.getTasks().get(":addExtension")).isEqualTo(BuildResult.SUCCESS_OUTCOME);

        firstBuild = runGradleWrapper(projectRoot,"removeExtension", "--extensions=hibernate-orm");
        assertThat(firstBuild.getTasks().get(":removeExtension")).isEqualTo(BuildResult.SUCCESS_OUTCOME);

    }
    private void createProject(SourceType sourceType) throws IOException {
        Map<String, Object> context = new HashMap<>();
        context.put("path", "/greeting");
        assertThat(new CreateProject(projectRoot.toPath(),
                QuarkusPlatformConfig.getGlobalDefault().getPlatformDescriptor())
                .groupId("com.acme.foo")
                .artifactId("foo")
                .version("1.0.0-SNAPSHOT")
                .buildTool(BuildTool.GRADLE)
                .className("org.acme.GreetingResource")
                .sourceType(sourceType)
                .doCreateProject(context))
                .withFailMessage("Project was not created")
                .isTrue();
    }
}
