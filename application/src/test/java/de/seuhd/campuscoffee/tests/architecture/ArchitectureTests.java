package de.seuhd.campuscoffee.tests.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * Architecture tests that ensure that the application follows the ports-and-adapters pattern.
 */
public class ArchitectureTests {
    @Test
    void testArchitecture() {
        JavaClasses classes = new ClassFileImporter()
                .importPackages(
                        "de.seuhd.campuscoffee" // imports all sub-packages
                );

        layeredArchitecture()
                .consideringAllDependencies()
                .layer("api").definedBy("de.seuhd.campuscoffee.api..")
                .layer("domain").definedBy("de.seuhd.campuscoffee.domain..")
                .layer("data").definedBy("de.seuhd.campuscoffee.data..")
                .layer("application").definedBy("de.seuhd.campuscoffee", "de.seuhd.campuscoffee.tests..")
                .whereLayer("api").mayOnlyBeAccessedByLayers("application")
                .whereLayer("domain").mayOnlyBeAccessedByLayers("api", "data", "application")
                .whereLayer("data").mayOnlyBeAccessedByLayers("application")
                .whereLayer("application").mayNotBeAccessedByAnyLayer()
                .check(classes);
    }
}
