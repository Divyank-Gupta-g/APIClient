package com.APIClient;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.*;

		public class APIClient {

		    private static String BASE_URL = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp";
		    private static String AUTH_URL = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp";
		    private static String bearerToken;

		    public static void main(String[] args) {
		        //Authenticate user and get bearer token
		        authenticateUser("test@sunbasedata.com", "Test@123");

		        //Create a new customer
		        createCustomer("Jane", "Doe", "Elvnu Street", "H no 2", "Delhi", "Delhi", "sam@gmail.com", "12345678");

		        //Get customer list
		        getCustomerList();

		        //Delete a customer (replace "customer_uuid" with an actual UUID)
		        deleteCustomer("customer_uuid");

		        //Update a customer (replace "customer_uuid" with an actual UUID)
		        updateCustomer("customer_uuid", "Jane", "Doe", "Elvnu Street", "H no 2", "Delhi", "Delhi", "sam@gmail.com", "12345678");
		    }

		    @SuppressWarnings("deprecation")
			private static void authenticateUser(String loginId, String password) {
		        try {
		            URL url = new URL(AUTH_URL);
		            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		            connection.setRequestMethod("POST");
		            connection.setRequestProperty("Content-Type", "application/json");
		            connection.setDoOutput(true);

		            String body = String.format("{\"login_id\": \"%s\", \"password\": \"%s\"}", loginId, password);

		            try (OutputStream os = connection.getOutputStream()) {
		                os.write(body.getBytes("utf-8"));
		            }

		            int responseCode = connection.getResponseCode();
		            if (responseCode == 200) {
		                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
		                    bearerToken = br.readLine();
		                    System.out.println("Authentication successful. Bearer Token: " + bearerToken);
		                }
		            } else {
		                System.out.println("Authentication failed. Response Code: " + responseCode);
		            }
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }

		    @SuppressWarnings("deprecation")
			private static void createCustomer(String firstName, String lastName, String street, String address,
		                                       String city, String state, String email, String phone) {
		        try {
		            URL url = new URL(BASE_URL + "?cmd=create");
		            HttpURLConnection connection = createConnection(url);

		            String body = String.format(
		                    "{\"first_name\": \"%s\", \"last_name\": \"%s\", \"street\": \"%s\", \"address\": \"%s\", " +
		                            "\"city\": \"%s\", \"state\": \"%s\", \"email\": \"%s\", \"phone\": \"%s\"}",
		                    firstName, lastName, street, address, city, state, email, phone);

		            sendRequest(connection, body, "Customer created successfully.", "Failed to create customer.");
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }

		    @SuppressWarnings("deprecation")
			private static void getCustomerList() {
		        try {
		            URL url = new URL(BASE_URL + "?cmd=get_customer_list");
		            HttpURLConnection connection = createConnection(url);

		            int responseCode = connection.getResponseCode();
		            if (responseCode == 200) {
		                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
		                    System.out.println("Customer List: " + br.readLine());
		                }
		            } else {
		                System.out.println("Failed to get customer list. Response Code: " + responseCode);
		            }
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }

		    @SuppressWarnings("deprecation")
			private static void deleteCustomer(String customerUuid) {
		        try {
		            URL url = new URL(BASE_URL + "?cmd=delete&uuid=" + customerUuid);
		            HttpURLConnection connection = createConnection(url);

		            sendRequest(connection, null, "Customer deleted successfully.", "Failed to delete customer.");
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }

		    @SuppressWarnings("deprecation")
			private static void updateCustomer(String customerUuid, String firstName, String lastName, String street,
		                                       String address, String city, String state, String email, String phone) {
		        try {
		            URL url = new URL(BASE_URL + "?cmd=update&uuid=" + customerUuid);
		            HttpURLConnection connection = createConnection(url);

		            String body = String.format(
		                    "{\"first_name\": \"%s\", \"last_name\": \"%s\", \"street\": \"%s\", \"address\": \"%s\", " +
		                            "\"city\": \"%s\", \"state\": \"%s\", \"email\": \"%s\", \"phone\": \"%s\"}",
		                    firstName, lastName, street, address, city, state, email, phone);

		            sendRequest(connection, body, "Customer updated successfully.", "Failed to update customer.");
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }

		    private static HttpURLConnection createConnection(URL url) throws IOException {
		        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		        connection.setRequestMethod("POST");
		        connection.setRequestProperty("Authorization", "Bearer " + bearerToken);
		        connection.setRequestProperty("Content-Type", "application/json");
		        connection.setDoOutput(true);
		        return connection;
		    }

		    private static void sendRequest(HttpURLConnection connection, String requestBody, String successMessage, String errorMessage) throws IOException {
		        if (requestBody != null) {
		            try (OutputStream os = connection.getOutputStream()) {
		                os.write(requestBody.getBytes("utf-8"));
		            }
		        }

		        int responseCode = connection.getResponseCode();
		        if (responseCode == 200) {
		            System.out.println(successMessage);
		        } else {
		            System.out.println(errorMessage + " Response Code: " + responseCode);
		        }
		    }
		}
