int p=0;//padding
int sw=1;//stroke weight
int i=1;//stroke weight increment
int d=100;//delay
int t=100;//transparancy
int dm=50;//ellipse diameter

void setup(){
smooth();
size(400,600);
background(random(0,255),random(0,255),random(0,255));
}
void draw(){
strokeWeight(sw);
stroke(random(0,255),random(0,255),random(0,255),t);  
if(mouseX<width/2+p && mouseY<height/2+p){
line(mouseX, mouseY, width/2, height/2);
line(0,0,mouseX, mouseY);
ellipse(3*width/4,3*height/4, dm, dm);
delay(d);
}
if(mouseX>width/2+p && mouseY<height/2-p){
line(mouseX, mouseY, width/2, height/2);
line(width,0,mouseX, mouseY);
ellipse(3*width/4,height/4, dm, dm);
delay(d);
}

if(mouseX>width/2+p && mouseY>height/2+p){
line(mouseX, mouseY, width/2, height/2);
line(width,height,mouseX, mouseY);
delay(d);
ellipse(width/4,3*height/4, dm, dm);
}

if(mouseX<width/2-p && mouseY>height/2+p){
line(mouseX, mouseY, width/2, height/2);
line(0,height,mouseX, mouseY);
ellipse(width/4,height/4, dm, dm);
delay(d);
}

}

void mousePressed(){
  background(random(0,255),random(0,255),random(0,255));
  sw+=i;
}
