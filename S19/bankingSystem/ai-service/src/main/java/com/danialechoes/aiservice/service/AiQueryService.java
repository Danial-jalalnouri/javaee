package com.danialechoes.aiservice.service;

import com.danialechoes.aiservice.dto.QueryResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AiQueryService {
    private final ChatClient chatClient;
    private final JdbcTemplate customerJdbcTemplate;
    private final JdbcTemplate depositJdbcTemplate;

    @Autowired
    public AiQueryService(ChatClient chatClient,
                          @Qualifier("customerJdbcTemplate") JdbcTemplate customerJdbcTemplate,
                          @Qualifier("depositJdbcTemplate") JdbcTemplate depositJdbcTemplate) {
        this.chatClient = chatClient;
        this.customerJdbcTemplate = customerJdbcTemplate;
        this.depositJdbcTemplate = depositJdbcTemplate;
    }

    public QueryResponse processNaturalLanguageQuery(String question) {

        try {

            if (isCrossDatabaseQuery(question)) {
                return handleCrossDatabaseQuery(question);
            }

            String sqlQuery = generateSQLQuery(question);

            List<Map<String, Object>> data = executeSQLQuery(sqlQuery);

            String humanAnswer = generateHumanReadableResponse(question, data, sqlQuery);

            return new QueryResponse(question, sqlQuery, data, humanAnswer);
        } catch (Exception e) {
            return new QueryResponse(question, "Error processing query: " + e.getMessage());
        }

    }

    private QueryResponse handleCrossDatabaseQuery(String question) {
        try {
            String lowerQuestion = question.toLowerCase();

            if (lowerQuestion.contains("deposit") && lowerQuestion.contains("without") &&
                    (lowerQuestion.contains("person") || lowerQuestion.contains("customer") || lowerQuestion.contains("related"))) {
                return findDepositsWithoutRelatedCustomer();
            } else if (lowerQuestion.contains("most money") || lowerQuestion.contains("highest amount") ||
                    lowerQuestion.contains("richest customer")) {
                return findCustomerWithMostMoney();
            } else if (lowerQuestion.contains("total money") || lowerQuestion.contains("total deposits")) {
                return getTotalDepositsPerCustomer();
            } else if (lowerQuestion.contains("customers with deposits")) {
                return getCustomersWithDeposits();
            } else {
                // Default: try to get both customer and deposit info
                return getCombinedCustomerDepositInfo(question);
            }

        } catch (Exception e) {
            return new QueryResponse(question, "Error processing cross-database query: " + e.getMessage());
        }
    }


    private QueryResponse findCustomerWithMostMoney() {
        try {
            // Get all deposits grouped by customer_id with total amounts
            String depositQuery = "SELECT customer_id, SUM(amount) as total_amount FROM deposit GROUP BY customer_id ORDER BY total_amount DESC";
            List<Map<String, Object>> deposits = depositJdbcTemplate.queryForList(depositQuery);

            if (deposits.isEmpty()) {
                return new QueryResponse("Which customer has the most money?", depositQuery, deposits, "No deposits found.");
            }

            // Get the customer_id with the most money
            Long topCustomerId = ((Number) deposits.get(0).get("customer_id")).longValue();

            // Get customer details
            String customerQuery = "SELECT * FROM customer WHERE id = ?";
            List<Map<String, Object>> customers = customerJdbcTemplate.queryForList(customerQuery, topCustomerId);

            // Combine the results
            Map<String, Object> result = new HashMap<>();
            if (!customers.isEmpty()) {
                result.putAll(customers.get(0));
                result.put("TOTAL_AMOUNT", deposits.get(0).get("total_amount"));
            }

            List<Map<String, Object>> combinedData = List.of(result);

            // Generate human-readable response
            String humanAnswer = String.format("The customer with the most money is %s (ID: %d) with a total of %s.",
                    result.get("NAME"), result.get("ID"), result.get("TOTAL_AMOUNT"));

            return new QueryResponse("Which customer has the most money?",
                    "Combined queries: " + depositQuery + " + " + customerQuery,
                    combinedData, humanAnswer);

        } catch (Exception e) {
            return new QueryResponse("Which customer has the most money?", "Error in cross-database query: " + e.getMessage());
        }
    }

    private QueryResponse getTotalDepositsPerCustomer() {
        try {
            // Get all customers
            String customerQuery = "SELECT * FROM customer";
            List<Map<String, Object>> customers = customerJdbcTemplate.queryForList(customerQuery);

            // Get deposits grouped by customer
            String depositQuery = "SELECT customer_id, SUM(amount) as total_amount, COUNT(*) as deposit_count FROM deposit GROUP BY customer_id";
            List<Map<String, Object>> deposits = depositJdbcTemplate.queryForList(depositQuery);

            // Create a map for quick lookup
            Map<Long, Map<String, Object>> depositMap = new HashMap<>();
            for (Map<String, Object> deposit : deposits) {
                Long customerId = ((Number) deposit.get("customer_id")).longValue();
                depositMap.put(customerId, deposit);
            }

            // Combine customer and deposit data
            List<Map<String, Object>> combinedData = new ArrayList<>();
            for (Map<String, Object> customer : customers) {
                Map<String, Object> combined = new HashMap<>(customer);
                Long customerId = ((Number) customer.get("ID")).longValue();

                Map<String, Object> depositInfo = depositMap.get(customerId);
                if (depositInfo != null) {
                    combined.put("TOTAL_AMOUNT", depositInfo.get("total_amount"));
                    combined.put("DEPOSIT_COUNT", depositInfo.get("deposit_count"));
                } else {
                    combined.put("TOTAL_AMOUNT", 0);
                    combined.put("DEPOSIT_COUNT", 0);
                }
                combinedData.add(combined);
            }

            String humanAnswer = generateHumanReadableResponse("Show me total deposits per customer", combinedData,
                    "Combined: " + customerQuery + " + " + depositQuery);

            return new QueryResponse("Total deposits per customer",
                    "Combined: " + customerQuery + " + " + depositQuery,
                    combinedData, humanAnswer);

        } catch (Exception e) {
            return new QueryResponse("Total deposits per customer", "Error: " + e.getMessage());
        }
    }

    private String generateHumanReadableResponse(String question, List<Map<String, Object>> data) {
        return generateHumanReadableResponse(question, data, null);
    }

    private String generateHumanReadableResponse(String question, List<Map<String, Object>> data, String sqlQuery) {
        if (data.isEmpty()) {
            return "No results found for your query.";
        }

        String dataStr = data.toString();
        String queryInfo = sqlQuery != null ? String.format("\nSQL Query Executed: %s", sqlQuery) : "";

        String prompt = String.format("""
            Based on the following query results, provide a clear, human-readable answer to the user's question.
            Make the response conversational and easy to understand.
            
            Original Question: %s%s
            
            Query Results: %s
            
            Human-readable answer:
            """, question, queryInfo, dataStr);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content()
                .trim();
    }
    private QueryResponse getCustomersWithDeposits() {
        try {
            // Get customers who have deposits
            String depositQuery = "SELECT DISTINCT customer_id FROM deposit";
            List<Map<String, Object>> customerIds = depositJdbcTemplate.queryForList(depositQuery);

            if (customerIds.isEmpty()) {
                return new QueryResponse("Customers with deposits", depositQuery, customerIds, "No customers have deposits.");
            }

            // Get customer details for those who have deposits
            String ids = customerIds.stream()
                    .map(row -> row.get("customer_id").toString())
                    .collect(Collectors.joining(","));

            String customerQuery = "SELECT * FROM customer WHERE id IN (" + ids + ")";
            List<Map<String, Object>> customers = customerJdbcTemplate.queryForList(customerQuery);

            String humanAnswer = String.format("Found %d customers who have made deposits.", customers.size());

            return new QueryResponse("Customers with deposits",
                    "Combined: " + depositQuery + " + " + customerQuery,
                    customers, humanAnswer);

        } catch (Exception e) {
            return new QueryResponse("Customers with deposits", "Error: " + e.getMessage());
        }
    }

    private QueryResponse getCombinedCustomerDepositInfo(String question) {
        try {
            // Get all customers and all deposits, let the AI figure out how to present it
            String customerQuery = "SELECT * FROM customer";
            String depositQuery = "SELECT * FROM deposit";

            List<Map<String, Object>> customers = customerJdbcTemplate.queryForList(customerQuery);
            List<Map<String, Object>> deposits = depositJdbcTemplate.queryForList(depositQuery);

            // Combine into a summary format
            Map<String, Object> summary = new HashMap<>();
            summary.put("CUSTOMER_COUNT", customers.size());
            summary.put("DEPOSIT_COUNT", deposits.size());
            summary.put("CUSTOMERS", customers);
            summary.put("DEPOSITS", deposits);

            List<Map<String, Object>> combinedData = List.of(summary);

            String dataForAI = String.format("Customers: %s, Deposits: %s", customers, deposits);
            String prompt = String.format("""
                Based on the following customer and deposit data, answer the user's question in a clear way.
                
                Question: %s
                
                Customer Data: %s
                Deposit Data: %s
                
                Provide a helpful answer:
                """, question, customers, deposits);

            String humanAnswer = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content()
                    .trim();

            return new QueryResponse(question,
                    "Combined: " + customerQuery + " + " + depositQuery,
                    combinedData, humanAnswer);

        } catch (Exception e) {
            return new QueryResponse(question, "Error in combined query: " + e.getMessage());
        }
    }

    private QueryResponse findDepositsWithoutRelatedCustomer() {
        try {
            // Get all deposits
            String depositQuery = "SELECT * FROM deposit";
            List<Map<String, Object>> allDeposits = depositJdbcTemplate.queryForList(depositQuery);

            // Get all customer IDs
            String customerQuery = "SELECT id FROM customer";
            List<Map<String, Object>> customerIds = customerJdbcTemplate.queryForList(customerQuery);

            // Extract customer IDs into a Set for efficient lookup
            Set<Long> validCustomerIds = customerIds.stream()
                    .map(row -> ((Number) row.get("id")).longValue())
                    .collect(Collectors.toSet());

            // Filter deposits that don't have a corresponding customer
            List<Map<String, Object>> orphanedDeposits = allDeposits.stream()
                    .filter(deposit -> {
                        Long customerId = ((Number) deposit.get("customer_id")).longValue();
                        return !validCustomerIds.contains(customerId);
                    })
                    .collect(Collectors.toList());

            String humanAnswer;
            if (orphanedDeposits.isEmpty()) {
                humanAnswer = "Good news! All deposits have related customers. There are no orphaned deposits in the system.";
            } else {
                humanAnswer = String.format("Found %d deposit(s) without related customers. These deposits have customer_id values that don't exist in the customer database.",
                        orphanedDeposits.size());
            }

            return new QueryResponse("Is there any deposit without related person?",
                    "Combined: " + depositQuery + " + " + customerQuery,
                    orphanedDeposits, humanAnswer);

        } catch (Exception e) {
            return new QueryResponse("Is there any deposit without related person?",
                    "Error checking deposits: " + e.getMessage());
        }
    }

    private boolean isCrossDatabaseQuery(String question) {
        String lowerQuestion = question.toLowerCase();

        // Special case: deposits without related person/customer
        if ((lowerQuestion.contains("deposit") && lowerQuestion.contains("without") &&
                (lowerQuestion.contains("person") || lowerQuestion.contains("customer") || lowerQuestion.contains("related")))) {
            return true;
        }

        // Keywords that indicate customer information
        boolean hasCustomerKeywords = lowerQuestion.contains("customer") ||
                lowerQuestion.contains("name") ||
                lowerQuestion.contains("phone") ||
                lowerQuestion.contains("who");

        // Keywords that indicate deposit/money information
        boolean hasDepositKeywords = lowerQuestion.contains("money") ||
                lowerQuestion.contains("deposit") ||
                lowerQuestion.contains("amount") ||
                lowerQuestion.contains("balance") ||
                lowerQuestion.contains("total") ||
                lowerQuestion.contains("sum");

        return hasCustomerKeywords && hasDepositKeywords;
    }
    private List<Map<String, Object>> executeSQLQuery(String sqlQuery) {
        String cleanSql = cleanSqlQuery(sqlQuery);

        if(containsCustomerTables(cleanSql)){
            return customerJdbcTemplate.queryForList(cleanSql);
        } else if(containsDepositTables(cleanSql)){
            return depositJdbcTemplate.queryForList(cleanSql);
        } else {
            try {
                return customerJdbcTemplate.queryForList(cleanSql);
            } catch (Exception e) {
                return depositJdbcTemplate.queryForList(cleanSql);
            }
        }
    }

    private boolean containsDepositTables(String sql) {
        String lowerSql = sql.toLowerCase();
        return lowerSql.contains("deposit");
    }

    private boolean containsCustomerTables(String sql) {
        String lowerSql = sql.toLowerCase();
        return lowerSql.contains("customer") || lowerSql.contains("real_customer") || lowerSql.contains("legal_customer");
    }

    private String cleanSqlQuery(String sqlQuery) {
        // Remove markdown code blocks if present
        String cleaned = sqlQuery.replaceAll("```sql\\s*", "").replaceAll("```\\s*", "");

        // Extract SQL query part only (before any explanation text)
        // Look for patterns like "Query Strategy:", "Explanation:", etc.
        String[] lines = cleaned.split("\n");
        StringBuilder sqlBuilder = new StringBuilder();

        boolean foundSql = false;
        for (String line : lines) {
            line = line.trim();

            // Skip empty lines
            if (line.isEmpty()) continue;

            // Stop if we hit explanation sections
            if (line.toLowerCase().contains("query strategy") ||
                    line.toLowerCase().contains("explanation") ||
                    line.toLowerCase().contains("note:") ||
                    line.matches("^\\d+\\..*")) { // numbered explanations
                break;
            }

            // Check if this line looks like SQL
            if (line.toLowerCase().matches(".*\\b(select|insert|update|delete|create|drop|alter)\\b.*")) {
                foundSql = true;
            }

            // If we found SQL, add this line
            if (foundSql) {
                sqlBuilder.append(line).append(" ");
            }
        }

        cleaned = sqlBuilder.toString().trim();

        // If no SQL found, try to extract from the original response
        if (cleaned.isEmpty()) {
            // Look for SELECT, INSERT, UPDATE, DELETE statements
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                    "(SELECT.*?;|INSERT.*?;|UPDATE.*?;|DELETE.*?;)",
                    java.util.regex.Pattern.CASE_INSENSITIVE | java.util.regex.Pattern.DOTALL
            );
            java.util.regex.Matcher matcher = pattern.matcher(sqlQuery);
            if (matcher.find()) {
                cleaned = matcher.group(1);
            }
        }

        // Remove extra whitespace and newlines
        cleaned = cleaned.replaceAll("\\s+", " ").trim();

        // Ensure it ends with semicolon if it doesn't
        if (!cleaned.endsWith(";")) {
            cleaned += ";";
        }

        return cleaned;
    }

    private String generateSQLQuery(String question) {
        String databaseSchema = getDatabaseSchema();

        String prompt = String.format("""
                You are a SQL expert. Convert the following natural language question into a SQL query.
                
                Available Database Schema:
                %s
                
                IMPORTANT RULES:
                1. Return ONLY the SQL query, nothing else
                2. No explanations, no markdown, no additional text
                3. Customer and deposit data are in SEPARATE databases - DO NOT use JOINs between them
                4. If the question involves both customers and deposits, focus on ONE table only
                5. Use standard SQL syntax for a single table
                6. End the query with a semicolon
                
                Question: %s
                
                SQL:
                """, databaseSchema, question);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content()
                .trim();
    }

    private String getDatabaseSchema() {
        return """
                Customer Database Tables:
                - customer (id BIGINT PRIMARY KEY, name VARCHAR(255), phone_number VARCHAR(20), type VARCHAR(10))
                - real_customer (id BIGINT PRIMARY KEY, family VARCHAR(255), FOREIGN KEY (id) REFERENCES customer(id))
                - legal_customer (id BIGINT PRIMARY KEY, fax VARCHAR(20), FOREIGN KEY (id) REFERENCES customer(id))
                
                Deposit Database Tables:
                - deposit (id BIGINT PRIMARY KEY, amount NUMERIC(38,2), customer_id BIGINT, version BIGINT, currency ENUM('EUR','USD'))
                
                Note: customer_id in deposit table refers to id in customer table, but they are in separate databases.
                """;
    }
}
