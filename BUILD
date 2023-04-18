java_binary(
    name = "data-loader",
    main_class = "com.example.DataLoader",
    srcs = ["DataLoader.java"],
    deps = [
        "@maven//:com_vaticle_typedb_typedb_client_2_16_1",
        "@maven//:com_vaticle_typeql_typeql_lang_2_14_0",
        "@maven//:com_vaticle_typeql_typeql_query",
#        "@maven//:client-java",
#        "@maven//java:typeql-lang",
#        "@maven//api",
#        "@maven//java/query",
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
