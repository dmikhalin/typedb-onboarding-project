load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

RULES_JVM_EXTERNAL_TAG = "4.5"
RULES_JVM_EXTERNAL_SHA = "b17d7388feb9bfa7f2fa09031b32707df529f26c91ab9e5d909eb1676badd9a6"

http_archive(
    name = "rules_jvm_external",
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    sha256 = RULES_JVM_EXTERNAL_SHA,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:repositories.bzl", "rules_jvm_external_deps")

rules_jvm_external_deps()

load("@rules_jvm_external//:setup.bzl", "rules_jvm_external_setup")

rules_jvm_external_setup()

load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    artifacts = [
#        "junit:junit:4.12",
#        "androidx.test.espresso:espresso-core:3.1.1",
#        "org.hamcrest:hamcrest-library:1.3",
        "com.vaticle.typedb:typedb-client:2.16.1",
        "com.vaticle.typeql:typeql-lang:2.14.0",
    ],
    repositories = [
        # Private repositories are supported through HTTP Basic auth
#        "http://username:password@localhost:8081/artifactory/my-repository",
#        "https://maven.google.com",
#        "https://repo1.maven.org/maven2",
        "https://repo.vaticle.com/repository/maven/",
    ],
)