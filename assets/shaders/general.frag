#version 400 core

out vec4 FragColor;
uniform vec4 color;

void main(){
    FragColor = color;
    if(FragColor.a < 0.1){
        discard;
    }
}