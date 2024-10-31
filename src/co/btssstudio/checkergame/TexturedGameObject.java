package co.btssstudio.checkergame;

import static org.lwjgl.opengl.GL40.*;

import java.nio.FloatBuffer;

public class TexturedGameObject extends GameObject {

	public static final float[] TEXCOORD = {
			0f, 1f,//LB
			0f, 0f,//LT
			1f, 0f,//RT
			1f, 1f//RB
	};
	
	int TBO;
	Texture tex;
	
	public TexturedGameObject(float[] vert, byte[] index, Texture tex) {
		super(vert, index, Main.texed, true, false);
		this.tex = tex;
		glBindVertexArray(VAO);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		TBO = glGenBuffers();
		FloatBuffer tBuffer = Util.fillBufferWithFloat(TEXCOORD);
		glBindBuffer(GL_ARRAY_BUFFER, TBO);
		glBufferData(GL_ARRAY_BUFFER, tBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
	}
	public void update(Texture tex) {
		this.tex = tex;
	}
	@Override
	public void bind() {
		shader.use();
		shader.uniformVec4("color", r, g, b, a);
		tex.bind();
		glBindVertexArray(VAO);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, IBO);
	}
	@Override
	public void unbind() {
		glBindVertexArray(0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		shader.disable();
		tex.unbind();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}
}
