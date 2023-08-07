package com.trello_server.webclosure.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.stereotype.Service;

@Service
public class TrelloService {
    public boolean addTask(String name, String description) throws UnirestException {

            HttpResponse<JsonNode> jsonResponse
                = Unirest.post("https://api.trello.com/1/cards")
                .header("accept", "application/json")
                .queryString("idList", "64cc82c807bdb9b5afdec1c0")
                .queryString("name", name)
                .queryString("desc", description)
                .queryString("key", "df14229fa59386df45d2454466c6f01b")
                .queryString("token", "ATTA366628db69b0611e29a470a9e8e7b6ba74ed4304a3b48d4fbf022370e9b71b76303362B3")
                .asJson();
            return jsonResponse.getStatus() == 200;
    }
}
