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
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    static class GroupsInput extends Input {
        public GroupsInput(String path) {
            super(path);
        }

        String template(Json group) {
            if (group.at("id").isNull())
                return "";
            String typeQLInsertQuery = "insert $g isa group";
            typeQLInsertQuery += ", has id " + group.at("id");
            typeQLInsertQuery += ", has title " + group.at("title");
            typeQLInsertQuery += ", has school-year " + group.at("school-year");
            typeQLInsertQuery += ";";
            return typeQLInsertQuery;
        }
    }

    static class ResultsInput extends Input {
        public ResultsInput(String path) {
            super(path);
        }

        String template(Json result) {
            if (result.at("title").isNull())
                return "";
            String typeQLInsertQuery = "insert $r isa result";
            typeQLInsertQuery += ", has title " + result.at("title");
            typeQLInsertQuery += ", has rank " + result.at("rank").asLong();
            typeQLInsertQuery += ";";
            return typeQLInsertQuery;
        }
    }

    static class OlympiadsInput extends Input {
        public OlympiadsInput(String path) {
            super(path);
        }

        String template(Json result) {
            if (result.at("title").isNull())
                return "";
            String typeQLInsertQuery = "insert $o isa olympiad";
            typeQLInsertQuery += ", has title " + result.at("title");
            typeQLInsertQuery += ", has school-year " + result.at("school-year");
            typeQLInsertQuery += ", has level " + result.at("level");
            typeQLInsertQuery += ";";
            return typeQLInsertQuery;
        }
    }

    static class MembershipInput extends Input {
        public MembershipInput(String path) {
            super(path);
        }

        String template(Json item) {
            String typeQLInsertQuery = "match $s isa student, has id " + item.at("student_id") + ";\n";
            typeQLInsertQuery += "$g isa group, has id " + item.at("group_id") + ";\n";
            typeQLInsertQuery += "insert (member: $s, group: $g) isa membership;";
            return typeQLInsertQuery;
        }
    }

    static class TeachingInput extends Input {
        public TeachingInput(String path) {
            super(path);
        }

        String template(Json item) {
            if (item.at("teacher_id").isNull())
                return "";
            String typeQLInsertQuery = "match $t isa teacher, has id " + item.at("teacher_id") + ";\n";
            typeQLInsertQuery += "$g isa group, has id " + item.at("group_id") + ";\n";
            typeQLInsertQuery += "insert (group: $g, teacher: $t) isa teaching;";
            return typeQLInsertQuery;
        }
    }

    static class ParticipationInput extends Input {
        public ParticipationInput(String path) {
            super(path);
        }

        String template(Json item) {
            String typeQLInsertQuery = "match $o isa olympiad, has title " + item.at("olympiad") + ";\n";
            typeQLInsertQuery += "$s isa student, has id " + item.at("student_id") + ";\n";
            if (! item.at("result").isNull()) {
                typeQLInsertQuery += "$r isa result, has title " + item.at("result") + ";\n";
                typeQLInsertQuery += "insert (olympiad: $o, participant: $s, result: $r) isa participation";
            }
            else {
                typeQLInsertQuery += "insert (olympiad: $o, participant: $s) isa participation";
            }
            if (! item.at("score").isNull()) {
                typeQLInsertQuery += ", has score " + item.at("score").asLong();
            }
            typeQLInsertQuery += ", has grade " + item.at("grade").asLong();
            typeQLInsertQuery += ";";
            return typeQLInsertQuery;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        String databaseName = "achievements";
        String databaseAddress = "localhost:1729";
        String schemaFileName = "schema/schema.tql";

        Collection<Input> inputs = initialiseInputs();

        TypeDBClient client = TypeDB.coreClient(databaseAddress);

        createDatabase(databaseName, client);
        defineSchema(databaseName, schemaFileName, client);
        loadAllData(databaseName, inputs, client);

        client.close();

    }

    private static void createDatabase(String databaseName, TypeDBClient client) {
        if (client.databases().contains(databaseName)) {
            System.out.println("Database '" + databaseName + "' exists, recreating...");
            client.databases().get(databaseName).delete();
        }
        client.databases().create(databaseName);
    }

    private static void defineSchema(String databaseName, String schemaFileName, TypeDBClient client) {
        try (TypeDBSession session = client.session(databaseName, TypeDBSession.Type.SCHEMA)) {
            try (TypeDBTransaction transaction = session.transaction(TypeDBTransaction.Type.WRITE)) {
                String typeQLSchemaQuery = Files.readString(Paths.get(schemaFileName));
                System.out.println("Defining schema...");
                transaction.query().define(TypeQL.parseQuery(typeQLSchemaQuery).asDefine());
                transaction.commit();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void loadAllData(String databaseName, Collection<Input> inputs, TypeDBClient client) throws FileNotFoundException {
        try (TypeDBSession session = client.session(databaseName, TypeDBSession.Type.DATA)) {
            for (Input input : inputs) {
                System.out.println("Loading from [" + input.getDataPath() + "] into TypeDB ...");
                loadDataIntoTypeDB(input, session);
            }
        }
    }

    private static Collection<Input> initialiseInputs() {
        Collection<Input> inputs = new ArrayList<>();
        inputs.add(new StudentInput("students.csv"));
        inputs.add(new TeacherInput("teachers.csv"));
        inputs.add(new GroupsInput("groups.csv"));
        inputs.add(new ResultsInput("results.csv"));
        inputs.add(new OlympiadsInput("olympiads.csv"));
        inputs.add(new MembershipInput("membership.csv"));
        inputs.add(new TeachingInput("teaching.csv"));
        inputs.add(new ParticipationInput("participation.csv"));
        return inputs;
    }

    static void loadDataIntoTypeDB(Input input, TypeDBSession session) throws FileNotFoundException {
        ArrayList<Json> items = parseDataToJson(input);
        for (Json item : items) {
            try (TypeDBTransaction transaction = session.transaction(TypeDBTransaction.Type.WRITE)) {
                String typeQLInsertQuery = input.template(item);
                if (! typeQLInsertQuery.isEmpty()) {
                    transaction.query().insert(TypeQL.parseQuery(typeQLInsertQuery).asInsert());
                    transaction.commit();
                }
            }
        }
        System.out.println("\nInserted " + items.size() + " items from [" + input.getDataPath() + "] into TypeDB.\n");
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
