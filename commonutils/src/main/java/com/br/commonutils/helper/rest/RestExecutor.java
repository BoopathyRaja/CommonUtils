package com.br.commonutils.helper.rest;

import android.net.Uri;
import android.os.AsyncTask;

import com.br.commonutils.log.Logger;
import com.br.commonutils.provider.Task;
import com.br.commonutils.util.ConnectionUtil;
import com.br.commonutils.validator.Validator;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

class RestExecutor<T> extends AsyncTask<Param, Void, Result<T>> {

    private static final String TAG = RestExecutor.class.getSimpleName();
    private final String charset = "UTF-8";
    private final String BOUNDARY = "**********";
    private final String TWO_HYPHENS = "--";
    private final String LINE_FEED = "\n";

    private final String JSON_MEDIA_TYPE = "application/json";
    private final String JSON_MULTIPART = "multipart/form-data; boundary=" + BOUNDARY;

    private final String baseUri;
    private final Task task;
    private String uri;
    private Object payload;
    private Type responseType;
    private MethodType methodType;
    private BodyType bodyType;

    private Gson gson;

    public RestExecutor(String baseUri, Task task) {
        this.baseUri = baseUri;
        this.task = task;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public void setResponseType(Type responseType) {
        this.responseType = responseType;
    }

    public void setMethodType(MethodType methodType) {
        this.methodType = methodType;
    }

    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    @Override
    protected Result<T> doInBackground(Param... params) {
        Result<T> retVal = new Result<>();

        try {
            if (ConnectionUtil.isInternetEnabled()) {
                Uri.Builder builder = Uri.parse(baseUri).buildUpon();

                if (uri.contains("/")) {
                    String[] split = uri.split("/");

                    for (String s : split) {
                        builder.appendPath(s);
                    }
                } else {
                    builder.appendPath(uri);
                }

                if (Validator.isValid(params) && params.length > 0) {
                    for (Param param : params) {
                        if (param instanceof QueryParam)
                            builder.appendQueryParameter(param.getName(), param.getValue());
                    }
                }

//                Authenticator.setDefault(new Authenticator() {
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication("root", "5864e86b-1646-492a-86e5-c562d3bad267".toCharArray());
//                    }
//                });

                URL url = new URL(builder.build().toString());
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setUseCaches(false);

                switch (bodyType) {
                    case JSON:
                        httpURLConnection.setRequestProperty("Content-Type", JSON_MEDIA_TYPE);
                        break;

                    case FORM_DATA:
                        httpURLConnection.setRequestProperty("Content-Type", JSON_MULTIPART);
                        break;
                }

                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestMethod(methodType.name().toUpperCase());
                httpURLConnection.setConnectTimeout(5 * 1000);

                if (Validator.isValid(params) && params.length > 0) {
                    for (Param param : params) {
                        if (param instanceof HeaderParam)
                            httpURLConnection.addRequestProperty(param.getName(), param.getValue());
                    }
                }

                if (Validator.isValid(payload)) {
                    DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());

                    switch (bodyType) {
                        case JSON: {
                            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(dataOutputStream, charset));
                            bufferedWriter.write(gson.toJson(payload));
                            bufferedWriter.flush();
                            bufferedWriter.close();
                        }
                        break;

                        case FORM_DATA: {
                            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(dataOutputStream, charset), true);

                            Class<?> componentClass = payload.getClass();
                            for (Field field : componentClass.getDeclaredFields()) {
                                field.setAccessible(true);

                                String filedName = field.getName();
                                Object filedValue = field.get(payload);

                                SerializedName serializedName = field.getAnnotation(SerializedName.class);
                                if (Validator.isValid(serializedName))
                                    filedName = serializedName.value();

                                if (filedValue instanceof File)
                                    addFilePart(printWriter, dataOutputStream, filedName, (File) filedValue);
                                else if (filedValue instanceof String)
                                    addFormField(printWriter, filedName, (String) filedValue);
                                else
                                    addFormField(printWriter, filedName, gson.toJson(filedValue));
                            }

                            printWriter.append(TWO_HYPHENS + BOUNDARY + TWO_HYPHENS);
                            printWriter.close();
                        }
                        break;
                    }

                    dataOutputStream.close();
                }

                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK
                        || responseCode == HttpsURLConnection.HTTP_CREATED
                        || responseCode == HttpsURLConnection.HTTP_ACCEPTED) {
                    InputStream inputStream = httpURLConnection.getInputStream();

                    Result serverResult = getResult(inputStream);
                    if (Validator.isValid(serverResult)) {
                        if (serverResult.isStatus()) {
                            retVal.setStatus(true);

                            if (responseType == Void.class) {
                                retVal.setData(null);
                            } else {
                                Object data = serverResult.getData();
                                if (Validator.isValid(data)) {
                                    String jsonData = gson.toJson(data);
                                    retVal.setData(gson.fromJson(jsonData, responseType));
                                }
                            }
                        } else {
                            retVal.setMessage(serverResult.getMessage());
                        }
                    } else {
                        retVal.setMessage("No data found");
                    }
                } else {
                    InputStream errorStream = httpURLConnection.getErrorStream();
                    if (Validator.isValid(errorStream)) {
                        Result result = getResult(errorStream);
                        retVal.setMessage(result.getMessage());
                    } else {
                        retVal.setMessage(httpURLConnection.getResponseMessage() + " (Code " + responseCode + ")");
                    }
                }

                httpURLConnection.disconnect();
            } else {
                retVal.setMessage("Oops... Looks like we have a network info! Please check your internet connection");
            }
        } catch (Exception e) {
            retVal.setMessage(e.getLocalizedMessage());
        }

        return retVal;
    }

    @Override
    protected void onPostExecute(Result<T> result) {
        if (result.isStatus())
            task.success(result.getData());
        else
            task.failure(result.getMessage());
    }

    private void addFormField(PrintWriter printWriter, String name, String value) {
        printWriter.append(TWO_HYPHENS + BOUNDARY + LINE_FEED);
        printWriter.append("Content-Disposition: form-data; name=\"" + name + "\"" + LINE_FEED);
        printWriter.append("Content-Type: text/plain; charset=" + charset + LINE_FEED);
        printWriter.append(LINE_FEED);
        printWriter.append(value + LINE_FEED);
        printWriter.flush();
    }

    private void addFilePart(PrintWriter printWriter, OutputStream outputStream, String fieldName, File uploadFile) throws IOException {
        String fileName = uploadFile.getName();

        printWriter.append(TWO_HYPHENS + BOUNDARY + LINE_FEED);
        printWriter.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"" + LINE_FEED);
        printWriter.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName) + LINE_FEED);
        printWriter.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        printWriter.append(LINE_FEED);
        printWriter.flush();

        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.flush();
        inputStream.close();

        printWriter.append(LINE_FEED);
        printWriter.flush();
    }

    private Result getResult(InputStream inputStream) {
        Result retVal = null;

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }

            bufferedReader.close();

            String responseData = response.toString();
            Logger.logInfo(TAG, responseData);

            retVal = gson.fromJson(responseData, Result.class);
        } catch (Exception e) {

        }

        return retVal;
    }
}