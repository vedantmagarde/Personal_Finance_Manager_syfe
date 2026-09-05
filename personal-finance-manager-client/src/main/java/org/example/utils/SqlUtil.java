package org.example.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.example.models.Transaction;
import org.example.models.TransactionCategory;
import org.example.models.User;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SqlUtil {

    // =========================
    // GET METHODS
    // =========================

    public static User getUserByEmail(String userEmail) {
        HttpURLConnection conn = null;

        try {
            conn = ApiUtil.fetchApi(
                    "/api/v1/user?email=" + userEmail,
                    ApiUtil.RequestMethod.GET,
                    null);

            if (conn.getResponseCode() != 200) {
                System.out.println("Error(getUserByEmail): " + conn.getResponseCode());
                return null;
            }

            String userDataJson = ApiUtil.readApiResponse(conn);

            JsonObject jsonObject = JsonParser.parseString(userDataJson).getAsJsonObject();

            int id = jsonObject.get("id").getAsInt();
            String name = jsonObject.get("name").getAsString();
            String email = jsonObject.get("email").getAsString();
            String password = jsonObject.get("password").getAsString();

            LocalDateTime createdAt = new Gson().fromJson(
                    jsonObject.get("created_at"),
                    LocalDateTime.class);

            return new User(id, name, email, password, createdAt);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return null;
    }

    public static List<TransactionCategory> getAllTransactionCategoriesByUser(User user) {
        List<TransactionCategory> categories = new ArrayList<>();
        HttpURLConnection conn = null;

        try {
            conn = ApiUtil.fetchApi(
                    "/api/v1/transaction-category/user/" + user.getId(),
                    ApiUtil.RequestMethod.GET,
                    null);

            if (conn.getResponseCode() != 200) {
                System.out.println("Error(getAllTransactionCategoriesByUser): " + conn.getResponseCode());
                return null;
            }

            String result = ApiUtil.readApiResponse(conn);

            JsonArray resultJsonArray = JsonParser.parseString(result).getAsJsonArray();

            for (JsonElement jsonElement : resultJsonArray) {
                JsonObject categoryObject = jsonElement.getAsJsonObject();

                int categoryId = categoryObject.get("id").getAsInt();
                String categoryName = categoryObject.get("categoryName").getAsString();
                String categoryColor = categoryObject.get("categoryColor").getAsString();

                categories.add(new TransactionCategory(
                        categoryId,
                        categoryName,
                        categoryColor));
            }

            return categories;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return null;
    }

    public static List<Transaction> getRecentTransactionByUserId(
            int userId,
            int startPage,
            int endPage,
            int size) {
        List<Transaction> recentTransactions = new ArrayList<>();
        HttpURLConnection conn = null;

        try {
            conn = ApiUtil.fetchApi(
                    "/api/v1/transaction/recent/user/"
                            + userId
                            + "?startPage="
                            + startPage
                            + "&endPage="
                            + endPage
                            + "&size="
                            + size,
                    ApiUtil.RequestMethod.GET,
                    null);

            if (conn.getResponseCode() != 200) {
                System.out.println("Error(getRecentTransactionByUserId): " + conn.getResponseCode());
                return null;
            }

            String results = ApiUtil.readApiResponse(conn);

            JsonArray resultJsonArray = JsonParser.parseString(results).getAsJsonArray();

            for (JsonElement jsonElement : resultJsonArray) {
                JsonObject transactionJsonObj = jsonElement.getAsJsonObject();

                recentTransactions.add(parseTransaction(transactionJsonObj));
            }

            return recentTransactions;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return null;
    }

    public static List<Transaction> getAllTransactionsByUserId(
            int userId,
            int year,
            Integer month) {
        List<Transaction> transactions = new ArrayList<>();
        HttpURLConnection conn = null;

        try {
            String apiUrl = "/api/v1/transaction/user/"
                    + userId
                    + "?year="
                    + year;

            if (month != null) {
                apiUrl += "&month=" + month;
            }

            conn = ApiUtil.fetchApi(
                    apiUrl,
                    ApiUtil.RequestMethod.GET,
                    null);

            if (conn.getResponseCode() != 200) {
                System.out.println("Error(getAllTransactionsByUserId): " + conn.getResponseCode());
                return null;
            }

            String results = ApiUtil.readApiResponse(conn);

            JsonArray resultJsonArray = JsonParser.parseString(results).getAsJsonArray();

            for (JsonElement jsonElement : resultJsonArray) {
                JsonObject transactionJsonObj = jsonElement.getAsJsonObject();

                transactions.add(parseTransaction(transactionJsonObj));
            }

            return transactions;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return null;
    }

    public static List<Integer> getAllDistinctYears(int userId) {
        List<Integer> years = new ArrayList<>();
        HttpURLConnection conn = null;

        try {
            conn = ApiUtil.fetchApi(
                    "/api/v1/transaction/user/" + userId + "/years",
                    ApiUtil.RequestMethod.GET,
                    null);

            if (conn.getResponseCode() != 200) {
                System.out.println("Error(getAllDistinctYears): " + conn.getResponseCode());
                return null;
            }

            String results = ApiUtil.readApiResponse(conn);

            JsonArray resultJsonArray = JsonParser.parseString(results).getAsJsonArray();

            for (JsonElement jsonElement : resultJsonArray) {
                years.add(jsonElement.getAsInt());
            }

            return years;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return null;
    }

    // =========================
    // POST METHODS
    // =========================

    public static boolean postLoginUser(String email, String password) {
        HttpURLConnection conn = null;

        try {
            conn = ApiUtil.fetchApi(
                    "/api/v1/user/login?email="
                            + email
                            + "&password="
                            + password,
                    ApiUtil.RequestMethod.POST,
                    null);

            return conn.getResponseCode() == 200;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return false;
    }

    public static boolean postCreateUser(JsonObject userData) {
        HttpURLConnection conn = null;

        try {
            conn = ApiUtil.fetchApi(
                    "/api/v1/user",
                    ApiUtil.RequestMethod.POST,
                    userData);

            return conn.getResponseCode() == 200;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return false;
    }

    public static boolean postTransactionCategory(JsonObject transactionCategoryData) {
        HttpURLConnection conn = null;

        try {
            conn = ApiUtil.fetchApi(
                    "/api/v1/transaction-category",
                    ApiUtil.RequestMethod.POST,
                    transactionCategoryData);

            return conn.getResponseCode() == 200;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return false;
    }

    public static boolean postTransaction(JsonObject transactionData) {
        HttpURLConnection conn = null;

        try {
            conn = ApiUtil.fetchApi(
                    "/api/v1/transaction",
                    ApiUtil.RequestMethod.POST,
                    transactionData);

            return conn.getResponseCode() == 200;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return false;
    }

    // =========================
    // PUT METHODS
    // =========================

    public static boolean putTransaction(JsonObject newTransactionData) {
        HttpURLConnection conn = null;

        try {
            conn = ApiUtil.fetchApi(
                    "/api/v1/transaction",
                    ApiUtil.RequestMethod.PUT,
                    newTransactionData);

            if (conn.getResponseCode() != 200) {
                System.out.println("Error(putTransaction): " + conn.getResponseCode());
                return false;
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return false;
    }

    public static boolean putTransactionCategory(
            int categoryId,
            String newCategoryName,
            String newCategoryColor) {
        HttpURLConnection conn = null;

        String encodedCategoryName = URLEncoder.encode(
                newCategoryName,
                StandardCharsets.UTF_8);

        String encodedCategoryColor = URLEncoder.encode(
                newCategoryColor,
                StandardCharsets.UTF_8);

        try {
            conn = ApiUtil.fetchApi(
                    "/api/v1/transaction-category/"
                            + categoryId
                            + "?newCategoryName="
                            + encodedCategoryName
                            + "&newCategoryColor="
                            + encodedCategoryColor,
                    ApiUtil.RequestMethod.PUT,
                    null);

            if (conn.getResponseCode() != 200) {
                System.out.println("Error(putTransactionCategory): " + conn.getResponseCode());
                return false;
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return false;
    }

    // =========================
    // DELETE METHODS
    // =========================

    public static boolean deleteTransactionCategoryById(int categoryId) {
        HttpURLConnection conn = null;

        try {
            conn = ApiUtil.fetchApi(
                    "/api/v1/transaction-category/" + categoryId,
                    ApiUtil.RequestMethod.DELETE,
                    null);

            if (conn.getResponseCode() != 200) {
                System.out.println("Error(deleteTransactionCategoryById): " + conn.getResponseCode());
                return false;
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return false;
    }

    public static boolean deleteTransactionById(int transactionId) {
        HttpURLConnection conn = null;

        try {
            conn = ApiUtil.fetchApi(
                    "/api/v1/transaction/" + transactionId,
                    ApiUtil.RequestMethod.DELETE,
                    null);

            if (conn.getResponseCode() != 200) {
                System.out.println("Error(deleteTransactionById): " + conn.getResponseCode());
                return false;
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return false;
    }

    // =========================
    // HELPER METHOD
    // =========================

    private static Transaction parseTransaction(JsonObject transactionJsonObj) {
        int transactionId = transactionJsonObj.get("id").getAsInt();

        TransactionCategory transactionCategory = null;

        if (transactionJsonObj.has("transactionCategory")
                && !transactionJsonObj.get("transactionCategory").isJsonNull()) {

            JsonObject transactionCategoryJsonObj = transactionJsonObj.get("transactionCategory").getAsJsonObject();

            int transactionCategoryId = transactionCategoryJsonObj.get("id").getAsInt();
            String transactionCategoryName = transactionCategoryJsonObj.get("categoryName").getAsString();
            String transactionCategoryColor = transactionCategoryJsonObj.get("categoryColor").getAsString();

            transactionCategory = new TransactionCategory(
                    transactionCategoryId,
                    transactionCategoryName,
                    transactionCategoryColor);
        }

        String transactionName = transactionJsonObj.get("transactionName").getAsString();
        double transactionAmount = transactionJsonObj.get("transactionAmount").getAsDouble();

        LocalDate transactionDate = LocalDate.parse(
                transactionJsonObj.get("transactionDate").getAsString());

        String transactionType = transactionJsonObj.get("transactionType").getAsString();

        return new Transaction(
                transactionId,
                transactionCategory,
                transactionName,
                transactionAmount,
                transactionDate,
                transactionType);
    }
}