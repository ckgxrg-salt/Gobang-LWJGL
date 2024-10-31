package co.btssstudio.checkergame;

import static org.lwjgl.opengl.GL40.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class GameObject {
	byte[] index;
	float[] vert;
	int VAO, VBO, IBO;
	float r,g,b,a;
	boolean visible;
	Shader shader;
	/*
	 * int VAO = glGenVertexArrays();
	    			glBindVertexArray(VAO);
	    			int VBO = glGenBuffers();
	    			glBindBuffer(GL_ARRAY_BUFFER, VBO);
	    			glBufferData(GL_ARRAY_BUFFER, boardvertices, GL_STATIC_DRAW);
	    			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
	    			int IBO = glGenBuffers();
	    			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, IBO);
	    			glBufferData(GL_ELEMENT_ARRAY_BUFFER, boardindicies, GL_STATIC_DRAW);
	    			glEnableVertexAttribArray(0);
	    			glUseProgram(program);
	    			glDrawArrays(GL_TRIANGLES, 0, 3);
	    			glfwSwapBuffers(window);
	 */
	public GameObject(float[] vert, byte[] index, Shader shader) {
		this(vert, index, shader, false);
	}
	/*public GameObject(byte[] index, Shader shader, boolean isDynamic) {
		this.index = index;
		ByteBuffer iBuffer = Util.fillBufferWithByte(index);
		
		VAO = glGenVertexArrays();
		glBindVertexArray(VAO);
		
		VBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		
		IBO = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, IBO);
		if(isDynamic) {
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, iBuffer, GL_DYNAMIC_DRAW);
		} else {
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, iBuffer, GL_STATIC_DRAW);
		}
		glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}*/
	public GameObject(byte[] index, Shader shader, boolean isDynamic) {
		this.index = index;
		ByteBuffer iBuffer = Util.fillBufferWithByte(index);
		
		VAO = glGenVertexArrays();
		glBindVertexArray(VAO);
		
		VBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		
		IBO = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, IBO);
		if(isDynamic) {
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, iBuffer, GL_DYNAMIC_DRAW);
		} else {
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, iBuffer, GL_STATIC_DRAW);
		}
		glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		visible = false;
		this.shader = shader;
	}
	public GameObject(float[] vert, byte[] index, Shader shader, boolean isDynamic) {
		this(vert, index, shader, isDynamic, false);
	}
	public GameObject(float[] vert, byte[] index, Shader shader, boolean isDynamic, boolean isVisible) {
		this.index = index;
		this.vert = vert;
		FloatBuffer vBuffer = Util.fillBufferWithFloat(vert);
		ByteBuffer iBuffer = Util.fillBufferWithByte(index);
		
		VAO = glGenVertexArrays();
		glBindVertexArray(VAO);
		
		VBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		if(isDynamic) {
			glBufferData(GL_ARRAY_BUFFER, vBuffer, GL_DYNAMIC_DRAW);
		} else {
			glBufferData(GL_ARRAY_BUFFER, vBuffer, GL_STATIC_DRAW);
		}
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		
		IBO = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, IBO);
		if(isDynamic) {
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, iBuffer, GL_DYNAMIC_DRAW);
		} else {
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, iBuffer, GL_STATIC_DRAW);
		}
		glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		this.visible = isVisible;
		this.shader = shader;
	}
	public void shader(Shader shader) {
		this.shader = shader;
	}
	public void visible(boolean value) {
		visible = value;
	}
	public void color(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	public void bind() {
		shader.use();
		shader.uniformVec4("color", r, g, b, a);
		glBindVertexArray(VAO);
		glEnableVertexAttribArray(0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, IBO);
	}
	public void unbind() {
		glBindVertexArray(0);
		glDisableVertexAttribArray(0);
		shader.disable();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	public void update(float[] vert) {
		this.vert = vert;
		FloatBuffer vBuffer = Util.fillBufferWithFloat(vert);
		glBindVertexArray(VAO);
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, vBuffer, GL_DYNAMIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}
	public void draw() {
		if(!visible)
			return;
		bind();
		glDrawElements(GL_TRIANGLES, index.length, GL_UNSIGNED_BYTE, 0);
		unbind();
	}
}