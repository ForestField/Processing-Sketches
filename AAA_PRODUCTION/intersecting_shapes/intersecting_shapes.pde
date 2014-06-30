//processing serial library:
import processing.serial.*;
//minim sound synthesis libraries:
import ddf.minim.*;
import ddf.minim.signals.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;

Serial port;  // Create object from Serial class
//Signal Strength Data Received from Serial Port
int rssi;// signal strength received from the the transmitting dancer's 
// xbee module. rssi range is between 36 and 80. 36 is, like, 0. 

//Boxcar average Variables:
int window_size=40;//size of the Box car array to average
int[] readings = new int[window_size]; //create an array called "readings" that will be averaged
int index = 0;//start at the first index of the array
int total = 0;//total will = total- readings[index]
float average = 0; //average will = total/window_size
int low_rssi = 36;//lowest rssi value->maps to 1
int high_rssi = 80;// highest rssi value-> maps to width
PFont font;
//Audio Variables:
int onCount = 0;//"On" Counter, counts the time that performers have been close
//and is used to increment the volume the longer they've been near each other

int offCount = 0;//"Off" Counter, counts the time that performers have been apart
//and is used to decrement the volume the longer they've been apart

int onThreshold = 0;//set the threshold that the "On" Counter has to reach before the volume increments
int offThreshold = 1;//set the threshold that the "Off" Counter has to reach before the volume decrements

float onIncrement = 2;//amount volume is increased per near time step
float offIncrement = .7;//amount volume is decresead per away time step
float volMax = 120.0;//maximum volume
float volMin = 30.0;//minimum volume

float vol;//This is the amplitude (functionally volume) used in the synthesizer
int off=0;//This is the variable for the amplitude when a note is turned off

int closeCount=0;//count how many times performers have come close
int farCount=0;//count how many time performers have been far - closenessCount can't increase 
//until this has an equal value

AudioOutput out;//audio output
SineWave sineG;//audio G sine wave
SineWave sineC;//audio C sine wave
SineWave sineF;//audio F sine wave
SquareWave squareB;//B flat sine wave
Minim minim;//minim session

//count the number of times squareB plays
int loopCount=0;
//End audio variables

void setup() {
  size(800, 600);
  background(0);
  //smooth();
  //**SECTION HEAD------SERIAL EVENT-----**//
  //**This section created a serial event. The serial port reads the rssi strength up to the
  //size of of the window array "readings". This will then be averaged later as the
  //functional RSSI(the control value for all dynamics in the project)
  //println(Serial.list());
  String arduinoPort = Serial.list()[0];
  port = new Serial(this, arduinoPort, 9600);
  for (int thisReading = 0; thisReading < window_size; thisReading++)
    readings[thisReading]=0;

  minim = new Minim(this);
  // get a line out from Minim, default bufferSize is 1024, default sample rate is 44100, bit depth is 16
  out = minim.getLineOut(Minim.STEREO);
}

void serialEvent (Serial myPort) {
  // get the signal strength between the dancers' xbee modules through the serial port:
  rssi= myPort.read();
}

void draw() {
  println(rssi);  
  //  smooth();

  //**SECTION HEADER ---- PROCESS THE RSSI **\\    
  total = total - readings[index];
  readings[index] = rssi;
  total = total + readings[index];
  index = index + 1;

  if (index == window_size)
    index = 0;

  average = (total / window_size);

  average= map(average, low_rssi, high_rssi, 1, width);  //1 is min screen width

    if (average<=0)
    average=0; 
  //**SECTION FOOTER ----PROCESS THE RSSI***\\



  //*MAIN HEADER -----------AUDIO---------------******\\  

  //**SECTION HEAD------SYNTH VOLUME CONTROLLER------**//
  //**The following code increments the volume while the performers are very close
  //and decrements it if they are away from each other**//
  //**Use the closeCount to control which notes are played


  //ending
  // if (loopCount>=10) {
  if (rssi>=58) {
    if (farCount==4)farCount=closeCount;
    else if (closeCount>farCount) {
      farCount+=1;
      loopCount+=1;
    }
  }

  if (rssi<=36 && closeCount==farCount) {
    closeCount+=1;
  }

  //blue, red

  float backgroundRed=map(rssi, 33, 80, 200, 255);
  float backgroundGreen=map(rssi, 33, 80, 200, 100);
  float backgroundBlue=map(rssi, 33, 80, 255, 0);

  println("R: " + backgroundRed + " G: " + backgroundGreen + " B: " + backgroundBlue);

  background(backgroundRed, backgroundGreen, backgroundBlue);
  noStroke();
  fill(255-backgroundRed, 255-backgroundGreen, 255-backgroundBlue);
  rect(0, 0, 800, 600, map(rssi, 33, 90, 20, 1500), map(rssi, 33, 90, 20, 1000));
  fill(255-backgroundRed+80, 255-backgroundGreen+80, 255-backgroundBlue+80);
  rect(200,150, 400, 300, map(rssi, 33, 90, 20, 1500), map(rssi, 33, 90, 20, 1000));
  fill(255-backgroundRed-40, 255-backgroundGreen-40, 255-backgroundBlue-40);
  rect(300,200, 200, 150, map(rssi, 33, 90, 20, 1500), map(rssi, 33, 90, 20, 1000));
  rectMode(CORNER);
  //rect(0,0,800,600);

  //background(255, 165, 0);
  delay(200);
  smooth();
}

