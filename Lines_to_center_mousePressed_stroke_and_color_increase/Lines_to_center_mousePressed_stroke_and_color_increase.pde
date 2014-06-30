int count;

void setup(){
size(800,600);
smooth();
background(100);
}

void draw(){
  linesToCenter();
if(mousePressed){
  strokeWeight(count);
  stroke(random(0,255));
count+=1;
println(count);
}
if (count>=200){
  stroke(random(0,255),random(0,255),random(0,255));
     
  
}
delay(10);
}


void linesToCenter(){
  line(mouseX,mouseY, width/2,height/2);
}


void mouseReleased(){
  pushMatrix();
  //translate(5,5);
  //rotate(1);
  stroke(255);
  fill(0);
 // rectMode(CENTER);
  rect(mouseX,mouseY,100,100);
  popMatrix();
  strokeWeight(0);
  stroke(0);
  count=0;
  
}
