package com.danialechoes.aiservice.dto;

import java.util.List;
import java.util.Map;

public class QueryResponse {
    private String question;
    private String sqlQuery;
    private List<Map<String, Object>> data;
    private String humanReadableAnswer;
    private String error;

    public QueryResponse() {
    }

    public QueryResponse(String question, String sqlQuery, List<Map<String, Object>> data, String humanReadableAnswer) {
        this.question = question;
        this.sqlQuery = sqlQuery;
        this.data = data;
        this.humanReadableAnswer = humanReadableAnswer;
    }

    public QueryResponse(String question, String error) {
        this.question = question;
        this.error = error;
    }

    public String getSqlQuery() {
        return sqlQuery;
    }

    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getHumanReadableAnswer() {
        return humanReadableAnswer;
    }

    public void setHumanReadableAnswer(String humanReadableAnswer) {
        this.humanReadableAnswer = humanReadableAnswer;
    }
}
