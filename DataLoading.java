package com.example;

import com.vaticle.typedb.client.api.TypeDBClient;
import com.vaticle.typedb.client.api.TypeDBSession;
import com.vaticle.typedb.client.TypeDB;

public class DataLoading {
    public static void main(String[] args) {
        TypeDBClient client = TypeDB.coreClient("localhost:1729");
        // client is open
        TypeDBSession session = client.session("social_network", TypeDBSession.Type.DATA);
        // session is open
        session.close();
        // session is closed
        client.close();
        // client is closed
    }
}
