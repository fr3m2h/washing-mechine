#include <Adafruit_Sensor.h>
#include <DHT.h>
#include <DHT_U.h>
#include <Adafruit_NeoPixel.h> // Ajout pour WS2812B
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include <WiFi.h>
#include <Wire.h>
#include <PubSubClient.h>

#define SCREEN_WIDTH 128 // OLED display width, in pixels
#define SCREEN_HEIGHT 32 // OLED display height, in pixels (corrigé pour 32 pixels de hauteur)
#define OLED_RESET    -1 // Reset pin # (or -1 if sharing Arduino reset pin)
Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, OLED_RESET);

// Pins et paramètres du capteur de courant
#define DHTPIN A0  // Port du capteur de courant
#define CURRENT_THRESHOLD 0.5 // Ajustez selon votre capteur et moteur

const char* ssid = "Votre_SSID";
const char* password = "Votre_MotDePasse";
const char* serverUrl = "http://votre-backend.com/api/data";

// Paramètres réseau
const char* ssid = "Your_SSID";
const char* password = "Your_PASSWORD";
const char* mqtt_server = "Your_MQTT_BROKER_IP";
const int mqtt_port = 1883;
const char* mqtt_topic = "laverie/machine/status";

WiFiClient espClient;
PubSubClient client(espClient);

// Variable d'état
bool isUsed = false;

void connectToWiFi() {
  Serial.print("Connexion au WiFi");
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("\nConnecté au WiFi !");
}

void reconnectMQTT() {
  while (!client.connected()) {
    Serial.print("Connexion à MQTT...");
    if (client.connect("ESP32Client")) {
      Serial.println("Connecté à MQTT !");
    } else {
      Serial.print("Échec, rc=");
      Serial.print(client.state());
      Serial.println(" ; nouvelle tentative dans 5 secondes.");
      delay(5000);
    }
  }
}

void setup() {
  Serial.begin(115200);

  // Initialisation de l'écran OLED
  if (!display.begin(SSD1306_SWITCHCAPVCC, 0x3C)) {
    Serial.println(F("Échec de l'initialisation de l'écran OLED !"));
    while (true);
  }
  display.clearDisplay();
  display.display();

  // Connexion au WiFi et au serveur MQTT
  connectToWiFi();
  client.setServer(mqtt_server, mqtt_port);
}

void loop() {
  if (!client.connected()) {
    reconnectMQTT();
  }
  client.loop();

  // Lecture du capteur de courant
  float sensorValue = analogRead(CURRENT_SENSOR_PIN);
  float current = sensorValue * (5.0 / 1023.0); // Conversion en tension (ajustez selon l'ESP32)

  // Mise à jour de l'état
  bool previousIsUsed = isUsed;
  isUsed = current > CURRENT_THRESHOLD;

  if (isUsed) {
    // Décrémente le minuteur uniquement si la machine est utilisée
    if (timeLeft > 0) {
      timeLeft -= 1.0 / 60.0; // Réduction de 1 seconde (1/60 minute)
    } else {
      timeLeft = 0; // Empêche les valeurs négatives
    }
  } else if (previousIsUsed && !isUsed) {
    // Réinitialise le minuteur lorsque la machine s'arrête
    timeLeft = 35.0;
  }

  // Mise à jour de l'écran OLED
  display.clearDisplay();
  display.setTextSize(1);
  display.setTextColor(SSD1306_WHITE);
  display.setCursor(0, 0);

  if (isUsed) {
    display.print("Machine: Utilisee");
    display.setCursor(0, 10);
    display.print("Temps restant: ");
    display.print(timeLeft, 2);
    display.print(" min");
  } else {
    display.print("Machine: Disponible");
  }

  display.display();

  // Envoi des données au backend
  String payload = "{\"isUsed\": " + String(isUsed ? "true" : "false") + ", \"timeLeft\": " + String(timeLeft, 2) + "}";
  client.publish(mqtt_topic, payload.c_str());

  delay(1000); // Attente d'une seconde
}
