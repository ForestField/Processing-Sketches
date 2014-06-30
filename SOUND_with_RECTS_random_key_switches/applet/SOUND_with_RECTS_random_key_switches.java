import processing.core.*; 
import processing.xml.*; 

import pitaru.sonia_v2_9.*; 
import processing.serial.*; 
import ddf.minim.*; 
import ddf.minim.signals.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class SOUND_with_RECTS_random_key_switches extends PApplet {

//control a bleepy sound with the RSSI between the two moving lilypad units.  Also rectangles are visualized.
//variable for rssi is "int val"
//sept 2 2012, we'd just gone to the zoo - saw the guys, the new wolves, the new sea eagle (east asian counterpart to the bald eagle), an arctic fox! 
//so THEN, I added Easing to this sketch - I don't know if it's really working or not.





Minim minim;
AudioOutput out;
SineWave sine;
SineWave sine1;
SineWave sine2;
SineWave sine3;
SquareWave square1; 
SquareWave square2;
SquareWave square3;
SquareWave square4;

Serial port;  // Create object from Serial class
Rectangle outerRect;
Rectangle outerRect1;
Rectangle midRect;
Rectangle innerRect1;
Rectangle innerRect;

int val;    // RSSI from basestation
int[] readings = new int[40];
int index = 0;
int total = 0;
int average = 0;
int audio = 0;
int h_on_off; //height on off  
int sw= 0;//initial strokeWeight


int state=1;//1 means major key switch, 0 means minor key switch
int tempState;//stores previous state for comparison
float rand;//random number between 0,1. randomized on sound state change



float RSSI=0;
float theta;  
float a;
float c;
float s;
float magnitude;
float r = random(0,255);
float g = random(0,255);
float b = random(0,255);
int pos;

class Rectangle{
  
  float w;
  float h;
  int clr;
  
 Rectangle(float tempW, float tempH, int tempClr){
   w=tempW;
   h=tempH;
   clr=tempClr;
 }
  
  public void display(){
    
      rectMode(CENTER);
      strokeWeight(0);
      fill(clr);
      rect (width/2,height/2,w,h);
  }
}  

public void setup() {
  size(screenWidth, screenHeight);
  background(255-r, 255-g, 255-b);
  minim = new Minim(this);
  // get a line out from Minim, default bufferSize is 1024, default sample rate is 44100, bit depth is 16
  out = minim.getLineOut(Minim.STEREO);
  // create a sine wave Oscillator, set to 27.5 Hz, at 0.5 amplitude, sample rate from line out
  //sine = new SineWave(27.5, 0.06, out.sampleRate());
  //sine1= new SineWave(27.5, 0.06, out.sampleRate());
  //sine2= new SineWave(27.5, 0.06, out.sampleRate());
  //sine3= new SineWave(27.5, 0.06, out.sampleRate());
  square1 = new SquareWave(27.5f, .06f, 44100);
  square2 = new SquareWave(27.5f, .06f, 44100);
  square3 = new SquareWave(27.5f, .06f, 44100);
  square4 = new SquareWave(27.5f, .06f, 44100);
  // set the portamento speed on the oscillator to 200 milliseconds
  //sine.portamento(200);
  //sine1.portamento(200);
  //sine2.portamento(200);
  //sine3.portamento(200);  // add the oscillator to the line out
  square1.portamento(20);
  square2.portamento(20);  
  square3.portamento(20);  
  square4.portamento(20);  
  //out.addSignal(sine);
  //out.addSignal(sine1);
  //out.addSignal(sine2);
  //out.addSignal(sine3);
  out.addSignal(square1);
  out.addSignal(square2);
  out.addSignal(square3);
  out.addSignal(square4);
  println(Serial.list());
  String arduinoPort = Serial.list()[0];
  port = new Serial(this, arduinoPort, 9600);
  for (int thisReading = 0; thisReading < 40; thisReading++)
    readings[thisReading] = 0;  
  smooth();
}
public void serialEvent (Serial myPort) {
  // get the byte:
  val = myPort.read();
}

public void draw()
{
  
   if(tempState!=state){getRand();
  println("heeeeeeeeeeeeeeeeeeeey");
println(rand);}
 tempState=state;  
   background(255-r, 255-g, 255-b);
  
   if (average>=1200){
    r= random(0, 255);
    g= random(0, 255);
    b= random(150, 200);    
  }
  
  if (val<38)
     h_on_off=0;
  if (val>=38)
     h_on_off=1;
  
  outerRect = new Rectangle ((a*10),(random(0,255))*h_on_off,color (r,g,b,50));
  outerRect1 = new Rectangle ((a*5),(val+100)*h_on_off,color (255-2, 255,255-2*b,75));
  midRect = new Rectangle ((a*2),(val+50)*h_on_off,color (random(0,255),g,b,100));
  innerRect1= new Rectangle ((a),(val+25)*h_on_off,color ((255-r), (255), (255-(b)),255)); 
  innerRect = new Rectangle (a/2,val*h_on_off,color (r-20,g-20,b-20));
//target rssi-current rssi
//float easing=.5;
//
//
//float x=abs(val-RSSI)*easing;
float freq;
float freq0;// = map(val,36,80, 0,27.5);
float freq1;
float freq2;
float freq3;
float freq4;
float freq5;
float freq6;
float freq7;
float freq8;

if (val<38){state=1;
freq0=27.5f;
freq=freq0;
square1.setFreq(freq);}

else if (val>=38 && val<=40){
//
state=2;
freq1=27.5f;
freq=freq1;
square1.setFreq(freq);
float note;
if (rand==0){note=5*freq/4;
square2.setFreq(note);
square3.setFreq((note)+(5*note/4));//minor 3rd
square4.setFreq((note)+(6*note/5)+(6*freq/5));}
else if(rand==1){note=6*freq/5;
square2.setFreq(note);
square3.setFreq((note)+(5*note/4));//minor 3rd
square4.setFreq((note)+(6*note/5)+(6*freq/5));}
}
else if (val>=40 && val<=52){
//
state=3;
freq2=27.5f;
freq=freq2;
float note;
if (rand==0){note=5*freq/4;
square2.setFreq(note);
square3.setFreq((note)+(5*note/4));//minor 3rd
square4.setFreq((note)+(6*note/5)+(6*freq/5));}
else if(rand==1){note=6*freq/5;
square2.setFreq(note);
square3.setFreq((note)+(5*note/4));//minor 3rd
square4.setFreq((note)+(6*note/5)+(6*freq/5));}
}


else if (val>=54 && val<=60){
state=4;
freq3=27.5f+27.5f*3/2;
freq=freq3;
square1.setFreq(freq);
float note;
if (rand==0){note=5*freq/4;
square2.setFreq(note);
square3.setFreq((note)+(5*note/4));//minor 3rd
square4.setFreq((note)+(6*note/5)+(6*freq/5));}
else if(rand==1){note=6*freq/5;
square2.setFreq(note);
square3.setFreq((note)+(5*note/4));//minor 3rd
square4.setFreq((note)+(6*note/5)+(6*freq/5));}

}
//minor3rd}

 //major 3rd
// minor 7th



else if (val>=60 && val<=68){
//
state=5;
freq4=27.5f+27.5f*3/2+27.5f*3/2;
freq=freq4;
square1.setFreq(freq);
float note;
if (rand==0){note=5*freq/4;
square2.setFreq(note);
square3.setFreq((note)+(5*note/4));//minor 3rd
square4.setFreq((note)+(6*note/5)+(6*freq/5));}
else if(rand==1){note=6*freq/5;
square2.setFreq(note);
square3.setFreq((note)+(5*note/4));//minor 3rd
square4.setFreq((note)+(6*note/5)+(6*freq/5));}
}
else if (val>=68 && val<=70){
//
state=6;
freq5=27.5f+27.5f*3/2+27.5f*3/2;
freq=freq5;
square1.setFreq(freq);
float note;
if (rand==0){note=5*freq/4;
square2.setFreq(note);
square3.setFreq((note)+(5*note/4));//minor 3rd
square4.setFreq((note)+(6*note/5)+(6*freq/5));}
else if(rand==1){note=6*freq/5;
square2.setFreq(note);
square3.setFreq((note)+(5*note/4));//minor 3rd
square4.setFreq((note)+(6*note/5)+(6*freq/5));}
}
else if (val>=70 && val<=80){
//
state=7;
freq6=27.5f+27.5f*3/2+27.5f*3/2+27.5f/3*2;
freq=freq6;
square1.setFreq(freq);
float note;
if (rand==0){note=5*freq/4;
square2.setFreq(note);
square3.setFreq((note)+(5*note/4));//minor 3rd
square4.setFreq((note)+(6*note/5)+(6*freq/5));}
else if(rand==1){note=6*freq/5;
square2.setFreq(note);
square3.setFreq((note)+(5*note/4));//minor 3rd
square4.setFreq((note)+(6*note/5)+(6*freq/5));}
}

else if (val>80){
//
state=8;
freq7=27.5f+27.5f*3/2+27.5f*3/2+27.5f*3/2+27.5f/3*2;
freq=freq7;
square1.setFreq(freq);
float note;
if (rand==0){note=5*freq/4;
square2.setFreq(note);
square3.setFreq((note)+(5*note/4));//minor 3rd
square4.setFreq((note)+(6*note/5)+(6*freq/5));}
else if(rand==1){note=6*freq/5;
square2.setFreq(note);
square3.setFreq((note)+(5*note/4));//minor 3rd
square4.setFreq((note)+(6*note/5)+(6*freq/5));}
}
//sine.setFreq(freq);
//sine1.setFreq(freq/3);
//sine2.setFreq(3*freq);
//sine3.setFreq(freq/40);
//square1.setFreq(freq*2);
//square2.setFreq(freq*10);

//
//int randChord=random(0,1);
//println randChord;


//square1.setFreq(freq);
//square2.setFreq(freq);


//println(freq);
println(val);
  total = total - readings[index];
  readings[index] = val;
  total = total + readings[index];
  index = index + 1;

  if (index >=10 )
    index = 0;

  average = total / 10;
  //average= val;
  audio = average;
  //println (average);
  average = (1 + (average-25)*(1350-1)/(70-25));

  if (average<=38)
    average=0;
    
  magnitude = (1.1f-  (.1f +    ((audio-36) *  (1-.1f)/ (80-36)))  );
  frameRate(30); 
//  stroke(r, g, b);
//    
//rectMode(CENTER);
//rect(width/2,height/2,a*5, val);   
   outerRect.display();
   outerRect1.display();
   
   midRect.display();
   innerRect1.display();
   innerRect.display(); 

  if (val<=36)
   a=0;

    a =  (average/ (float) width) * 135f;
//  theta = radians(a);
//
//  noFill();
}


public void getRand() {  
  rand=random(1);
  if (rand<.5f) {
    rand=0;
  }
  else if (rand>.5f) {
    rand=1;
  }
  println(rand);
  println("ran state control");
}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--present", "--bgcolor=#666666", "--hide-stop", "SOUND_with_RECTS_random_key_switches" });
  }
}
