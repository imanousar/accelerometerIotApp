package com.example.giannis.myapplication1;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MQTT {


    public MqttAndroidClient mqttAndroidClient;

    final String serverUri = "tcp://demo.thingsboard.io";
    private final String username = "rroaEAa4CD6fWpa0bFhd";
    final String publishTopic = "v1/devices/me/telemetry";


    public MQTT(Context context) {
        mqttAndroidClient = new MqttAndroidClient(context, serverUri, MqttClient.generateClientId(), new MemoryPersistence());
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                Log.w("Mqtt", s);
            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Mqtt", mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    public void setCallback(MqttCallbackExtended callback) {
        mqttAndroidClient.setCallback(callback);
    }

    public void connect(){

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setUserName(username);
        try {

            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.w("Mqtt", "Successful connection to : " + serverUri);
                    MainActivity.connected = true;
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("Mqtt", "Failed to connect to: " + serverUri + exception.toString());
                }
            });


        } catch (MqttException ex){
            ex.printStackTrace();
        }
    }

    public void disconnect(){
        try {
            mqttAndroidClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public void publish(float x, float y, float z) throws JSONException, UnsupportedEncodingException {
        try {

            JSONObject payload = new JSONObject();
            payload.put("X Axis", String.format("%f", x));
            payload.put("Y Axis", String.format("%f", y));
            payload.put("Z Axis", String.format("%f", z));

            MqttMessage message= new MqttMessage(payload.toString().getBytes());
            Log.w("Mqtt", "Publishing...");
            mqttAndroidClient.publish(publishTopic, message);

        } catch (MqttException ex) {
            System.err.println("Exception whilst publishing");
            ex.printStackTrace();
        }
    }


}
