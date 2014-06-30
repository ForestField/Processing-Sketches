// Example 11-09 from "Getting Started with Processing" 
// by Reas & Fry. O'Reilly / Make 2010

import processing.serial.*;

Serial port;  // Create object from Serial class
float val;    // Data received from the serial port
float angle;
float radius;

void setup() {
  size(440, 440);
  frameRate(30);
  strokeWeight(2);
  smooth();
  String arduinoPort = Serial.list()[0];
  port = new Serial(this, "COM4", 9600);
  background(0);
}

void draw() {
  if ( port.available() > 0) {  // If data is available,
    val = port.read();          // read it and store it in val
    println(val);
    // Convert the values to set the radius
    radius = map(val-36, 0, 255, 0, height * 3);  
  }

  int middleX = width/2;
  int middleY = height/2;
  float x = middleX + cos(val) * height * 10;
  float y = middleY + sin(val) * height * 10;
  stroke(0);
  line(middleX, middleY, x, y);
  
  x = middleX + cos(angle) * radius;
  y = middleY + sin(angle) * radius;
  stroke(255);
  line(middleX, middleY, x, y);
  
  angle += 0.01;
}