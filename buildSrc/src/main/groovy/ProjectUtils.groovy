import org.gradle.api.Project

class ProjectUtils {

    static boolean isSubProject(Project project, String projectName) {
        if (project.parent == null) {
            return project.name == projectName
        }
        return project.parent.name == projectName || isSubProject(project.parent, projectName)
    }

    static String computeGroup(Project project) {
        if (project.parent == null) {
            return project.group
        }
        def suffix = project.parent == project.rootProject ? "" : ("." + project.parent.name)
        return computeGroup(project.parent) + suffix
    }

    static String computeBaseName(Project project) {
        return computeProjectName(project, "-")
    }

    static String computeDockerName(Project project) {
        return computeProjectName(project, "-", "/")
    }

    def static loadSecretProps(Project project, secretFile) {
        def sf = new File(secretFile.toString())
        if (sf.exists()) {
            def props = new Properties()
            sf.withInputStream { props.load(it) }
            props.findAll { Strings.isBlank(extraProp(project, it.key.toString())) }
                 .each { k, v -> project.ext.set(k, v) }
        }
    }

    static String extraProp(Project project, String key) {
        return extraProp(project, key, null)
    }

    static String extraProp(Project project, String key, String fallback) {
        return project.ext.has(key) && !Strings.isBlank((String) project.ext.get(key)) ? (String) project.ext.get(key) :
               fallback
    }

    private static String computeProjectName(Project project, String sep, String firstSep = null) {
        if (project.parent == null) {
            return extraProp(project, "baseName", project.name)
        }
        final def s = project.parent.parent == null && firstSep ? firstSep : sep
        return computeProjectName(project.parent, sep, firstSep) + s + project.projectDir.name
    }
}
