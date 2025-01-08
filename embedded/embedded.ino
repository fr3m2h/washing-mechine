#include <WiFi.h>
#include <HTTPClient.h>

const char* ssid = "Votre_SSID";
const char* password = "Votre_MotDePasse";
const char* serverUrl = "http://votre-backend.com/api/data";

void setup() {
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
  }
}

void loop() {
  if (WiFi.status() == WL_CONNECTED) {
    HTTPClient http;
    http.begin(serverUrl);
    http.addHeader("Content-Type", "application/json");
    String jsonData = "{\"machineId\":1, \"status\":\"active\", \"temperature\":45}";
    int httpResponseCode = http.POST(jsonData);
    http.end();
  }
  delay(10000); // Intervalle entre les envois
}
