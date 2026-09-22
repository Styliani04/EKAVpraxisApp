package com.example.ekavpraxis;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CloudVisionOcrHelper {

    private static final String API_URL =
            "https://vision.googleapis.com/v1/images:annotate?key=";

    private static final String API_KEY = "YOUR_GOOGLE_CLOUD_VISION_API_KEY";
    
    public interface OcrCallback {
        void onSuccess(String extractedText);
        void onFailure(String errorMessage);
    }

    public static void recognizeText(Context context, Uri imageUri, OcrCallback callback) {
        new Thread(() -> {
            try {
                // 1. Φόρτωση και συμπίεση εικόνας σε base64
                String base64Image = uriToBase64(context, imageUri);

                // 2. Κατασκευή JSON request
                JsonObject imageObject = new JsonObject();
                JsonObject contentObject = new JsonObject();
                contentObject.addProperty("content", base64Image);
                imageObject.add("image", contentObject);

                // DOCUMENT_TEXT_DETECTION = καλύτερο για έγγραφα με πολύ κείμενο
                JsonObject featureObject = new JsonObject();
                featureObject.addProperty("type", "DOCUMENT_TEXT_DETECTION");
                featureObject.addProperty("maxResults", 1);

                JsonObject imageContext = new JsonObject();
                JsonArray languageHints = new JsonArray();
                languageHints.add("el"); // Greek
                languageHints.add("el-GR");
                imageContext.add("languageHints", languageHints);

                JsonArray featuresArray = new JsonArray();
                featuresArray.add(featureObject);

                imageObject.add("features", featuresArray);
                imageObject.add("imageContext", imageContext);

                JsonArray requestsArray = new JsonArray();
                requestsArray.add(imageObject);

                JsonObject requestBody = new JsonObject();
                requestBody.add("requests", requestsArray);

                // 3. HTTP POST
                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(
                        requestBody.toString(),
                        MediaType.get("application/json; charset=utf-8")
                );

                Request request = new Request.Builder()
                        .url(API_URL + API_KEY)
                        .post(body)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        callback.onFailure("Σφάλμα σύνδεσης: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseBody = response.body().string();

                        try {
                            String text = parseVisionResponse(responseBody);
                            callback.onSuccess(text);
                        } catch (Exception e) {
                            callback.onFailure("Σφάλμα ανάλυσης απόκρισης: " + e.getMessage());
                        }
                    }
                });

            } catch (IOException e) {
                callback.onFailure("Σφάλμα φόρτωσης εικόνας: " + e.getMessage());
            }
        }).start();
    }

    private static String uriToBase64(Context context, Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        // Resize αν είναι πολύ μεγάλη (Cloud Vision limit: 20MB, αλλά μικρότερη = πιο γρήγορη)
        bitmap = resizeBitmap(bitmap, 2048);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
    }

    private static Bitmap resizeBitmap(Bitmap bitmap, int maxDimension) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width <= maxDimension && height <= maxDimension) return bitmap;

        float scale = (float) maxDimension / Math.max(width, height);
        int newWidth = Math.round(width * scale);
        int newHeight = Math.round(height * scale);

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }

    private static String parseVisionResponse(String jsonResponse) {
        JsonObject root = JsonParser.parseString(jsonResponse).getAsJsonObject();

        // Έλεγχος για σφάλμα API
        if (root.has("error")) {
            String errorMsg = root.getAsJsonObject("error")
                    .get("message").getAsString();
            throw new RuntimeException("API Error: " + errorMsg);
        }

        JsonArray responses = root.getAsJsonArray("responses");
        if (responses == null || responses.size() == 0) {
            return "Δεν βρέθηκε κείμενο";
        }

        JsonObject firstResponse = responses.get(0).getAsJsonObject();

        // DOCUMENT_TEXT_DETECTION βάζει το πλήρες κείμενο εδώ
        if (firstResponse.has("fullTextAnnotation")) {
            return firstResponse.getAsJsonObject("fullTextAnnotation")
                    .get("text").getAsString();
        }

        return "Δεν αναγνωρίστηκε κείμενο στο έγγραφο";
    }
}