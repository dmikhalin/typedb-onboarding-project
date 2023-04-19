java_library(
    name = "data-loader-lib",
    srcs = ["DataLoader.java"],
    deps = [
        "@maven//:com_vaticle_typedb_typedb_client_2_16_1",
        "@maven//:com_vaticle_typeql_typeql_lang_2_14_0",
        "@maven//:com_vaticle_typeql_typeql_query",
        "@maven//:org_sharegov_mjson_1_4_1",
        "@maven//:com_univocity_univocity_parsers_2_9_1"
    ],
    data = [
        "//:data-csv",
        "//:schema",
    ],
    resources = ["//resources:logback.xml"],
    resource_strip_prefix = "resources",
)

java_binary(
    name = "data-loader",
    main_class = "com.example.DataLoader",
    srcs = ["DataLoader.java"],
    deps = [
        "@maven//:com_vaticle_typedb_typedb_client_2_16_1",
        "@maven//:com_vaticle_typeql_typeql_lang_2_14_0",
        "@maven//:com_vaticle_typeql_typeql_query",
        "@maven//:org_sharegov_mjson_1_4_1",
        "@maven//:com_univocity_univocity_parsers_2_9_1"
    ],
    data = [
        "//:data-csv",
        "//:schema",
    ],
    resources = ["//resources:logback.xml"],
    resource_strip_prefix = "resources",
)

load("@vaticle_typedb_common//test:rules.bzl", "native_typedb_artifact")
load("@vaticle_bazel_distribution//artifact:rules.bzl", "artifact_extractor")

native_typedb_artifact(
    name = "native-typedb-artifact",
    mac_artifact = "@vaticle_typedb_artifact_mac//file",
    linux_artifact = "@vaticle_typedb_artifact_linux//file",
    windows_artifact = "@vaticle_typedb_artifact_windows//file",
    output = "typedb-server-native.tar.gz",
    visibility = ["//test:__subpackages__"],
)

artifact_extractor(
    name = "typedb-extractor",
    artifact = ":native-typedb-artifact",
)

java_test(
    name = "test",
    test_class = "com.example.DataLoaderTest",
    srcs = [
        "DataLoaderTest.java"
    ],
    deps = [
        "@maven//:com_vaticle_typedb_typedb_client_2_16_1",
        "@maven//:com_vaticle_typeql_typeql_lang_2_14_0",
        "@maven//:com_vaticle_typeql_typeql_query",
        "@maven//:junit_junit_4_13_2",
        ":data-loader-lib",
    ],
    resources = ["//resources:logback.xml", "//resources:logback-test.xml"],
    resource_strip_prefix = "resources",
)

filegroup(
    name = "data-csv",
    srcs = glob(["data/*.csv"]),
    visibility = ["//visibility:public"]
)

filegroup(
    name = "schema",
    srcs = glob(["schema/*.tql"]),
    visibility = ["//visibility:public"]
)
