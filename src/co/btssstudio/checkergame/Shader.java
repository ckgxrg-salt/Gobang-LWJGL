package co.btssstudio.checkergame;

import static org.lwjgl.opengl.GL40.*;

public class Shader {
	private int vertexShader;
	private int fragmentShader;
	private int program;
	public Shader(String vertCode, String fragCode) {
		vertexShader = glCreateShader(GL_VERTEX_SHADER);
		fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(vertexShader, Util.pathToString(vertCode));
		glShaderSource(fragmentShader, Util.pathToString(fragCode));
		glCompileShader(vertexShader);
		glCompileShader(fragmentShader);
		program = glCreateProgram();
		glAttachShader(program, vertexShader);
		glAttachShader(program, fragmentShader);
		glLinkProgram(program);
		glValidateProgram(program);
		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
	}
	public void use() {
		glUseProgram(program);
	}
	public void disable() {
		glUseProgram(0);
	}
	public int getProgram() {
		return program;
	}
	public int uniformLoc(String name) {
		return glGetUniformLocation(program, name);
	}
	public void uniformVec4(String name, float v1, float v2, float v3, float v4){
	    glUniform4f(uniformLoc(name), v1, v2, v3, v4);
	}
}
