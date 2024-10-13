package com.example.restaurantmanagementapp.CartScreen;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.EphemeralKey;
import com.stripe.model.PaymentIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.EphemeralKeyCreateParams;
import com.stripe.param.PaymentIntentCreateParams;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class mock2 {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void  go(Callback<String> callback) {
        executorService.execute(() -> {
            try {
                Stripe.apiKey = "Secret Key :*";

                CustomerCreateParams customerParams = CustomerCreateParams.builder().build();
                Customer customer = Customer.create(customerParams);

                EphemeralKeyCreateParams ephemeralKeyParams = EphemeralKeyCreateParams.builder()
                        .setStripeVersion("2024-09-30.acacia")
                        .setCustomer(customer.getId())
                        .build();
                EphemeralKey ephemeralKey = EphemeralKey.create(ephemeralKeyParams);

                PaymentIntentCreateParams paymentIntentParams = PaymentIntentCreateParams.builder()
                        .setAmount(1099L)
                        .setCurrency("eur")
                        .setCustomer(customer.getId())
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                        .setEnabled(true)
                                        .build()
                        )
                        .build();
                PaymentIntent paymentIntent = PaymentIntent.create(paymentIntentParams);

                Map<String, String> responseData = new HashMap<>();
                responseData.put("paymentIntent", paymentIntent.getClientSecret());
                responseData.put("ephemeralKey", ephemeralKey.getSecret());
                responseData.put("customer", customer.getId());
                responseData.put("publishableKey", "Publishable key :*");

                // Call the callback with the result
                callback.onSuccess(new Gson().toJson(responseData));
            } catch (StripeException e) {
                e.printStackTrace();
                callback.onError(e);
            }
        });
    }

    // Callback interface for async results
    public interface Callback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }
}
