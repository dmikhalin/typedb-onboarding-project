package com.example;

import com.vaticle.typedb.client.api.TypeDBClient;
import com.vaticle.typedb.client.api.TypeDBOptions;
import com.vaticle.typedb.client.api.TypeDBSession;
import com.vaticle.typedb.client.TypeDB;
import com.vaticle.typedb.client.api.TypeDBTransaction;
import com.vaticle.typeql.lang.TypeQL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;


public class DataLoaderTest {
    TypeDBClient client;
    TypeDBSession session;
    String databaseName = "achievements";
    String databaseAddress = "localhost:1729";

    @Before
    public void loadDataAndConnect() throws FileNotFoundException {
        DataLoader.main(new String[] {});
        client = TypeDB.coreClient(databaseAddress);
        session = client.session(databaseName, TypeDBSession.Type.DATA);
    }

    static class TestExampleLong {
        String message;
        String query;
        long correctResult;

        public TestExampleLong(String message, String query, long result) {
            this.message = message;
            this.query = query;
            this.correctResult = result;
        }

        public void testQueryResult(TypeDBTransaction transaction) {
            long queryResult = transaction.query().match(TypeQL.parseQuery(query).asMatchAggregate()).get().asNumber().longValue();
            assertEquals(message, correctResult, queryResult);
        }
    }

    @Test
    public void assertMigrationResults() {
        Collection<TestExampleLong> testExamples = new ArrayList<>();
        testExamples.add(new TestExampleLong(
                "Number of students",
                "match $x isa student; get $x; count;",
                915
        ));
        testExamples.add(new TestExampleLong(
                "Number of teachers",
                "match $x isa teacher; get $x; count;",
                11
        ));
        testExamples.add(new TestExampleLong(
                "Number of groups",
                "match $x isa group; get $x; count;",
                190
        ));
        testExamples.add(new TestExampleLong(
                "Number of results",
                "match $x isa result; get $x; count;",
                10
        ));
        testExamples.add(new TestExampleLong(
                "Number of olympiads",
                "match $x isa olympiad; get $x; count;",
                23
        ));
        testExamples.add(new TestExampleLong(
                "Number of membership relations",
                "match $x isa membership; get $x; count;",
                2121
        ));
        testExamples.add(new TestExampleLong(
                "Number of teaching relations",
                "match $x isa teaching; get $x; count;",
                123
        ));
        testExamples.add(new TestExampleLong(
                "Number of participation relations",
                "match $x isa participation; get $x; count;",
                1316
        ));

        TypeDBTransaction transaction = session.transaction(TypeDBTransaction.Type.READ);
        for (TestExampleLong example : testExamples) {
            example.testQueryResult(transaction);
        }
        transaction.close();
    }

    @Test
    public void assertRules() {
        TestExampleLong example = new TestExampleLong(
                "Number of ROI-2023 prize-winners",
                "match $stud isa student; $olymp isa olympiad, has title 'ROI 2023'; " +
                        "($stud, $olymp) isa prize-winning; get $stud; count;",
                6
        );
        TypeDBTransaction transaction = session.transaction(TypeDBTransaction.Type.READ, TypeDBOptions.core().infer(true));
        example.testQueryResult(transaction);
        transaction.close();
    }

    @After
    public void disconnect() {
        session.close();
        client.close();
    }
}
