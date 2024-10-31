package co.btssstudio.checkergame;

import static org.lwjgl.opengl.GL40.*;
import java.nio.IntBuffer;

public class Texture {
	public int texID;
	public Texture(String path) {
		Util.ImageData idata = Util.readImageData(path);
		int[] data = idata.getData();
		texID = glGenTextures();
		bind();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_MIRRORED_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_MIRRORED_REPEAT);
		IntBuffer d = Util.fillBufferWithInt(data);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, idata.getWidth(), idata.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, d);
		unbind();
	}
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, texID);
	}
	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	public void genMipMap() {
		bind();
		glGenerateMipmap(GL_TEXTURE_2D);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_NEAREST);
		unbind();
	}
}
