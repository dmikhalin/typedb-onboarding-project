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

    static class TestQuantity extends TestExampleLong {
        public TestQuantity(String entity, long result) {
            super("Number of " + entity + "s",
                    "match $x isa " + entity + "; get $x; count;",
                    result);
        }
    }

    @Test
    public void assertMigrationResults() {
        Collection<TestQuantity> testExamples = new ArrayList<>();
        testExamples.add(new TestQuantity("student", 915));
        testExamples.add(new TestQuantity("teacher", 11));
        testExamples.add(new TestQuantity("group", 190));
        testExamples.add(new TestQuantity("result", 10));
        testExamples.add(new TestQuantity("olympiad", 23));
        testExamples.add(new TestQuantity("membership", 2121));
        testExamples.add(new TestQuantity("teaching", 123));
        testExamples.add(new TestQuantity("participation", 1316));

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
