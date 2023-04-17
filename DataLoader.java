package com.example;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.vaticle.typedb.client.api.TypeDBClient;
import com.vaticle.typedb.client.api.TypeDBSession;
import com.vaticle.typedb.client.TypeDB;
import com.vaticle.typedb.client.api.TypeDBTransaction;
import com.vaticle.typeql.lang.TypeQL;
import mjson.Json;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

public class DataLoader {
    abstract static class Input {
        String path;

        public Input(String path) {
            this.path = path;
        }

        String getDataPath() {
            return "data/" + path;
        }

        abstract String template(Json data);
    }

    static class StudentInput extends Input{
        public StudentInput(String path) {
            super(path);
        }

        String template(Json student) {
            if (student.at("id").isNull())
                return "";
            String typeQLInsertQuery = "insert $s isa student";
            typeQLInsertQuery += ", has id " + student.at("id");
            typeQLInsertQuery += ", has name " + student.at("name");
            typeQLInsertQuery += ", has email " + student.at("email");
            typeQLInsertQuery += ", has graduate-year " + student.at("graduate-year").asLong();
            typeQLInsertQuery += ";";
            return typeQLInsertQuery;
        }
    }

    static class TeacherInput extends Input {
        public TeacherInput(String path) {
            super(path);
        }

        String template(Json teacher) {
            if (teacher.at("name").isNull())
                return "";
            String typeQLInsertQuery = "insert $t isa teacher";
            typeQLInsertQuery += ", has id " + teacher.at("id");
            typeQLInsertQuery += ", has name " + teacher.at("name");
            typeQLInsertQuery += ", has email " + teacher.at("email");
            typeQLInsertQuery += ";";
            return typeQLInsertQuery;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        String databaseName = "achievements";
        String databaseAddress = "localhost:1729";
        Collection<Input> inputs = initialiseInputs();

        TypeDBClient client = TypeDB.coreClient(databaseAddress);
        TypeDBSession session = client.session(databaseName, TypeDBSession.Type.DATA);

        for (Input input : inputs) {
            System.out.println("Loading from [" + input.getDataPath() + ".csv] into TypeDB ...");
            loadDataIntoTypeDB(input, session);
        }

        session.close();
        client.close();
    }

    private static Collection<Input> initialiseInputs() {
        Collection<Input> inputs = new ArrayList<>();
        inputs.add(new StudentInput("students.csv"));
        inputs.add(new TeacherInput("teachers.csv"));
        return inputs;
    }

    static void loadDataIntoTypeDB(Input input, TypeDBSession session) throws FileNotFoundException {
        ArrayList<Json> items = parseDataToJson(input);
        for (Json item : items) {
            TypeDBTransaction transaction = session.transaction(TypeDBTransaction.Type.WRITE);
            String typeQLInsertQuery = input.template(item);
            System.out.println("Executing TypeQL Query: " + typeQLInsertQuery);
            transaction.query().insert(TypeQL.parseQuery(typeQLInsertQuery).asInsert());
            transaction.commit();
        }
        System.out.println("\nInserted " + items.size() + " items from [ " + input.getDataPath() + ".csv] into TypeDB.\n");
    }

    static ArrayList<Json> parseDataToJson(Input input) throws FileNotFoundException {
        ArrayList<Json> items = new ArrayList<>();

        CsvParserSettings settings = new CsvParserSettings();
        settings.setLineSeparatorDetectionEnabled(true);
        CsvParser parser = new CsvParser(settings);
        parser.beginParsing(new InputStreamReader(new FileInputStream(input.getDataPath())));

        String[] columns = parser.parseNext();
        String[] row;
        while ((row = parser.parseNext()) != null) {
            Json item = Json.object();
            for (int i = 0; i < row.length; i++) {
                item.set(columns[i], row[i]);
            }
            items.add(item);
        }
        return items;
    }

}
