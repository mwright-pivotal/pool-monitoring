/*
  Repeating Wifi Web Client

 This sketch connects to a a web server and makes a request
 using a WiFi equipped Arduino board.

 created 23 April 2012
 modified 31 May 2012
 by Tom Igoe
 modified 13 Jan 2014
 by Federico Vanzati

 http://www.arduino.cc/en/Tutorial/WifiWebClientRepeating
 This code is in the public domain.
 */

#include <SPI.h>
#include <WiFiNINA.h> 
#include <Arduino_JSON.h>
#include <ArduinoHttpClient.h>

#define PhSensorPin A0            //pH meter Analog output to Arduino Analog Input 0
#define ORP_Pin A1 //ADC pin 1
#define TdsSensorPin A2
#define Offset 0.06            //deviation compensate
#define ORP_OFFSET 0             //zero drift voltage
#define LED 13
#define samplingInterval 20
#define ArrayLenth  40    //times of collection
#define voltage 5.00              //system voltage

int pHArray[ArrayLenth];   //Store the average value of the sensor feedback
int pHArrayIndex=0;

int tdsBuffer[ArrayLenth];    // store the analog value in the array, read from ADC
int tdsBufferTemp[ArrayLenth];
int tdsBufferIndex = 0,copyIndex = 0;
float averageVoltage = 0,tdsValue = 0,temperature = 25;

const String statusText1 = "signal strength (RSSI): ";
const String statusText2 = " dBm";

double ORP_Value;

int ORP_Array[ArrayLenth];
int ORP_ArrayIndex=0;

///////please enter your sensitive data in the Secret tab/arduino_secrets.h
char ssid[] = "";        // your network SSID (name)
char pass[] = "";    // your network password (use for WPA, or use as key for WEP)
int keyIndex = 0;            // your network key Index number (needed only for WEP)

int status = WL_IDLE_STATUS;

// server address:
char server[] = "pool-monitor.apps.cf4k8s.wrightcode.io";
//IPAddress server(64,131,82,241);
// Initialize the Wifi client library
WiFiSSLClient wifi;
HttpClient httpClient = HttpClient(wifi, server, 443);

unsigned long lastConnectionTime = 0;            // last time you connected to the server, in milliseconds
const unsigned long postingInterval = 30L * 1000L; // delay between updates, in milliseconds

void setup() {
  //Initialize serial and wait for port to open:
  Serial.begin(9600);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }

  // check for the WiFi module:
  if (WiFi.status() == WL_NO_MODULE) {
    Serial.println("Communication with WiFi module failed!");
    // don't continue
    while (true);
  }

  String fv = WiFi.firmwareVersion();
  if (fv < "1.0.0") {
    Serial.println("Please upgrade the firmware");
  }

  // attempt to connect to Wifi network:
  while (status != WL_CONNECTED) {
    Serial.print("Attempting to connect to SSID: ");
    Serial.println(ssid);
    // Connect to WPA/WPA2 network. Change this line if using open or WEP network:
    status = WiFi.begin(ssid, pass);

    // wait 10 seconds for connection:
    delay(10000);
  }
  // you're connected now, so print out the status:
  printWifiStatus();
}

void loop() {

  static unsigned long samplingTime = millis();
  static float pHValue,pHVoltage;
  char buff[11];
  
  // if there's incoming data from the net connection.
  // send it out the serial port.  This is for debugging
  // purposes only:
  while (wifi.available()) {
    char c = wifi.read();
    Serial.write(c);
  }

  if(millis()-samplingTime > samplingInterval)
  {
      pHArray[pHArrayIndex++]=analogRead(PhSensorPin);
      ORP_Array[ORP_ArrayIndex++]=analogRead(ORP_Pin);
      tdsBuffer[tdsBufferIndex++] = analogRead(TdsSensorPin);
      
      if(pHArrayIndex==ArrayLenth) {
        pHArrayIndex=0;
        ORP_ArrayIndex=0;
        tdsBufferIndex=0;
      }

      pHVoltage = avergearray(pHArray, ArrayLenth)*5.0/1024;
      pHValue = 3.5*pHVoltage+Offset;

      ORP_Value=((30*(double)voltage*1000)-(75*avergearray(ORP_Array, ArrayLenth)*voltage*1000/1024))/75-ORP_OFFSET;

      averageVoltage = avergearray(tdsBuffer,ArrayLenth) * (float)voltage / 1024.0; // read the analog value more stable by the median filtering algorithm, and convert to voltage value
      float compensationCoefficient=1.0+0.02*(temperature-25.0);    //temperature compensation formula: fFinalResult(25^C) = fFinalResult(current)/(1.0+0.02*(fTP-25.0));
      float compensationVolatge=averageVoltage/compensationCoefficient;  //temperature compensation
      tdsValue=(133.42*compensationVolatge*compensationVolatge*compensationVolatge - 255.86*compensationVolatge*compensationVolatge + 857.39*compensationVolatge)*0.5; //convert voltage value to tds value
      
      samplingTime=millis();
  }
  // if ten seconds have passed since your last connection,
  // then connect again and send data:
  if (millis() - lastConnectionTime > postingInterval) {
    JSONVar myObject;
    myObject["monitorUUID"] = "2050Windfaire";
    
    
    Serial.print("Voltage:");
    Serial.print(voltage);
    Serial.print("    pH value: ");
    Serial.print(pHValue,2);
    Serial.print(" orp value: ");
    Serial.println((float)ORP_Value);
    Serial.print("TDS Value:");
    Serial.print(tdsValue,0);
    Serial.println("ppm");
    digitalWrite(LED,digitalRead(LED)^1);

    buff[0] = '\0';
    dtostrf(pHValue, 4, 6, buff);  //4 is mininum width, 6 is precision
    myObject["phValue"] = buff;

    buff[0] = '\0';
    dtostrf((int)ORP_Value, 4, 6, buff);
    myObject["orpValue"] = buff;

    buff[0] = '\0';
    dtostrf((int)tdsValue, 4, 6, buff);
    myObject["tdsValue"] = buff;

    // print the received signal strength:
    long rssi = WiFi.RSSI();
    buff[0] = '\0';
    dtostrf((int)rssi, 4, 6, buff);
    myObject["textStatus"] =  statusText1 + buff + statusText2;

    String jsonString = JSON.stringify(myObject);
    httpRequest(jsonString);
  }

}

// this method makes a HTTP connection to the server:
void httpRequest(String status) {
  
  // close any connection before send a new request.
  // This will free the socket on the Nina module
  httpClient.stop();

  // if there's a successful connection:
  
  Serial.print("sending...");
  Serial.println(status);
  // send the HTTP PUT request:
  String contentType = "application/json";
  String postData = status;
  httpClient.post("/v1/api/status", contentType, postData);

    // read the status code and body of the response
  int statusCode = httpClient.responseStatusCode();
  String response = httpClient.responseBody();

  Serial.print("Status code: ");
  Serial.println(statusCode);
  Serial.print("Response: ");
  Serial.println(response);
    
  // note the time that the connection was made:
  lastConnectionTime = millis();
}

double avergearray(int* arr, int number){
  int i;
  int max,min;
  double avg;
  long amount=0;
  if(number<=0){
    Serial.println("Error number for the array to avraging!/n");
    return 0;
  }
  if(number<5){   //less than 5, calculated directly statistics
    for(i=0;i<number;i++){
      amount+=arr[i];
    }
    avg = amount/number;
    return avg;
  }else{
    if(arr[0]<arr[1]){
      min = arr[0];max=arr[1];
    }
    else{
      min=arr[1];max=arr[0];
    }
    for(i=2;i<number;i++){
      if(arr[i]<min){
        amount+=min;        //arr<min
        min=arr[i];
      }else {
        if(arr[i]>max){
          amount+=max;    //arr>max
          max=arr[i];
        }else{
          amount+=arr[i]; //min<=arr<=max
        }
      }//if
    }//for
    avg = (double)amount/(number-2);
  }//if
  return avg;
}

void printWifiStatus() {
  // print the SSID of the network you're attached to:
  Serial.print("SSID: ");
  Serial.println(WiFi.SSID());

  // print your board's IP address:
  IPAddress ip = WiFi.localIP();
  Serial.print("IP Address: ");
  Serial.println(ip);

  // print the received signal strength:
  long rssi = WiFi.RSSI();
  Serial.print("signal strength (RSSI):");
  Serial.print(rssi);
  Serial.println(" dBm");
}
