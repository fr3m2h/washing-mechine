#include <Adafruit_Sensor.h>
#include <DHT.h>
#include <DHT_U.h>
#include <Adafruit_NeoPixel.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include <WiFi.h>
#include <HTTPClient.h>

// Définition des dimensions de l'écran OLED
#define SCREEN_WIDTH 128
#define SCREEN_HEIGHT 32
#define OLED_RESET    -1
Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, OLED_RESET);

// Pins et paramètres du capteur de courant
#define CURRENT_SENSOR_PIN 27  // Pin analogique du capteur de courant
#define CURRENT_THRESHOLD 0.5 // Seuil en ampères pour déterminer l'utilisation
#define ACS_OFFSET 2500       // Décalage (en mV) du capteur ACS712
#define MV_PER_AMP 100        // mV par ampère pour le capteur ACS712 (20A)

// Identifiant de la machine
const int MACHINE_ID = 1;
int mVperAmp = 100; // use 100 for 20A Module and 66 for 30A Module
int sensorValue= 0;
int ACSoffset = 2500;
double Voltage = 0;
double Amps = 0;

// Wi-Fi et serveur
const char* ssid = "SSID";
const char* password = "Mot_de_Passe";
const char* serverUrl = "http://localhost8080/api/";

// Variables globales
bool isUsed = false;
double timeLeft = 35.0; // Minuteur (en minutes)
double voltage = 0;
double current = 0;
unsigned long lastSendTime = 0; // Pour le contrôle d'envoi toutes les 2 minutes

void setup() {
  Serial.begin(115200);

  // Initialisation de l'écran OLED
  if (!display.begin(SSD1306_SWITCHCAPVCC, 0x3C)) {
    Serial.println(F("Échec de l'initialisation de l'écran OLED !"));
    while (true);
  }
  display.clearDisplay();
  display.display();

  // Connexion Wi-Fi
  Serial.println("Connexion au Wi-Fi...");
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("\nConnecté au Wi-Fi !");
}

void loop() {

  // Mise à jour de l'état de la machine
  bool previousIsUsed = isUsed;
  isUsed = (current > CURRENT_THRESHOLD);

  // Gestion du minuteur
  if (isUsed) {
    if (timeLeft > 0) {
      timeLeft -= 1.0 / 60.0; // Réduction de 1 seconde
    } else {
      timeLeft = 0;
    }
  } else if (previousIsUsed && !isUsed) {
    timeLeft = 35.0; // Réinitialisation du minuteur
  }

  // Mise à jour de l'écran OLED
  display.clearDisplay();
  display.setTextSize(1);
  display.setTextColor(SSD1306_WHITE);
  display.setCursor(0, 0);

  if (isUsed) {
    display.println("Machine: Utilisee");
    display.setCursor(0, 10);
    display.print("Temps restant: ");
    display.print(timeLeft, 2);
    display.println(" min");
  } else {
    display.println("Machine: Disponible");
  }

  display.setCursor(0, 20);
  display.print("Courant: ");
  display.print(current, 2);
  display.println(" A");
  display.display();

  sensorValue = analogRead(CURRENT_SENSOR_PIN);
  Voltage = (sensorValue / 1024.0) * 5000; // Gets you mV
  Amps = ((Voltage - ACSoffset) / mVperAmp);
  Serial.print("Raw Value = " ); // shows pre-scaled value
  Serial.print(sensorValue);
  Serial.print("\t mV = "); // shows the voltage measured
  Serial.print(Voltage,3); // the '3' after voltage allows you to display 3 digits after decimalpoint
  Serial.print("\t Amps = "); // shows the voltage measured
  Serial.println(Amps,3); // the '3' after voltage allows you to display 3 digits after decimalpoint

  // Envoi au serveur toutes les 2 minutes
  if (millis() - lastSendTime > 120000) { // 2 minutes en millisecondes
    sendStatusToServer();
    lastSendTime = millis();
  }

  delay(1000); // Attente de 1 seconde
}

//Nous avons eu des problèmes à lancer la connexion au Wifi car nous le Wifi de la ME requiert une authentification.

void sendStatusToServer() {
  if (WiFi.status() == WL_CONNECTED) {
    HTTPClient http;
    http.begin(serverUrl);
    http.addHeader("Content-Type", "application/json");

    // Création du payload JSON
    String payload = "{\"machineId\": " + String(MACHINE_ID) + ", ";
    payload += "\"isUsed\": " + String(isUsed ? "true" : "false") + ", ";
    if (isUsed) {
      payload += "\"timeLeft\": " + String(timeLeft, 2);
    } else {
      payload += "\"timeLeft\": null";
    }
    payload += "}";

    // Envoi de la requête POST
    int httpResponseCode = http.POST(payload);

    // Vérification de la réponse
    if (httpResponseCode > 0) {
      String response = http.getString();
      Serial.println("Réponse du serveur : " + response);
    } else {
      Serial.print("Erreur lors de l'envoi : ");
      Serial.println(httpResponseCode);
    }

    http.end();
  } else {
    Serial.println("Erreur : Non connecté au Wi-Fi !");
  }
}