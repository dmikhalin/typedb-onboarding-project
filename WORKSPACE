workspace(name = "example")

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
        "com.vaticle.typedb:typedb-client:2.16.1",
        "com.vaticle.typeql:typeql-lang:2.14.0",
        "org.sharegov:mjson:1.4.1",
        "com.univocity:univocity-parsers:2.9.1",
        "junit:junit:4.13.2"
    ],
    repositories = [
        "https://maven.google.com",
        "https://repo1.maven.org/maven2",
        "https://repo.vaticle.com/repository/maven/",
    ],
)

load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")

git_repository(
    name = "vaticle_dependencies",
    remote = "https://github.com/vaticle/dependencies",
    commit = "a8b3b714a5e0562d41f4c05ca8f266d48b7d0fd3",
)

git_repository(
    name = "vaticle_typedb_common",
    remote = "https://github.com/vaticle/typedb-common",
    commit = "f0dd708adaea9fe1fdc3699180797a12166d33e8"
)

load("@vaticle_dependencies//distribution:deps.bzl", "vaticle_bazel_distribution")
vaticle_bazel_distribution()
